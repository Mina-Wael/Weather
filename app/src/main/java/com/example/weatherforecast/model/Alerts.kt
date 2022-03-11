package com.example.howsweather.model

class Alerts {


    constructor()
    constructor(sender_name: String, event: String, start: Int, end: Int,
                description: String, tags: List<String>) {
        this.sender_name = sender_name
        this.event = event
        this.start = start
        this.end = end
        this.description = description
        this.tags = tags
    }

    var sender_name: String = ""
        get() = field
        set(value) {
            field = value
        }
    var event: String = ""
        get() = field
        set(value) {
            field = value
        }
    var start: Int = 0
        get() = field
        set(value) {
            field = value
        }


    var end: Int = 0
        get() = field
        set(value) {
            field = value
        }
    var description: String = ""
        get() = field
        set(value) {
            field = value
        }
    var tags: List<String> = emptyList()
        get() = field
        set(value) {
            field = value
        }
}