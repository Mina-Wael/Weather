package com.example.weatherforecast.ui.home


import android.content.Context
import android.location.Location
import androidx.lifecycle.*
import com.example.howsweather.model.Forecast
import com.example.weatherforecast.repository.Repository
import com.example.weatherforecast.util.Helper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.paperdb.Paper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class HomeViewModel(var context: Context) : ViewModel(){
    var repo: Repository
    private var _foreCastList:MutableStateFlow<Forecast> = MutableStateFlow<Forecast>(Forecast())

    private var _networkState = MutableLiveData<Boolean>()
    var networkState: LiveData<Boolean> = _networkState

    private var _locationState = MutableLiveData<Location>()
    var locationState: LiveData<Location> = _locationState

    private var _forecastMutable = MutableLiveData<Forecast>()
    var forecastMutable: LiveData<Forecast> = _forecastMutable



    lateinit var fusedLocationClient: FusedLocationProviderClient

    private val _stateMutableLiveData:MutableLiveData<HomeState> = MutableLiveData()
    val stateLiveData:LiveData<HomeState> = _stateMutableLiveData

    init {
        repo = Repository.getInstance(context)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        Paper.init(context)
    }

    fun checkNetwork(){
        viewModelScope.launch (Dispatchers.IO){
            _networkState.postValue(Helper.check.hostAvailable())
        }
    }


    fun getData(lat: Double, lng: Double, lang: String?, unit: String?) {

        viewModelScope.launch(Dispatchers.IO) {

            _stateMutableLiveData.postValue(HomeState.Loading)

           val res= repo.getApiData(lat, lng, lang, unit)
            if (res.isSuccessful){
                _stateMutableLiveData.postValue(HomeState.OnSuccess(res.body()!!))
                repo.deleteOld()
                repo.insertForecast(res.body()!!)
            }
            else
                _stateMutableLiveData.value=HomeState.OnFail(OnFailMassage.ServerError)
        }
    }

    fun startListening(homeFragment: HomeFragment) {
        viewModelScope.launch {
            repo.getForecast().observe(homeFragment, Observer {

                 if(it!=null)  {
                     _forecastMutable.value =it
                 }

            })
        }

    }

    var forecast: MutableStateFlow<Forecast> = _foreCastList

sealed class HomeState {
    class OnSuccess(forecast: Forecast): HomeState()
    class OnFail(msg:OnFailMassage) : HomeState()
    object Loading: HomeState()
}

    enum class OnFailMassage{
        NoInternet,ServerError
    }

}