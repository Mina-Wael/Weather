package com.example.weatherforecast.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.howsweather.model.Forecast
import com.example.howsweather.network.Retro
import com.example.weatherforecast.database.LocalDatabase
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response


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


    suspend fun getApiData(lat: Double, lng: Double,lang:String?,unit:String?):Response<Forecast> =
              retro.getWeatherData().getAllWeather(lat, lng,lang,unit)


    suspend fun insertForecast(forecast: Forecast) {
        localDatabase.insertForecast(forecast)
    }

    fun getForecast(): LiveData<Forecast> = localDatabase.getForecast()


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

    fun getFavoriteList(): LiveData<List<Forecast>> =  localDatabase.getFavorite()

    fun deleteForecast(forecast: Forecast) {
        GlobalScope.launch {
            localDatabase.deleteForecast(forecast)
        }

    }

    suspend fun getForecastById(id: Int): LiveData<Forecast> =  localDatabase.getForecastById(id)


}