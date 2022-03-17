package com.example.weatherforecast.ui.favorite

import com.google.android.gms.maps.model.LatLng

interface OnItemClick {
    fun onClick(id:Int,latLng: LatLng)
}