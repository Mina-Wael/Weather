package com.example.weatherforecast.util



import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Message
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import java.io.IOException
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Socket
import java.net.URL


class Helper {

    object check{
        @JvmStatic
        fun isNetworkAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetworkInfo: NetworkInfo? = null
            activeNetworkInfo = cm.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
        }


        fun hostAvailable(): Boolean {

            try {
                Socket().use { socket ->
                    socket.connect(InetSocketAddress("www.google.com",80), 2000)
                    return true
                }
            } catch (e: IOException) {
                // Either we have a timeout or unreachable host or failed DNS lookup
                println(e)
                return false
            }

        }


        fun requestPer(context: Context) //ask user for location permission
        {

            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                4
            )
        }

        fun checkLocationPermission(context: Context):Boolean=
            ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED


    }




}



fun View.showSnackbar(snackbarText: String, timeLength: Int) {
    Snackbar.make(this, snackbarText, timeLength).run {
        show()
    }
}


fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: LiveData<Boolean>,
    timeLength: Int
) {

    snackbarEvent.observe(lifecycleOwner, Observer { event ->

        showSnackbar("No Internet", timeLength)

    })

//    fun Fragment.setupRefreshLayout(
//        refreshLayout: ScrollChildSwipeRefreshLayout,
//        scrollUpChild: View? = null
//    ) {
//        refreshLayout.setColorSchemeColors(
//            ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
//            ContextCompat.getColor(requireActivity(), R.color.colorAccent),
//            ContextCompat.getColor(requireActivity(), R.color.colorPrimaryDark)
//        )
//        // Set the scrolling view in the custom SwipeRefreshLayout.
//        scrollUpChild?.let {
//            refreshLayout.scrollUpChild = it
//        }
//    }
//






}