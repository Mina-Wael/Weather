package com.example.weatherforecast.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howsweather.model.Daily
import com.example.howsweather.model.Forecast
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.model.Hourly
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import io.paperdb.Paper


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var homeViewModel: HomeViewModel

    lateinit var hourlyList: List<Hourly>
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    lateinit var dailyList: List<Daily>
    lateinit var dailyAdapter: DailyAdapter
    lateinit var linearLayoutManager2: LinearLayoutManager

    private lateinit var navController: NavController
    lateinit var tempUnit: String
    lateinit var language: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Paper.init(requireContext())
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        var homeViewModelFactory = HomeViewModelFactory(requireActivity())
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        initSettingsData()
        initHourlyRecycleView()
        initDailyRecycleView()

        binding.homeGpsBtn.setOnClickListener {
            requestGps()
        }
        binding.homeSwipeRefresh.setOnRefreshListener {
            onSwipeRefresh()
        }

        if (!Paper.book().read<Boolean>("firstTime", true)!!)
            startLoadingData()

        return binding.root
    }

    private fun onSwipeRefresh() {
        if (Paper.book().read<Boolean>("firstTime", true)!!)
            runFirstTime()
        else {
            homeViewModel.checkNetwork()
            startObserveToNetworkState()
            Toast.makeText(requireActivity(), "Updated", Toast.LENGTH_SHORT).show()
        }
        binding.homeSwipeRefresh.isRefreshing = false

    }

    private fun runFirstTime() {
        if (homeViewModel.checkLocationPermission()) {
            if (homeViewModel.checkGpsState()) {
                startLoadingData()
                showAndHideGpsCard(View.GONE)
            } else {
                showAndHideGpsCard(View.VISIBLE)
                showAndHideFields(View.GONE)
            }
        } else {
            showAndHideFields(View.GONE)
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onResume() {
        super.onResume()
        if (Paper.book().read<Boolean>("firstTime", true)!!)
            runFirstTime()

    }

    private fun startLoadingData() {
        if (!(Paper.book().read("Started", false)!!)) {
            Log.i("mina", "not started: ")
            homeViewModel.checkNetwork()
            startObserveToNetworkState()
            Paper.book().write("Started", true)
        } else {
            Log.i("mina", "onCreateView: started true")
            if (homeViewModel.forecast != null)
                showData(homeViewModel.forecast!!)
            else {
                homeViewModel.getDataFromDatabase()
                startObserveToDatabase()
            }
        }
    }

    private fun initSettingsData() {
        tempUnit = Paper.book().read("temp", "metric")!!
        language = Paper.book().read("language", "en")!!
    }

    private fun initHourlyRecycleView() {
        hourlyList = ArrayList()
        hourlyAdapter = HourlyAdapter(requireActivity(), hourlyList)
        linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.homeRv.layoutManager = linearLayoutManager
        binding.homeRv.adapter = hourlyAdapter
        binding.homeRv.setHasFixedSize(true)
    }

    private fun initDailyRecycleView() {
        dailyList = ArrayList()
        dailyAdapter = DailyAdapter(requireActivity(), dailyList)
        linearLayoutManager2 = LinearLayoutManager(activity)
        binding.homeRv2.layoutManager = linearLayoutManager2
        binding.homeRv2.adapter = dailyAdapter
        binding.homeRv2.setHasFixedSize(true)
    }

    private fun saveLocation(location: Location) {
        Paper.book().write("location", location)
    }

    private fun startObserveToNetworkState() {
        homeViewModel.networkState.observe(requireActivity(), androidx.lifecycle.Observer {
            if (it) {
                if (homeViewModel.checkGpsState()) {
                    homeViewModel.getLocation()
                    startObserveToLocation()
                    if (Paper.book().read<Boolean>("firstTime", true)!!)
                        Paper.book().write("firstTime", false)
                } else {
                    if (Paper.book().read<Boolean>("firstTime", true)!!) {
                        requestGps()
                    } else {
                        showMessage(binding.homeRelative, "Gps is closed")
                        var location = Paper.book().read<Location>("location")!!
                        homeViewModel.getApiData( location.latitude,location.longitude,language,tempUnit)
                        startObserveToApiData()
                    }
                }
            } else {
                showMessage(binding.homeRelative, "No Internet")
                homeViewModel.getDataFromDatabase()
                startObserveToDatabase()
                startShimmer()
            }
            Paper.book().write("Started", true)
            homeViewModel.networkState.removeObservers(requireActivity())

        })
    }

    private fun startObserveToLocation() {
        homeViewModel.locationLiveData.observe(requireActivity(), Observer {
            if (it != null) {
                saveLocation(it)
                homeViewModel.getApiData(it.latitude, it.longitude, language, tempUnit)
                startObserveToApiData()
                homeViewModel.locationLiveData.removeObservers(requireActivity())
            }
        })
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                runFirstTime()
                Log.i("mina", "location accepted ")

            } else {
                requireActivity().finish()
            }
        }

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun startObserveToApiData() {
        homeViewModel.stateLiveData.observe(requireActivity(), Observer {
            when (it) {
                is HomeViewModel.HomeState.Loading -> {
                    startShimmer()
                    Log.i("mina", "startObserveToApiData:loading ")
                }
                is HomeViewModel.HomeState.OnSuccess -> {
                    Log.i("mina", "onCreateView: success")
                    stopShimmer()
                    showAndHideFields(View.VISIBLE)
                    showAndHideNoInternet(View.GONE)
                    showData(it.forecast)
                }
                is HomeViewModel.HomeState.OnFail -> {
                }
            }
        })
    }

    private fun startObserveToDatabase() {
        if (homeViewModel.forecastLiveData != null)
            homeViewModel.forecastLiveData!!.observe(requireActivity(), Observer {
                stopShimmer()
                if (it != null) {
                    showData(it)
                    homeViewModel.forecast = it
                } else {
                    showAndHideFields(View.GONE)
                    showAndHideNoInternet(View.VISIBLE)
                }
                homeViewModel.forecastLiveData!!.removeObservers(requireActivity())
            })

    }

    private fun getTempValue(): String {
        var tempValue: String = ""
        if (language.equals("en")) {
            when (tempUnit) {
                "metric" -> tempValue = "ْ c"
                "imperial" -> tempValue = "ْ f"
                "kel" -> tempValue = "kel"
            }
        } else
            when (tempUnit) {
                "metric" -> tempValue = "سليزيس"
                "imperial" -> tempValue = "فيهرنهايت"
                "kel" -> tempValue = "كيلفن"
            }
        return tempValue
    }

    private fun showData(forecast: Forecast) {
        binding.homeCard1TvTempMeas.text = getTempValue()
        hourlyAdapter.setList(forecast.hourly)
        dailyAdapter.setList(forecast.daily)
        binding.homeCard1TempText.text = forecast.current.weather.get(0).description
        binding.homeCard1TvTempNum.text = forecast.current.temp.toString()
        binding.homeTvTimeZone.text = forecast.timezone
        binding.homeCard2PressTv1.text = forecast.current.pressure.toString() + "hps"
        binding.homeCard2HumTv1.text = forecast.current.humidity.toString() + "%"
        binding.homeCard2cloudsTv1.text = forecast.current.clouds.toString() + "%"
        binding.homeCard2visTv1.text = forecast.current.visibility.toString() + "m"
        binding.homeCard2WindTv1.text = forecast.current.wind_speed.toString() + "m/s"
        binding.homeCard2uviTv1.text = forecast.current.uvi.toString()
    }

    private fun stopShimmer() {
        binding.homeShimmer.stopShimmer()
        binding.homeShimmer.visibility = View.GONE
        binding.homeRelative.visibility = View.VISIBLE
    }

    private fun startShimmer() {
        binding.homeShimmer.visibility = View.VISIBLE
        binding.homeRelative.visibility = View.GONE
        binding.homeShimmer.startShimmer()
    }

    private fun showAndHideFields(vis: Int) {
        binding.homeNoInternet.visibility = vis
        binding.homeCard1TvTempMeas.visibility = vis
        binding.homeTvTimeZone.visibility = vis
        binding.homeCard2.visibility = vis
        binding.homeCard1.visibility = vis
        binding.homeRv.visibility = vis
        binding.homeRv2.visibility = vis
    }

    private fun showAndHideNoInternet(vis: Int) {
        binding.homeNoInternet.visibility = vis
    }

    private fun showAndHideGpsCard(vis: Int) {
        binding.homeGpsCard.visibility = vis
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

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

    private fun requestGps() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

}


