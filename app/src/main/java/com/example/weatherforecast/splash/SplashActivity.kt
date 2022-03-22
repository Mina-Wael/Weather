package com.example.weatherforecast.splash

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.ActivitySplashBinding
import io.paperdb.Paper

class SplashActivity : AppCompatActivity() {
  lateinit  var binding:ActivitySplashBinding
  lateinit var onRequestResult: OnRequestResult
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onRequestResult=PermissionFragment()
        Paper.init(this)


        var fm=supportFragmentManager
        var ft=fm.beginTransaction()
        ft.add(R.id.splashContainer,SplashFragment())
        ft.commit()

                         Handler().postDelayed( Runnable() {
              run() {
                  if(Paper.book().read<Boolean>("first",true)!!)
                  {
                  var fm2=supportFragmentManager
                  if(!fm2.isDestroyed){
                  var ft2=fm2.beginTransaction()
                  ft2.replace(R.id.splashContainer,PermissionFragment())
                  ft2.commit()}
                  }
                  else
                  {
                      startActivity(Intent(this,MainActivity::class.java))
                      finish()
                  }

            }
        },  1000);
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 4) {
            if ( grantResults[0] ==
                PackageManager.PERMISSION_GRANTED) {
                // you can get location here after checking Gps status
                Log.i("mina", "onRequestPermissionsResult: ok")
            }
            else
                finish()
        }
    }
}