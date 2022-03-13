package com.example.howsweather.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.weatherforecast.model.Hourly
import com.example.weatherforecast.model.Minutely
@Entity(tableName = "forecast")
class Forecast {
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
        get() = field
    var favorite:Int=0
    get() = field

    fun setFavo(fav:Int)
    {
        this.favorite=fav
    }


    @JvmOverloads constructor(
        lat: Double, lon: Double,  timezone: String, timezone_offset: Int,
        current: Current, minutely: List<Minutely>, hourly: List<Hourly>,
        daily: List<Daily>, alerts: List<Alerts>
    ) {
        this.lat = lat
        this.lon = lon
        this.timezone = timezone
        this.timezone_offset = timezone_offset
        this.current = current
        this.minutely = minutely
        this.hourly = hourly
        this.daily = daily
        this.alerts = alerts
    }

    constructor()
    var lat: Double = 0.0
        get() = field

    fun setLatt(lat1:Double)
    {
        this.lat=lat1
    }
//        set(value) {
//            field = value
//        }

    var lon: Double = 0.0
        get() = field
        set(value) {
            field = value
        }

    var timezone: String = ""
        get() = field
        set(value) {
            field = value
        }
    var timezone_offset: Int = 0
        get() = field
        set(value) {
            field = value
        }

    var current: Current = Current()
        get() = field
        set(value) {
            field = value
        }

    var minutely: List<Minutely> = emptyList()
        get() = field
        set(value) {
            field = value
        }

    var hourly: List<Hourly> = emptyList()
        get() = field
        set(value) {
            field = value
        }

    var daily: List<Daily> = emptyList()
        get() = field
        set(value) {
            field = value
        }

    var alerts: List<Alerts> = emptyList()
        get() = field
        set(value) {
            field = value
        }


}