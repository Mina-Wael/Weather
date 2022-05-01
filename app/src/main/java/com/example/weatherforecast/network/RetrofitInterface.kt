package com.example.howsweather.network

import com.example.howsweather.model.Forecast
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("data/2.5/onecall?appid=34fac36c0ac114e02d946fe20a18ff0a")
    suspend fun getAllWeather(@Query("lat")lat:Double,
                              @Query("lon")lon:Double,
                              @Query("lang") lang:String?,
                              @Query("units")units:String?): Response<Forecast>

}