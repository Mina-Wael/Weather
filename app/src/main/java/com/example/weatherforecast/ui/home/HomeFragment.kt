package com.example.weatherforecast.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howsweather.model.Daily

import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.model.Hourly
import com.example.weatherforecast.util.showSnackbar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import io.paperdb.Paper
import java.util.ArrayList
import java.util.Observer
import kotlin.reflect.KProperty

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val PERMISSION_ID = 1
    lateinit var homeViewModel: HomeViewModel

    lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var hourlyList: List<Hourly>
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    lateinit var dailyList: List<Daily>
    lateinit var dailyAdapter: DailyAdapter
    lateinit var linearLayoutManager2: LinearLayoutManager
    private lateinit var location: Location

    private lateinit var navController: NavController
    lateinit var locationManager: LocationManager
    lateinit var  temp:String
    lateinit var language:String



    private val binding get() = _binding!!
    var lat: Double = 0.0
    var lng: Double = 0.0

    private var networkState: Boolean = false


    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Paper.init(requireContext())

         temp = Paper.book().read("temp", "metric")!!
         language = Paper.book().read("language", "en")!!

        var homeViewModelFactory = HomeViewModelFactory(requireActivity())
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.homeNoInternet.visibility = View.GONE
        hideGpsCard()
        hideFields()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        if (!Paper.book().read<Boolean>("dataUpdated", false)!!) {
            if (Paper.book().read<Boolean>("map", false)!!) {

                var lat=Paper.book().read<Double>("mapLat")
                var lng=Paper.book().read<Double>("mapLng")
                if (lat!=null&&lng!=null) {
                    homeViewModel.getData( lat, lng,language,temp
                    )
                }

            } else if (Paper.book().read("source", "sds")!!.equals("gps")) {
                if (!isLocationEnabled()) {
                    hideFields()
                    showGpsCard()

                } else {
                    hideGpsCard()

                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location: Location? ->
                            if (location != null){
                                homeViewModel.getData(
                                    location.latitude,
                                    location.longitude,
                                    language,
                                    temp
                                )
                                Paper.book().write("mapLat",location.latitude)
                                Paper.book().write("mapLng",location.longitude)
                            }
                            else
                                Toast.makeText(requireContext(), "nullllll", Toast.LENGTH_SHORT).show()
                        }

                }

            }
            else{
                var lat=Paper.book().read<Double>("mapLat")
                var lng=Paper.book().read<Double>("mapLng")

                if (lat!=null&&lng!=null) {
                    homeViewModel.getData( lat, lng,language,temp )
                }
            }
        }


        var tempValue: String = ""
        if (language.equals("en")) {
            when (temp) {
                "metric" -> tempValue = "ْ c"
                "imperial" -> tempValue = "ْ f"
                "kel" -> tempValue = "kel"
            }
        } else
            when (temp) {
                "metric" -> tempValue = "سليزيس"
                "imperial" -> tempValue = "فيهرنهايت"
                "kel" -> tempValue = "كيلفن"
            }


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
                showFields()
                binding.homeProgress.visibility=View.GONE
                binding.homeCard2.visibility = View.VISIBLE
                binding.homeCard1TvTempMeas.setText(tempValue)
                hourlyAdapter.setList(it!!.hourly)
                dailyAdapter.setList(it.daily)
                binding.homeCard1TempText.text = it.current.weather.get(0).description
                binding.homeCard1TvTempNum.text = it.current.temp.toString()
                binding.homeCard1TvTempMeas.visibility = View.VISIBLE
                binding.homeTvTimeZone.text = it.timezone
                binding.homeCard2PressTv1.setText(it.current.pressure.toString() + "hps")
                binding.homeCard2HumTv1.setText(it.current.humidity.toString() + "%")
                binding.homeCard2cloudsTv1.setText(it.current.clouds.toString() + "%")
                binding.homeCard2visTv1.setText(it.current.visibility.toString() + "m")
                binding.homeCard2WindTv1.setText(it.current.wind_speed.toString() + "m/s")
                binding.homeCard2uviTv1.setText(it.current.uvi.toString())
                Log.i("TAG", "onCreateView: get data from database " + it!!.timezone)
            } else
                if (!networkState) {
                    hideFields()
                }
        }

        homeViewModel.locationState.observe(requireActivity(), androidx.lifecycle.Observer {
            if (it != null)
                Toast.makeText(requireContext(), "from frag " + it.latitude, Toast.LENGTH_SHORT)
                    .show()
        }

        )

        return root
    }

    private fun hideFields() {

//        hourlyAdapter.setList(hourlyList)
//        dailyAdapter.setList(dailyList)
//        binding.homeCard1TempText.text = ""
//        binding.homeCard1TvTempNum.text = ""
        binding.homeCard1TvTempMeas.visibility = View.GONE
        binding.homeTvTimeZone.visibility = View.GONE
        binding.homeCard2.visibility = View.GONE
        binding.homeCard1.visibility = View.GONE
        binding.homeRv.visibility = View.GONE
        binding.homeRv2.visibility = View.GONE
        binding.homeProgress.visibility=View.GONE


    }

    private fun showFields() {
        binding.homeNoInternet.visibility = View.GONE
        binding.homeCard1TvTempMeas.visibility = View.VISIBLE
        binding.homeTvTimeZone.visibility = View.VISIBLE
        binding.homeCard2.visibility = View.VISIBLE
        binding.homeCard1.visibility = View.VISIBLE
        binding.homeRv.visibility = View.VISIBLE
        binding.homeRv2.visibility = View.VISIBLE
        binding.homeProgress.visibility=View.GONE
        hideGpsCard()
    }

    fun hideGpsCard() {
        binding.homeGpsCard.visibility = View.GONE
        binding.homeProgress.visibility = View.GONE
    }

    fun showGpsCard() {
        binding.homeGpsCard.visibility = View.VISIBLE
        binding.homeProgress.visibility = View.GONE
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        var location: Location

        lat = 31.55654
        lng = 32.65659
        var lang = Paper.book().read("language", "en")
        var temp = Paper.book().read("temp", "metric")


        binding.homeGpsBtn.setOnClickListener(View.OnClickListener {
            enableGps()
        })

        homeViewModel.locationState.observe(requireActivity(), androidx.lifecycle.Observer{
            if (it != null)
                homeViewModel.getData(it.latitude, it.longitude, lang, temp)

        })




//        getLocation()
        homeViewModel.startListening(this)

        homeViewModel.networkState.observe(requireActivity(), androidx.lifecycle.Observer{
            networkState = it
            if (!it) {
                binding.root.showSnackbar("No Internet", Snackbar.LENGTH_SHORT)
                binding.homeNoInternet.visibility = View.VISIBLE
            }

        })

    }

    @SuppressLint("MissingPermission")
    override fun onResume() {

        if(Paper.book().read("locationChanged",false)!!)
        {
            var lang = Paper.book().read("language", "en")
            var temp = Paper.book().read("temp", "metric")
            var lat=Paper.book().read<Double>("mapLat")
            var lng=Paper.book().read<Double>("mapLng")
            homeViewModel.getData(lat!!,lng!!,lang,temp)
            Paper.book().write("locationChanged",false)
        }

        if (Paper.book().read("source", "dk")!!.equals("gps")) {

            if (!isLocationEnabled()) {
                hideFields()
                showGpsCard()

            } else {
                hideGpsCard()


                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null){
                            homeViewModel.getData(
                                location.latitude,
                                location.longitude,
                                language,
                                temp
                            )
                            Paper.book().write("mapLat",location.latitude)
                            Paper.book().write("mapLng",location.longitude)
                            Paper.book().write("source","sda")
                        }
                        else
                            Toast.makeText(requireContext(), "nullllll", Toast.LENGTH_SHORT).show()
                    }

            }

        }

            super.onResume()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility == View.GONE)
            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility =
                View.VISIBLE
//        requireActivity().findViewById<Toolbar>(R.id.mainToolbar).visibility=View.VISIBLE
//        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility=View.VISIBLE

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }


    fun enableGps() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }



    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }



}


