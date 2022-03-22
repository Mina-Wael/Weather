package com.example.weatherforecast.ui.favorite.details

import android.content.Context
import androidx.lifecycle.*
import com.bumptech.glide.manager.Lifecycle
import com.example.howsweather.model.Forecast
import com.example.weatherforecast.repository.Repository
import com.example.weatherforecast.ui.home.HomeFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteDetailsViewModel(context: Context) : ViewModel() {
    var repo: Repository
    private var _foreCast = MutableLiveData<Forecast>()

    init {
        repo= Repository.getInstance(context)
    }



    fun getForecastById(context: LifecycleOwner,id:Int)
    {

        viewModelScope.launch {
          repo.getForecastById(id).observe(context, Observer {
              _foreCast.value=it
          })
        }

    }
    fun getForecastByLatlng(latLng: LatLng){}
    var forecast: LiveData<Forecast?> = _foreCast

}