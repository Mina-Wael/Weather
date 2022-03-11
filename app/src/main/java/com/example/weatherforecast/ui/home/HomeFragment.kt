package com.example.weatherforecast.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howsweather.model.Daily
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.model.Hourly

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        var hourlyList:List<Hourly> = ArrayList()
        val hourlyAdapter=HourlyAdapter(hourlyList)
        val linearLayoutManager:LinearLayoutManager= LinearLayoutManager(activity)
        linearLayoutManager.orientation=LinearLayoutManager.HORIZONTAL
        binding.homeRv.layoutManager=linearLayoutManager
        binding.homeRv.adapter=hourlyAdapter
        binding.homeRv.setHasFixedSize(true)

        var dailyList:List<Daily> = ArrayList()
        val dailyAdapter=DailyAdapter(dailyList)
        val linearLayoutManager2= LinearLayoutManager(activity)
        binding.homeRv2.layoutManager=linearLayoutManager2
        binding.homeRv2.adapter=dailyAdapter
        binding.homeRv2.setHasFixedSize(true)

        homeViewModel.getData()


        homeViewModel.hourlyList.observe(viewLifecycleOwner) {
            hourlyAdapter.setList(it)
        }
        homeViewModel.dialyList.observe(viewLifecycleOwner)
        {
            dailyAdapter.setList(it)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}