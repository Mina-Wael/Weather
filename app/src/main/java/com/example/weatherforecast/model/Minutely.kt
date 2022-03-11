package com.example.weatherforecast.model

class Minutely {
    var dt: Long = 0
        set(value) {
            field = value
        }
        get() = field
    var precipitation: Double = 0.0
        set(value) {
            field = value
        }
        get() = field

}