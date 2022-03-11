package com.example.howsweather.model

import com.example.weatherforecast.model.Feels_like
import com.example.weatherforecast.model.Temp

class Daily {

    constructor()
    constructor(
        dt: Long, sunrise: Long, sunset: Long, moonrise: Long, moonset: Long, moon_phase: Float,
        pressure: Int, humidity: Int, dew_point: Float, wind_speed: Float, wind_deg: Int,
        clouds: Int, pop: Float, rain: Float, uvi: Float,
        feels_like: Feels_like, temp: Temp, weather: List<Weather>) {
        this.dt = dt
        this.sunrise = sunrise
        this.sunset = sunset
        this.moonrise = moonrise
        this.moonset = moonset
        this.moon_phase = moon_phase
        this.pressure = pressure
        this.humidity = humidity
        this.dew_point = dew_point
        this.wind_speed = wind_speed
        this.wind_deg = wind_deg
        this.clouds = clouds
        this.pop = pop
        this.rain = rain
        this.uvi = uvi
        this.feels_like = feels_like
        this.temp = temp
        this.weather = weather
    }
    var dt:Long = 0
        get() = field
        set(value) {field = value}
    var sunrise:Long = 0
        get() = field
        set(value) {field = value}
    var sunset:Long = 0
        get() = field
        set(value) {field = value}
    var moonrise:Long = 0
        get() = field
        set(value) {field = value}
    var moonset:Long = 0
        get() = field
        set(value) {field = value}
    var moon_phase:Float = 0f
        get() = field
        set(value) {field = value}

    var pressure:Int = 0
        get() = field
        set(value) {field = value}
    var humidity:Int = 0
        get() = field
        set(value) {field = value}
    var dew_point:Float = 0f
        get() = field
        set(value) {field = value}
    var wind_speed:Float = 0f
        get() = field
        set(value) {field = value}
    var wind_deg:Int = 0
        get() = field
        set(value) {field = value}
    var clouds:Int = 0
        get() = field
        set(value) {field = value}
    var pop:Float = 0f
        get() = field
        set(value) {field = value}
    var rain:Float = 0f
        get() = field
        set(value) {field = value}
    var uvi:Float = 0f
        get() = field
        set(value) {field = value}

    var feels_like:Feels_like = Feels_like()
        get() = field
        set(value) {field = value}

    var temp:Temp = Temp()
        get() = field
        set(value) {field = value}

    var weather:List<Weather> = emptyList()
    get() = field
    set(value) {field = value}

}