package com.example.weatherforecast.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.howsweather.model.Forecast
import com.example.howsweather.network.Retro
import com.example.weatherforecast.repository.Repository
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch

class HomeViewModel (context: Context): ViewModel() {
      var repo:Repository
    private var _foreCastList = MutableLiveData<Forecast>()

    init {
        repo= Repository.getInstance(context)
    }



    fun getData(lat:Double,lng:Double) {
      viewModelScope.launch {
          repo.getApiData(lat,lng)
      }
    }

    fun startListening(homeFragment: HomeFragment)
    {

        viewModelScope.launch {
            repo.getForecast().observe(homeFragment, Observer {

                _foreCastList.value=it


            })
        }

    }
    var forecast: LiveData<Forecast?> = _foreCastList



}