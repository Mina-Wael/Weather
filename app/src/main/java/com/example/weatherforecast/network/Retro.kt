package com.example.howsweather.network

import com.example.howsweather.model.Forecast
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retro() {
//make it singleton

    lateinit var retrofitInterface:RetrofitInterface
    lateinit var retrofit: Retrofit

    private val BASE_URL = "https://api.openweathermap.org/"
    init {
        retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
        retrofitInterface = retrofit.create(RetrofitInterface::class.java)
    }

    fun getWeatherData():Call<Forecast>{
        return retrofitInterface.getAllWeather(30.013056, 31.208853)
    }
}