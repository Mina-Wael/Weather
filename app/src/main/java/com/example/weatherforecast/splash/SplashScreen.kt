package com.example.weatherforecast.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.weatherforecast.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class SplashScreen : Fragment() {

    companion object {
        fun newInstance() = SplashScreen()
    }

    private lateinit var viewModel: SplashScreenViewModel
    private lateinit var navController:NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.splash_screen_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController=Navigation.findNavController(view)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SplashScreenViewModel::class.java)
        requireActivity().findViewById<Toolbar>(R.id.mainToolbar).visibility=View.GONE
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility=View.GONE


//         Handler().postDelayed( Runnable() {
//              run() {
//
//                  val navOptions: NavOptions = NavOptions.Builder()
//                      .setPopUpTo(R.id.navigation_home, true)
//                      .build()
//            }
//        },  2000);
    }

}