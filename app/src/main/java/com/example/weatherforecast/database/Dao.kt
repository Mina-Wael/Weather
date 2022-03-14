package com.example.howsweather.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Query
import androidx.room.Dao
import com.example.howsweather.model.Forecast
import androidx.room.Delete
import androidx.room.Insert


@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertForecast(forecast: Forecast)

    @Delete
    fun deleteForecast(forecast: Forecast)

    @Query("Select * FROM Forecast where favorite = 0")
    fun getForecast(): LiveData<Forecast>

    @Query("Select * FROM Forecast where favorite = 1")
    fun getFavorite(): LiveData<List<Forecast>>

    @Query("delete FROM Forecast where favorite = 0")
    fun deleteOld()


}