package com.example.weatherforecast.ui.home


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
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


    var repo: Repository = Repository.getInstance(context)

    private var _networkState = MutableLiveData<Boolean>()
    var networkState: LiveData<Boolean> = _networkState

    private var _locationMutable = MutableLiveData<Location>()
    var locationLiveData: LiveData<Location> = _locationMutable

      var forecastLiveData: LiveData<Forecast> ?=null
       var forecast:Forecast?=null

     var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private val _stateMutableLiveData:MutableLiveData<HomeState> = MutableLiveData()
    val stateLiveData:LiveData<HomeState> = _stateMutableLiveData

    init {
        Paper.init(context)
    }

    fun checkNetwork(){
        viewModelScope.launch (Dispatchers.IO){
            _networkState.postValue(Helper.check.hostAvailable()&&Helper.check.isNetworkAvailable(context))
        }
    }



    fun getApiData(lat: Double, lng: Double, lang: String?, unit: String?) {

        viewModelScope.launch(Dispatchers.IO) {

            _stateMutableLiveData.postValue(HomeState.Loading)

           val res= repo.getApiData(lat, lng, lang, unit)
            if (res.isSuccessful){
                _stateMutableLiveData.postValue(HomeState.OnSuccess(res.body()!!))
                repo.deleteOld()
                repo.insertForecast(res.body()!!)
                forecast=res.body()!!
            }
            else
                _stateMutableLiveData.value=HomeState.OnFail(OnFailMassage.ServerError)
        }
    }

    fun getDataFromDatabase() {
           forecastLiveData= repo.getForecast()

    }

sealed class HomeState {
    class OnSuccess(var forecast: Forecast): HomeState()
    class OnFail(var msg:OnFailMassage) : HomeState()
    object Loading: HomeState()
}

    enum class OnFailMassage{
        NoInternet,ServerError,NoDataFound
    }
     fun checkGpsState(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun checkLocationPermission():Boolean=Helper.check.checkLocationPermission(context)

    @SuppressLint("MissingPermission")
    fun getLocation(){
        fusedLocationClient=LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            _locationMutable.value=it
        }
    }


}