package com.example.weatherforecast.ui.settings

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivitySettingsBinding
import io.paperdb.Paper
import java.util.*


class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.settingsToolbar.setTitle(R.string.settings)
        setSupportActionBar(binding.settingsToolbar)

        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back_icon)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.settingsToolbar.setNavigationOnClickListener {
            Paper.book().write("languageChanged",1)
            finish()
        }

        var mode= Paper.book().read<Int>("mode",1)
        when(mode)
        {
            0-> binding.settingsDark.isChecked=true
            1-> binding.settingsLight.isChecked=true
        }
        var language= Paper.book().read("language","en")
        when(language)
        {
            "en"-> binding.settingsLanguageEng.isChecked=true
            "ar"-> binding.settingsLanguageAr.isChecked=true
        }


        binding.settingsModeRG.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener {
                radioGroup, i ->
            var m=-1
            when(i)
            {
                R.id.settingsDark->
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    m=0
                }
                R.id.settingsLight->{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    m=1
                }

            }
            Paper.book().write("mode",m)
        })

        binding.settingsLanguage.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener {
                radioGroup, i ->
            when(i)
            {
                R.id.settingsLanguageAr->{
                    Paper.book().write("language","ar")
                    changeLanguage("ar")
                }
                R.id.settingsLanguageEng->{
                    Paper.book().write("language","en")
                    changeLanguage("en")
                }
            }
        })

    }

    fun changeLanguage(language:String)
    {
        val myLocale = Locale(language)
        val res: Resources = resources
        val dm: DisplayMetrics = res.getDisplayMetrics()
        val conf: Configuration = res.getConfiguration()
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
        var refresh= Intent(this, this:: class.java)
        refresh.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(refresh)

    }

    override fun onBackPressed() {
           Paper.book().write("languageChanged",1)
        finish()

    }
}