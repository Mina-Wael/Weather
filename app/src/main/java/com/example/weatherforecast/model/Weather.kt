package com.example.howsweather.model

class Weather {
    var id: Int = 0
        get() = field
        set(value) {
            field = value
        }

    constructor(id: Int, main: String, description: String, icon: String) {
        this.id = id
        this.main = main
        this.description = description
        this.icon = icon
    }

    constructor()

    var main: String = ""
        get() = field
        set(value) {
            field = value
        }

    var description: String = ""
        get() = field
        set(value) {
            field = value
        }

    var icon: String = ""
        get() = field
        set(value) {
            field = value
        }
}