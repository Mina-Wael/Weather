package com.example.weatherforecast.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.howsweather.database.DatabaseBuilder
import com.example.howsweather.model.Forecast
import com.example.howsweather.network.Retro
import com.example.weatherforecast.database.LocalDatabase
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class Repository private constructor(var context: Context) {

       lateinit var localDatabase : LocalDatabase
    lateinit var retro:Retro

    init {
        localDatabase= LocalDatabase.getInstance(context)
        retro= Retro()
    }
    companion object {
        @Volatile
        private var repo: Repository? = null
        @Synchronized
        public fun getInstance(context: Context): Repository {
            if (repo == null) {
                repo =Repository(context)

            }
            return repo!!
        }
    }



suspend fun getApiData(lat:Double,lng:Double)
{
    deleteOld()

   val res= retro.getWeatherData().getAllWeather(lat,lng)
    if (res.isSuccessful){

       insertForcast(res.body()!!)
        Log.i("TAG", "getApiData: success ")
    }
}
    suspend fun insertForcast(forecast: Forecast)
    {

     localDatabase.insertForecast(forecast)
    }

    fun getForecast():LiveData<Forecast>
    {
        return localDatabase.getForecast()

    }
    fun deleteOld()
    {
        GlobalScope.launch { localDatabase.deleteOld() }
    }

    fun insertFavorite(latLng: LatLng)
    {
        var forecast:Forecast
        GlobalScope.launch {
           var res= retro.getWeatherData().getAllWeather(latLng.latitude,latLng.longitude)
            if (res.isSuccessful){
                forecast= res.body()!!
                forecast.setFavo(1)
                localDatabase.insertForecast(forecast)
            }
        }

    }

    fun getFavoriteList():LiveData<List<Forecast>>
    {
        return localDatabase.getFavorite()
    }

    fun deleteForecast(forecast: Forecast)
    {
        GlobalScope.launch {
            localDatabase.deleteForecast(forecast)
        }

    }



}