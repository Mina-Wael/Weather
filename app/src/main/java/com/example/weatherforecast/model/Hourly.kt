package com.example.weatherforecast.model

import com.example.howsweather.model.Weather

class Hourly {



    constructor(
        dt: Long,
        temp: Double,
        feels_like: Double,
        pressure: Int,
        humidity: Int,
        dew_point: Double,
        uvi: Double,
        clouds: Int,
        visibility: Int,
        wind_speed: Double,
        wind_deg: Int,
        wind_gust: Double,
        pop: Double,
        weather: List<Weather>
    ) {
        this.dt = dt
        this.temp = temp
        this.feels_like = feels_like
        this.pressure = pressure
        this.humidity = humidity
        this.dew_point = dew_point
        this.uvi = uvi
        this.clouds = clouds
        this.visibility = visibility
        this.wind_speed = wind_speed
        this.wind_deg = wind_deg
        this.wind_gust = wind_gust
        this.pop = pop
        this.weather = weather
    }

    constructor()

    var dt: Long = 0
        set(value) {
            field = value
        }
        get() = field
    var temp: Double = 0.0
        set(value) {
            field = value
        }
        get() = field
    var feels_like: Double = 0.0
        set(value) {
            field = value
        }
        get() = field
    var pressure: Int = 0
        set(value) {
            field = value
        }
        get() = field
    var humidity: Int = 0
        set(value) {
            field = value
        }
        get() = field
    var dew_point: Double = 0.0
        set(value) {
            field = value
        }
        get() = field
    var uvi: Double = 0.0
        set(value) {
            field = value
        }
        get() = field
    var clouds: Int = 0
        set(value) {
            field = value
        }
        get() = field
    var visibility: Int = 0
        set(value) {
            field = value
        }
        get() = field
    var wind_speed: Double = 0.0
        set(value) {
            field = value
        }
        get() = field
    var wind_deg: Int = 0
        set(value) {
            field = value
        }
        get() = field
    var wind_gust: Double = 0.0
        set(value) {
            field = value
        }
        get() = field
    var pop:Double=0.0

     var weather:List<Weather> = emptyList()
         set(value) {
             field = value
         }
         get() = field


}