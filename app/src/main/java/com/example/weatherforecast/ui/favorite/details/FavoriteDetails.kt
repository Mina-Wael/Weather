package com.example.weatherforecast.ui.favorite.details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howsweather.model.Daily
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.OnDrawerListener
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FavoriteDetailsFragmentBinding
import com.example.weatherforecast.model.Hourly
import com.example.weatherforecast.ui.home.DailyAdapter
import com.example.weatherforecast.ui.home.HourlyAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.ArrayList
import kotlin.reflect.KProperty

class FavoriteDetails : Fragment() {

    private var _binding: FavoriteDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    lateinit var navController: NavController
    var i=0
    lateinit var onDrawerListener: OnDrawerListener




    var args:FavoriteDetailsArgs by navArgs()

    lateinit var hourlyList: List<Hourly>
    lateinit var hourlyAdapter: HourlyAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    lateinit var dailyList: List<Daily>
    lateinit var dailyAdapter: DailyAdapter
    lateinit var linearLayoutManager2: LinearLayoutManager

    companion object {
        fun newInstance() = FavoriteDetails()
    }

    private lateinit var viewModel: FavoriteDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FavoriteDetailsFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setHasOptionsMenu(true)
        onDrawerListener=activity as MainActivity

        onDrawerListener.disableDrawer()


        hourlyList = ArrayList()
        hourlyAdapter = HourlyAdapter(requireActivity(), hourlyList)
        linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.detailsRv.layoutManager = linearLayoutManager
        binding.detailsRv.adapter = hourlyAdapter
        binding.detailsRv.setHasFixedSize(true)

        dailyList = ArrayList()
        dailyAdapter = DailyAdapter(requireActivity(), dailyList)
        linearLayoutManager2 = LinearLayoutManager(activity)
        binding.detailsRv2.layoutManager = linearLayoutManager2
        binding.detailsRv2.adapter = dailyAdapter
        binding.detailsRv2.setHasFixedSize(true)
        binding.detailsCard1TvTempMeas.visibility = View.GONE



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var factory=DetailsViewModelFactory(requireContext())
        viewModel = ViewModelProviders.of(this,factory).get(FavoriteDetailsViewModel::class.java)

        navController= Navigation.findNavController(view)


        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility= View.GONE

        viewModel.getForecastById(this,args.id)
        viewModel.forecast.observe(viewLifecycleOwner) {
            if (it != null) {
                hourlyAdapter.setList(it!!.hourly)
                dailyAdapter.setList(it.daily)
                binding.detailsCard1TempText.text = it.current.weather.get(0).description
                binding.detailsCard1TvTempNum.text = it.current.temp.toString()
                binding.detailsCard1TvTempMeas.visibility = View.VISIBLE
                binding.detailsTvTimeZone.text = it.timezone
                binding.detailsCard2PressTv1.setText(it.current.pressure.toString()+"hps")
                binding.detailsCard2HumTv1.setText(it.current.humidity.toString()+"%")
                binding.detailsCard2cloudsTv1.setText(it.current.clouds.toString()+"%")
                binding.detailsCard2visTv1.setText(it.current.visibility.toString()+"m")
                binding.detailsCard2WindTv1.setText(it.current.wind_speed.toString()+"m/s")
                binding.detailsCard2uviTv1.setText(it.current.uvi.toString())

                Log.i("TAG", "onCreateView: get data from database " + it!!.timezone)
            } else
                clearData()

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//      requireActivity().findViewById<Toolbar>(R.id.mainToolbar).setna
//
//        requireActivity().findViewById<Toolbar>(R.id.mainToolbar).setNavigationIcon(null)
//
        requireActivity().findViewById<Toolbar>(R.id.mainToolbar).setNavigationOnClickListener {
            navController.navigate(R.id.navigation_favorite)
        }
//        requireActivity().findViewById<Toolbar>(R.id.mainToolbar).setLogo(null)





    }
    private fun clearData() {
        hourlyAdapter.setList(hourlyList)
        dailyAdapter.setList(dailyList)
        binding.detailsCard1TempText.text =""
        binding.detailsCard1TvTempNum.text =""
        binding.detailsCard1TvTempMeas.visibility = View.GONE
        binding.detailsTvTimeZone.text = ""
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        navController.navigate(R.id.navigation_favorite)
        return true
    }

    override fun onPause() {
        onDrawerListener.enableDrawer()
        super.onPause()

    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    override fun onDestroy() {
        super.onDestroy()

    }
}



private operator fun Any.setValue(favoriteDetails: FavoriteDetails, property: KProperty<*>, favoriteDetailsArgs: FavoriteDetailsArgs) {

}

