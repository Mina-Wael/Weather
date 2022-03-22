package com.example.weatherforecast.repository

import android.accounts.NetworkErrorException
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
import java.text.SimpleDateFormat
import java.util.*

class Repository private constructor(var context: Context) {

    lateinit var localDatabase: LocalDatabase
    lateinit var retro: Retro

    init {
        localDatabase = LocalDatabase.getInstance(context)
        retro = Retro()
    }

    companion object {
        @Volatile
        private var repo: Repository? = null

        @Synchronized
        public fun getInstance(context: Context): Repository {
            if (repo == null) {
                repo = Repository(context)

            }
            return repo!!
        }
    }


    suspend fun getApiData(lat: Double, lng: Double,lang:String?,unit:String?) {
        deleteOld()
        try
        {
          val  res = retro.getWeatherData().getAllWeather(lat, lng,lang,unit)
            if (res.isSuccessful) {
                Log.i("date", "getApiData: "+convertLongToDayDate(res.body()!!.daily.get(0).dt))
                Log.i("date", "getApiData: "+convertLongToDayDate(res.body()!!.daily.get(1).dt))
                Log.i("date", "getApiData: "+convertLongToDayDate(res.body()!!.daily.get(2).dt))
                Log.i("date", "getApiData: "+convertLongToDayDate(res.body()!!.daily.get(3).dt))
                Log.i("date", "getApiData: "+res.body()!!.daily.get(0).dt)
                Log.i("date", "getApiData: "+res.body()!!.daily.get(1).dt)
                Log.i("date", "getApiData: "+res.body()!!.daily.get(2).dt)
                Log.i("date", "getApiData: "+res.body()!!.daily.get(3).dt)



                insertForcast(res.body()!!)
                Log.i("TAG", "getApiData: success ")
            }

        }catch (net:NetworkErrorException)
        {
            Log.i("net", "getApiData: error"+net.message)
        }

    }

    fun convertLongToDayDate(time: Long): Int {
        val date = Date(time)
        date.day
        return date.day
    }

    suspend fun insertForcast(forecast: Forecast) {

        localDatabase.insertForecast(forecast)
    }

    fun getForecast(): LiveData<Forecast> {
        return localDatabase.getForecast()

    }

    fun deleteOld() {
        GlobalScope.launch { localDatabase.deleteOld() }
    }

    fun insertFavorite(latLng: LatLng,lang:String?,temp:String?) {
        var forecast: Forecast
        GlobalScope.launch {
            Log.i("map", ":lat3 "+latLng!!.latitude+" lng3 "+latLng!!.longitude)
            var res = retro.getWeatherData().getAllWeather(latLng.latitude, latLng.longitude,lang,temp)
            if (res.isSuccessful) {
                Log.i("map", "insertFavorite: success ")
                forecast = res.body()!!
                forecast.setFavo(1)
                localDatabase.insertForecast(forecast)
            }
        }

    }

    fun getFavoriteList(): LiveData<List<Forecast>> {
        return localDatabase.getFavorite()
    }

    fun deleteForecast(forecast: Forecast) {
        GlobalScope.launch {
            localDatabase.deleteForecast(forecast)
        }

    }

    suspend fun getForecastById(id: Int): LiveData<Forecast> {
        return localDatabase.getForecastById(id)
    }


}