package com.example.howsweather.network

import com.example.howsweather.model.Forecast
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retro() {
//make it singleton

    lateinit var retrofitInterface:RetrofitInterface
    lateinit var retrofit: Retrofit

    private val BASE_URL = "https://api.openweathermap.org/"
    init {
        retrofit = Retrofit.Builder().baseUrl(BASE_URL).
        addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    }

    fun getWeatherData(): RetrofitInterface {
        return retrofit.create(RetrofitInterface::class.java)
    }
}