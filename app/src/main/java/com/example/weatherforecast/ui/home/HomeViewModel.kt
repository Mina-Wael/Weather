package com.example.weatherforecast.ui.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.*
import com.example.howsweather.model.Forecast
import com.example.weatherforecast.repository.Repository
import com.example.weatherforecast.util.Helper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.paperdb.Paper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(var context: Context) : ViewModel() {
    var repo: Repository
    private var _foreCastList = MutableLiveData<Forecast>()
    private var _networkState = MutableLiveData<Boolean>()
    var networkState: LiveData<Boolean> = _networkState
    private var _locationState = MutableLiveData<Location>()
    var locationState: LiveData<Location> = _locationState
    lateinit var fusedLocationClient: FusedLocationProviderClient




    init {
        repo = Repository.getInstance(context)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        Paper.init(context)
    }




    fun getData(lat: Double, lng: Double, lang: String?, unit: String?) {

        viewModelScope.launch(Dispatchers.IO) {
            if (Helper.check.hostAvailable()) {
                repo.getApiData(lat, lng, lang, unit)
                Paper.book().write("dataUpdated",true)
                setVal(true)
            } else {
                Log.i("hi", "getData: no internet")
                setVal(false)
            }
        }
    }

    private fun setVal(v: Boolean) {
        viewModelScope.launch {
            _networkState.value = v
        }

    }

    fun startListening(homeFragment: HomeFragment) {
        viewModelScope.launch {
            repo.getForecast().observe(homeFragment, Observer {

                _foreCastList.value = it
            })
        }

    }



    var forecast: LiveData<Forecast?> = _foreCastList












}