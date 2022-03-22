package com.example.weatherforecast.ui.alarm

import java.util.*

class AlarmModel {
     private lateinit var calenderFrom:Calendar
    fun setCalenderFRom(calenderFrom: Calendar)
    {
        this.calenderFrom=calenderFrom
    }
    fun getCalenderFRom():Calendar
    {
        return calenderFrom
    }
    fun getCalenderTO():Calendar
    {
        return calenderTo
    }

   private lateinit var calenderTo:Calendar

    fun setCalenderTO(calenderTo: Calendar)
    {
        this.calenderTo=calenderTo
    }



    constructor(calenderFrom: Calendar, calenderTo: Calendar) {
        this.calenderFrom = calenderFrom
        this.calenderTo = calenderTo
    }

    constructor()

}