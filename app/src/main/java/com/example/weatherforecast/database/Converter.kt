package com.example.howsweather.database

import androidx.room.TypeConverter
import com.example.howsweather.model.Alerts
import com.example.howsweather.model.Current
import com.example.howsweather.model.Daily
import com.example.howsweather.model.Weather
import com.example.weatherforecast.model.Hourly
import com.example.weatherforecast.model.Minutely
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import kotlin.math.min


class Converter {

    @TypeConverter
    fun fromMinutelyToString(minutely: List<Minutely>): String {
        return Gson().toJson(minutely)
    }

    @TypeConverter
    fun fromHourlyToString(hourly: List<Hourly>): String {
        return Gson().toJson(hourly)
    }

    @TypeConverter
    fun fromDailyToString(daily: List<Daily>): String {
        return Gson().toJson(daily)
    }

    @TypeConverter
    fun fromCurrentToString(current: Current): String {
        return Gson().toJson(current)
    }

    @TypeConverter
    fun fromAlertsToString(alerts: List<Alerts>): String {
        return Gson().toJson(alerts)
    }

    @TypeConverter
    fun fromStringToMinutely(minutely: String): List<Minutely> {
        val listType: Type = object : TypeToken<List<Minutely?>?>() {}.type
        return Gson().fromJson(minutely, listType)
    }

    @TypeConverter
    fun fromStringToHourly(hourly: String): List<Hourly> {
        val listType: Type = object : TypeToken<List<Hourly?>?>() {}.type
        return Gson().fromJson(hourly, listType)
    }

    @TypeConverter
    fun fromStringToDaily(daily: String): List<Daily> {
        val listType = object : TypeToken<List<Daily?>?>() {}.type
        return Gson().fromJson(daily, listType)
    }

    @TypeConverter
    fun fromStringToAlerts(alerts: String): List<Alerts> {
        val listType = object : TypeToken<List<Alerts?>?>() {}.type
        return Gson().fromJson(alerts, listType)
    }

    @TypeConverter
    fun fromStringToCurrent(current: String): Current {

        return Gson().fromJson(current, Current::class.java)
    }

    @TypeConverter
    fun fromWeatherToString(weather: Weather): String {
        return Gson().toJson(weather)
    }

    @TypeConverter
    fun fromStringToWeather(weather:String): Weather {
        val listType = object : TypeToken<List<Weather?>?>() {}.type
        return Gson().fromJson(weather, listType)
    }
}