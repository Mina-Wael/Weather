package com.example.weatherforecast.ui.favorite

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howsweather.model.Forecast
import com.example.weatherforecast.repository.Repository
import com.example.weatherforecast.util.Helper
import kotlinx.coroutines.*

class FavoriteViewModel(var context: Context) : ViewModel() {
    var repo:Repository
    init {
        repo= Repository.getInstance(context)
    }

    private val _favoriteList = MutableLiveData<List<Forecast>>()
    private val _net = MutableLiveData<Boolean>()
     val net : LiveData<Boolean> = _net


    val favoriteList: LiveData<List<Forecast>> = _favoriteList

    fun getFavoriteList(favoriteFragment: FavoriteFragment)
    {
        viewModelScope.launch {
//            repo.getFavoriteList().observe(favoriteFragment, Observer {
//                _favoriteList.value=it
//            })
        }
    }
    fun deleteFavorite(forecast: Forecast)
    {
//            repo.deleteForecast(forecast)
    }
    fun checkNetwork(){
        var value:Deferred<Boolean>
       viewModelScope.launch(Dispatchers.IO) {
           value=async{ Helper.check.hostAvailable() }
           setData(value.await())
       }
    }
    fun setData(d:Boolean)
    {
        viewModelScope.launch {
            _net.value=d
        }
    }

}