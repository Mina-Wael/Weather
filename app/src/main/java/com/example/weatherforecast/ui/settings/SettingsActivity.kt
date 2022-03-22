package com.example.weatherforecast.ui.settings

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivitySettingsBinding
import com.example.weatherforecast.util.Helper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import io.paperdb.Paper
import java.util.*


class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding
    lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var language = Paper.book().read("language", "en")
        var n2 = Paper.book().read<Int>("languageChangedRes", 0)
        var temp = Paper.book().read("temp", "metric")

        var comeFrom = Paper.book().read("location", "gps")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)




        if (n2 == 2) {
            changeLanguage(language!!)

        }
        when (temp) {
            "metric" -> binding.settingsRBCelsius.isChecked = true
            "kel" -> binding.settingsRBKelvin.isChecked = true
            "imperial" -> binding.settingsRBFahrenheit.isChecked = true
        }

        binding.settingsToolbar.setTitle(R.string.settings)
        setSupportActionBar(binding.settingsToolbar)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_icon)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.settingsToolbar.setNavigationOnClickListener {
            finish()
        }

        var mode = Paper.book().read<Int>("mode", 1)
        when (mode) {
            0 -> binding.settingsDark.isChecked = true
            1 -> binding.settingsLight.isChecked = true
        }
        when (language) {
            "en" -> binding.settingsLanguageEng.isChecked = true
            "ar" -> binding.settingsLanguageAr.isChecked = true
        }
        when (comeFrom) {
            "gps" -> binding.settingsRBGps.isChecked = true
            "map" -> binding.settingsRBMap.isChecked = true
        }





        binding.settingsModeRG.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            var m = -1
            when (i) {
                R.id.settingsDark -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    m = 0
                }
                R.id.settingsLight -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    m = 1
                }

            }
            Paper.book().write("mode", m)
        })

        binding.settingsLanguage.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.settingsLanguageAr -> {
                    Paper.book().write("language", "ar")
                    changeLanguage("ar")
                }
                R.id.settingsLanguageEng -> {
                    Paper.book().write("language", "en")
                    changeLanguage("en")
                }
            }
        })

        binding.settingsRGTemp.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener
        { radioGroup, i ->
            var temp: String? = null
            when (i) {
                R.id.settingsRBCelsius -> temp = "metric"
                R.id.settingsRBFahrenheit -> temp = "imperial"
                R.id.settingsRBKelvin -> temp = "kel"
            }
            if (temp != null)
                Paper.book().write("temp", temp!!)
            Paper.book().write("languageChanged", 1)

        })

        binding.settingsRGLocation.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.settingsRBGps -> {
                    getLocation()
                    Paper.book().write("comeFrom", "gps")
                    Paper.book().write("location", "gps")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                R.id.settingsRBMap -> {
                    Paper.book().write("comeFrom", "map")
                    Paper.book().write("location", "map")
                    Paper.book().write("settingMap", true)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            Paper.book().write("locationChanged", true)

        })

    }

    fun changeLanguage(language: String) {
        val myLocale = Locale(language)
        val res: Resources = resources
        val dm: DisplayMetrics = res.getDisplayMetrics()
        val conf: Configuration = res.getConfiguration()
        conf.locale = myLocale
        conf.setLayoutDirection(myLocale)
        res.updateConfiguration(conf, dm)
        var refresh = Intent(this, this::class.java)
        refresh.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(refresh)
        Paper.book().write("languageChangedRes", 1)
        Paper.book().write("languageChanged", 1)

    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        if (isLocationEnabled()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {

                        Paper.book().write("mapLat", location.latitude)
                        Paper.book().write("mapLng", location.longitude)
                    }
                }
        }
        else
            enableGps()

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
    fun enableGps() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

}