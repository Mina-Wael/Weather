package com.example.weatherforecast.ui.home

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
import io.paperdb.Paper


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
    lateinit var temp: String
    lateinit var language: String

    private val binding get() = _binding!!
    var lat: Double = 0.0
    var lng: Double = 0.0

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Paper.init(requireContext())

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var homeViewModelFactory = HomeViewModelFactory(requireActivity())
        homeViewModel = ViewModelProvider(this, homeViewModelFactory).get(HomeViewModel::class.java)

        temp = Paper.book().read("temp", "metric")!!
        language = Paper.book().read("language", "en")!!

        lat = 31.55654
        lng = 32.65659
        showAndHideGpsCard(View.GONE)

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

        binding.homeSwipeRefresh.setOnRefreshListener {
            homeViewModel.checkNetwork()
            startObserveToNetworkState()
            binding.homeSwipeRefresh.isRefreshing = false
            Toast.makeText(requireActivity(), "Updated", Toast.LENGTH_SHORT).show()
        }

        if (!(Paper.book().read("Started", false)!!)) {
            Log.i("mina", "onResume: ")
            homeViewModel.checkNetwork()
            startObserveToNetworkState()
            Paper.book().write("Started", true)
        }
        else
        {
            Log.i("mina", "onCreateView: started true")
            if (homeViewModel.forecast!=null)
            showData(homeViewModel.forecast!!)
            else{
                showAndHideFields(View.GONE)
                showAndHideNoInternet(View.VISIBLE)}
        }

        return root
    }

    private fun startObserveToNetworkState() {
        homeViewModel.networkState.observe(requireActivity(), androidx.lifecycle.Observer {
            if (it) {
                homeViewModel.getApiData(lat, lng, language, temp)
                startObserveToApiData()
                Log.i("mina", "onCreateView: network ok")
            } else {
                Log.i("mina", "onCreateView: network not ok")
                homeViewModel.getDataFromDatabase()
                startObserveToDatabase()
                startShimmer()
            }
            Paper.book().write("Started", true)
            homeViewModel.networkState.removeObservers(requireActivity())

        })
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
        if (homeViewModel.forecastLiveData!=null)
            homeViewModel.forecastLiveData!!.observe(requireActivity(), Observer {
                stopShimmer()
                if (it != null) {
                    showData(it)
                    homeViewModel.forecast=it
                }
                else{
                    showAndHideFields(View.GONE)
                    showAndHideNoInternet(View.VISIBLE)
                }
            })
    }

    private fun getTempValue(): String {
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
        return tempValue
    }

    private fun showData(forecast: Forecast) {
        binding.homeCard1TvTempMeas.setText(getTempValue())
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

    private fun showAndHideNoInternet(vis:Int){
        binding.homeNoInternet.visibility=vis
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

    fun enableGps() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

}


