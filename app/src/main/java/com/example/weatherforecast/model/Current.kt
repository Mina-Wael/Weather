package com.example.howsweather.model

class Current {

    constructor(
        dt: Long, sunrise: Long, sunset: Long, temp: Float, feels_like: Float, pressure: Int,
        humidity: Int, dew_point: Float, uvi: Float,
        clouds: Int, visibility: Int, wind_speed: Double, wind_deg: Int, weather: List<Weather>
    ) {
        this.dt = dt
        this.sunrise = sunrise
        this.sunset = sunset
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
        this.weather = weather
    }

    constructor()

    var dt: Long = 0
        get() = field
        set(value) {
            field = value
        }
    var sunrise: Long = 0
        get() = field
        set(value) {
            field = value
        }
    var sunset: Long = 0
        get() = field
        set(value) {
            field = value
        }
    var temp: Float = 0f
        get() = field
        set(value) {
            field = value
        }
    var feels_like: Float = 0f
        get() = field
        set(value) {
            field = value
        }
    var pressure: Int = 0
        get() = field
        set(value) {
            field = value
        }
    var humidity: Int = 0
        get() = field
        set(value) {
            field = value
        }
    var dew_point: Float = 0f
        get() = field
        set(value) {
            field = value
        }
    var uvi: Float = 0f
        get() = field
        set(value) {
            field = value
        }
    var clouds: Int = 0
        get() = field
        set(value) {
            field = value
        }
    var visibility: Int = 0
        get() = field
        set(value) {
            field = value
        }
    var wind_speed: Double = 0.0
        get() = field
        set(value) {
            field = value
        }
    var wind_deg: Int = 0
        get() = field
        set(value) {
            field = value
        }


    var weather: List<Weather> = emptyList()
        get() = field
        set(value) {
            field = value
        }
}