package com.example.weatherforecast.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.util.Helper
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PermissionViewModel : ViewModel() {

    private val _net = MutableLiveData<Boolean>()
    val net : LiveData<Boolean> = _net

    fun checkNetwork(){
        var value: Deferred<Boolean>
        viewModelScope.launch(Dispatchers.IO) {
            value=async{ Helper.check.hostAvailable() }
            setData(value.await())
        }
    }
    fun setData(d:Boolean)
    {
        viewModelScope.launch {
            _net.value=d
        }
    }

}