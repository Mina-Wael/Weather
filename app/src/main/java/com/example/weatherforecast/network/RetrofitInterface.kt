package com.example.howsweather.network

import com.example.howsweather.model.Forecast
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("data/2.5/onecall?appid=9a868e13d9d740c094ca6ab7c61167c9&units=metric")
    suspend fun getAllWeather(@Query("lat")lat:Double, @Query("lon")lon:Double): Response<Forecast>

}