package com.example.weatherforecast.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.room.Room
import com.example.howsweather.database.Dao
import com.example.howsweather.database.DatabaseBuilder
import com.example.howsweather.model.Forecast
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDatabase private constructor(var context: Context) :LocalInterface{
      lateinit var dao:Dao
      lateinit var databaseBuilder: DatabaseBuilder
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    init {
          databaseBuilder= DatabaseBuilder.getInstance(context)
          dao= databaseBuilder.getDao()!!

      }

    companion object {
        @Volatile
        private var localDatabase: LocalDatabase? = null
        @Synchronized
        public fun getInstance(context: Context): LocalDatabase {
            if (localDatabase == null) {
                localDatabase =
                    LocalDatabase(context)
            }
            return localDatabase!!
        }
    }
    override suspend fun insertForecast(forecast: Forecast) = withContext(ioDispatcher){
      dao.insertForecast(forecast)
    }

    override fun deleteForecast(forecast: Forecast) {
        dao.deleteForecast(forecast)
    }

    override fun getForecast(): LiveData<Forecast> {
        return dao.getForecast()
    }

    override fun getFavorite(): LiveData<List<Forecast>>{
       return dao.getFavorite()
    }

    override suspend fun deleteOld() {
        dao.deleteOld()
    }
}