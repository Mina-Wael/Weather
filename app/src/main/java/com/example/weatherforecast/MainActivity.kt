package com.example.weatherforecast

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
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.ui.settings.SettingsActivity
import com.google.android.gms.location.*
import com.google.android.material.navigation.NavigationView
import io.paperdb.Paper
import java.util.*

class MainActivity : AppCompatActivity(), OnDrawerListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var toggle: ActionBarDrawerToggle
     var language:String?=null

    lateinit var locationManager: LocationManager

    lateinit var fusedLocationClient:FusedLocationProviderClient

    lateinit var location: Location


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationManager=getSystemService(LOCATION_SERVICE) as LocationManager

       Paper.init(applicationContext)

         language = Paper.book().read("language", "en")

        val myLocale = Locale(Paper.book().read("language", "en"))
        val res: Resources = resources
        val dm: DisplayMetrics = res.getDisplayMetrics()
        val conf: Configuration = res.getConfiguration()
        conf.locale = myLocale
        conf.setLayoutDirection(myLocale)
        res.updateConfiguration(conf, dm)
        if (Paper.book().read<Int>("languageChangedRes", 0)==1)
        {
            Paper.book().write("languageChangedRes", 0)
            refresh()
        }

        var mode = Paper.book().read("mode", 1)
        when (mode) {
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val navView: BottomNavigationView = binding.navView
        setSupportActionBar(binding.mainToolbar)
        binding.mainNavView.bringToFront()
        navController = findNavController(R.id.nav_host_fragment_activity_main)

        if (Paper.book().read<Boolean>("map",false)!!){
            navController.navigate(R.id.mapsFragment)}
       if(Paper.book().read("settingMap",false)!!)
           navController.navigate(R.id.mapsFragment)


        toggle = ActionBarDrawerToggle(
            this, binding.mainDrawer, binding.mainToolbar, R.string.openDrawer,
            R.string.closeDrawer
        )
        toggle.isDrawerIndicatorEnabled = true
        binding.mainDrawer.setDrawerListener(toggle)
        binding.mainDrawer.addDrawerListener(toggle)
        toggle.syncState()
//         Passing each menu ID as a set of Ids because each
//         menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration.Builder(
            setOf(
                R.id.Home, R.id.navigation_favorite, R.id.Alarm
            )
        ).setDrawerLayout(binding.mainDrawer).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        binding.mainNavView.setupWithNavController(navController)
        binding.mainNavView.setNavigationItemSelectedListener(this)

    }

    override fun onRestart() {
        var n = Paper.book().read<Int>("languageChanged", 0)
        Log.i("TAG1", "refresh: ")
        if (n == 1) {
            refresh()
            Paper.book().write("languageChanged", 0)
        }

        super.onRestart()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    @SuppressLint("RestrictedApi")
    override fun disableDrawer() {
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_icon)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    @SuppressLint("RestrictedApi")
    override fun enableDrawer() {
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.drawer_icon)
        binding.mainToolbar.setNavigationOnClickListener {
            binding.mainDrawer.openDrawer(
                GravityCompat.START
            )
        }
    }

    override fun onBackPressed() {

        if (binding.mainDrawer.isDrawerOpen(GravityCompat.START))
            binding.mainDrawer.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.drawerSettings -> {
                var intent = Intent(this, SettingsActivity::class.java)
                startActivityForResult(intent, 5)
            }
            R.id.drawerAboutUs -> navController.navigate(R.id.AboutUs)
        }
        binding.mainDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun refresh() {
            var refresh = Intent(this, this::class.java)
            startActivity(refresh)
            finish()
    }

    override fun onStop() {
        Paper.book().write("map", false)
        Paper.book().write("gps", false)
        Paper.book().write("settingMap", false)
        Paper.book().write("dataUpdated",false)
        Paper.book().write("comeFrom","false")
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        Paper.book().write("Started",false)
    }

}