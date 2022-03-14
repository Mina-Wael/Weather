package com.example.weatherforecast.ui.favorite

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howsweather.model.Forecast
import com.example.weatherforecast.repository.Repository
import kotlinx.coroutines.launch

class FavoriteViewModel(var context: Context) : ViewModel() {
    var repo:Repository
    init {
        repo= Repository.getInstance(context)
    }

    private val _favoriteList = MutableLiveData<List<Forecast>>()


    val favoriteList: LiveData<List<Forecast>> = _favoriteList

    fun getFavoriteList(favoriteFragment: FavoriteFragment)
    {
        viewModelScope.launch {
            repo.getFavoriteList().observe(favoriteFragment, Observer {
                _favoriteList.value=it
            })
        }
    }
    fun deleteFavorite(forecast: Forecast)
    {

            repo.deleteForecast(forecast)


    }
}