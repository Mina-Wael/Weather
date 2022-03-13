package com.example.weatherforecast.ui.home

import java.text.SimpleDateFormat
import java.util.*

fun main()
{
    var c: Calendar = Calendar.getInstance()

//    var l:TimeUnit
//    val hours = TimeUnit.MILLISECONDS.toHours(1618315200).toInt()
//
    var l=1618315200
    val df: Date = Date(1646937378210)
    val vv: String = SimpleDateFormat("MM dd, yyyy hh:mma").format(df)
    val cal = Calendar.getInstance()
    cal.timeInMillis = 1618315200 * 1000
    val hr = cal.time.hours
    var hour:String
    if (hr-12>0) {
        hour=((hr-12).toString()+" pm")

    } else {
        hour=((-(hr-12)).toString()+" am")
    }
    var date:Date= Date()
    cal.timeInMillis=1647252000*1000
    var dayOfWeek:Int=cal.time.day
    var weekDay:String
    if (Calendar.MONDAY == dayOfWeek) weekDay = "monday"
    else if (Calendar.TUESDAY == dayOfWeek) weekDay = "tuesday"
    else if (Calendar.WEDNESDAY == dayOfWeek) weekDay = "wednesday"
    else if (Calendar.THURSDAY == dayOfWeek) weekDay = "thursday"
    else if (Calendar.FRIDAY == dayOfWeek) weekDay = "friday"
    else if (Calendar.SATURDAY == dayOfWeek) weekDay = "saturday"
    else if (Calendar.SUNDAY == dayOfWeek) weekDay = "sunday"
    else weekDay="Wrong"

    println(Calendar.SATURDAY)
//    l.convert(1618315200,)
//    1646937378210
//    1618315200
}