package com.example.weatherforecast

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.slidingpanelayout.widget.SlidingPaneLayout.LOCK_MODE_LOCKED
import com.example.weatherforecast.databinding.ActivityMainBinding
import com.example.weatherforecast.ui.settings.SettingsActivity
import com.google.android.material.navigation.NavigationView
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity(), OnDrawerListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    lateinit var navController: NavController
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var toggle: ActionBarDrawerToggle

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        setSupportActionBar(binding.mainToolbar)
        binding.mainNavView.bringToFront()
        navController = findNavController(R.id.nav_host_fragment_activity_main)


        Paper.init(applicationContext)
        var mode = Paper.book().read("mode", 1)
        when (mode) {
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            1 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        var language = Paper.book().read("language", "en")

        when (language) {
            "en" -> onRestoreState("en")
            "ar" -> onRestoreState("ar")
        }


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
                R.id.navigation_home, R.id.navigation_favorite, R.id.navigation_notifications
            )
        ).setDrawerLayout(binding.mainDrawer).build()
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        binding.mainNavView.setupWithNavController(navController)
        binding.mainNavView.setNavigationItemSelectedListener(this)


    }

    override fun onRestart() {
        refresh()
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
            R.id.drawerAboutUs -> navController.navigate(R.id.aboutUs)

        }
        binding.mainDrawer.closeDrawer(GravityCompat.START)
        return true
    }


    private fun onRestoreState(language: String) {
        val myLocale = Locale(language)
        val res: Resources = resources
        val dm: DisplayMetrics = res.getDisplayMetrics()
        val conf: Configuration = res.getConfiguration()
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
    }

    fun refresh() {
        var n = Paper.book().read<Int>("languageChanged", 0)
        Log.i("TAG1", "refresh: ")
        if (n == 1) {
            Paper.book().write("languageChanged", 0)
            var refresh = Intent(this, this::class.java)
            startActivity(refresh)
            finish()
        }
    }


}