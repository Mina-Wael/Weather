package com.example.weatherforecast.ui.favorite

import android.view.View
import com.example.howsweather.model.Forecast

interface OnDeleteListener {
    fun onClick(v: View,position:Int,forecast: Forecast)
}