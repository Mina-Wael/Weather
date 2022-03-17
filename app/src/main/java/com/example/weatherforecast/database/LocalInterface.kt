package com.example.weatherforecast.database

import androidx.lifecycle.LiveData
import com.example.howsweather.model.Forecast

interface LocalInterface {
    suspend fun insertForecast(forecast: Forecast)
    fun deleteForecast(forecast: Forecast)
    fun getForecast(): LiveData<Forecast>
    fun getFavorite():LiveData<List<Forecast>>
    suspend fun deleteOld()
    suspend fun getForecastById(id:Int):LiveData<Forecast>



}