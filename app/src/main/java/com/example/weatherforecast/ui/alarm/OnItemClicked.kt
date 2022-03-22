package com.example.weatherforecast.ui.alarm

import android.view.View

interface OnItemClicked {
    fun onItemClick()
    fun OnDelete(position:Int,view: View)
}