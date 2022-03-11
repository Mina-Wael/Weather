package com.example.weatherforecast.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.howsweather.model.Daily
import com.example.howsweather.model.Forecast
import com.example.howsweather.network.Retro
import com.example.weatherforecast.model.Hourly
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class HomeViewModel : ViewModel() {

    private val _hourlyList = MutableLiveData<List<Hourly>>()
    val hourlyList: LiveData<List<Hourly>> = _hourlyList
    private val _dialyList = MutableLiveData<List<Daily>>()
    val dialyList: LiveData<List<Daily>> = _dialyList
    fun getData(){
    val retro: Retro = Retro()
    var c = retro.getWeatherData()
    c.enqueue(object : Callback, retrofit2.Callback<Forecast> {
        override fun onFailure(call: Call<Forecast>?, t: Throwable?) {

        }

        override fun onResponse(call: Call<Forecast>?, response: Response<Forecast>?) {
            _hourlyList.value= response?.body()?.hourly
            _dialyList.value= response?.body()?.daily
        }
    })}

}