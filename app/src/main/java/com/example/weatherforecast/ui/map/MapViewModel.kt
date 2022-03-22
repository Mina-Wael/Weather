package com.example.weatherforecast.ui.map

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.repository.Repository
import com.google.android.gms.maps.model.LatLng
import io.paperdb.Paper
import kotlinx.coroutines.launch

class MapViewModel(var context: Context) :ViewModel() {
    var repo: Repository
    var language:String
    var temp:String
    init {
        Paper.init(context)
        repo = Repository.getInstance(context)
         language= Paper.book().read("language","en")!!
        temp= Paper.book().read("temp","metric")!!
    }

    fun insertData(latLng: LatLng)
    {
        viewModelScope.launch {
            repo.insertFavorite(latLng,language,temp)
        }

    }
}