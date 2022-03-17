package com.example.weatherforecast.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howsweather.model.Daily
import com.example.weatherforecast.R

import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.model.Hourly
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.io.IOException

import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val PERMISSION_ID = 1
    lateinit var homeViewModel: HomeViewModel

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    lateinit var hourlyList: List<Hourly>
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    lateinit var dailyList: List<Daily>
    lateinit var dailyAdapter: DailyAdapter
    lateinit var linearLayoutManager2: LinearLayoutManager


    private val binding get() = _binding!!
    var lat: Double = 0.0
    var lng: Double = 0.0


    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        var homeViewModelFactory = HomeViewModelFactory(requireActivity())
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        hourlyList = ArrayList()
        hourlyAdapter = HourlyAdapter(requireActivity(), hourlyList)
        linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.homeRv.layoutManager = linearLayoutManager
        binding.homeRv.adapter = hourlyAdapter
        binding.homeRv.setHasFixedSize(true)

        dailyList = ArrayList()
        dailyAdapter = DailyAdapter(requireActivity(), dailyList)
        linearLayoutManager2 = LinearLayoutManager(activity)
        binding.homeRv2.layoutManager = linearLayoutManager2
        binding.homeRv2.adapter = dailyAdapter
        binding.homeRv2.setHasFixedSize(true)
        binding.homeCard1TvTempMeas.visibility = View.GONE


        homeViewModel.forecast.observe(viewLifecycleOwner) {
            if (it != null) {
                hourlyAdapter.setList(it!!.hourly)
                dailyAdapter.setList(it.daily)
                binding.homeCard1TempText.text = it.current.weather.get(0).description
                binding.homeCard1TvTempNum.text = it.current.temp.toString()
                binding.homeCard1TvTempMeas.visibility = View.VISIBLE
                binding.homeTvTimeZone.text = it.timezone
                binding.homeCard2PressTv1.setText(it.current.pressure.toString()+"hps")
                binding.homeCard2HumTv1.setText(it.current.humidity.toString()+"%")
                binding.homeCard2cloudsTv1.setText(it.current.clouds.toString()+"%")
                binding.homeCard2visTv1.setText(it.current.visibility.toString()+"m")
                binding.homeCard2WindTv1.setText(it.current.wind_speed.toString()+"m/s")
                binding.homeCard2uviTv1.setText(it.current.uvi.toString())

                Log.i("TAG", "onCreateView: get data from database " + it!!.timezone)
            } else
                clearData()

        }

        return root
    }

    private fun clearData() {
        hourlyAdapter.setList(hourlyList)
        dailyAdapter.setList(dailyList)
        binding.homeCard1TempText.text =""
        binding.homeCard1TvTempNum.text =""
        binding.homeCard1TvTempMeas.visibility = View.GONE
        binding.homeTvTimeZone.text = ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var location: Location
        lat = 31.55654
        lng = 32.65659


        binding.homeCard1.setOnClickListener {
            this.lat = this.lat + 1.5
            this.lng = this.lng + 2.5
            homeViewModel.getData(lat!!, lng!!)
        }


//        getLocation()
        homeViewModel.startListening(this)


////        homeViewModel.getData(lat!!, lng!!)
//        if (checkPer())
//            if (locationEnabled()) {
//
////                location = getLocation()!!
////                lat = location.latitude
////                lng = location.longitude
////                if (lat != null && lng != null)
////                    homeViewModel.getData(lat!!, lng!!)
//            } else {
//                Toast.makeText(activity, " gps not enable", Toast.LENGTH_SHORT).show()
//                enableGps()
//            }
//        else {
//            requestPer()
//            Toast.makeText(activity, " per not enable", Toast.LENGTH_SHORT).show()
//        }
//        getLocation()


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility==View.GONE)
            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility= View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun checkPer(): Boolean // check if location permession is granted
    {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        )
            return true
        else
            return false
    }

    fun enableGps() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    fun requestPer() //ask user for location permession
    {

        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    private fun locationEnabled(): Boolean {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        var location: Location
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (locationEnabled()) {

                } else
                    enableGps()
            }

        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        var location: Location? = null
        val locationRequest = LocationRequest.create()
        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                lat = locationResult.lastLocation.latitude
                lng = locationResult.lastLocation.longitude
                location = locationResult.lastLocation
                Toast.makeText(requireActivity(), "lat" + lat + " lng " + lng, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.numUpdates = 1
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient?.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()!!
        )
        return location
    }

}