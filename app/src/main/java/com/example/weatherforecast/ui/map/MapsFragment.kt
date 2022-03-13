package com.example.weatherforecast.ui.map

import android.app.ActionBar
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.navigation.NavController
import androidx.navigation.NavGraphNavigator
import androidx.navigation.Navigation
import com.example.weatherforecast.R
import com.example.weatherforecast.repository.Repository

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView

class MapsFragment : Fragment(),OnSendData {

     var latLng:LatLng?=null
    lateinit var navController:NavController


    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        googleMap.setOnMapClickListener(GoogleMap.OnMapClickListener {
            googleMap.clear()
            Toast.makeText(requireActivity(), "lat lng"+it, Toast.LENGTH_SHORT).show()
            googleMap.addMarker(MarkerOptions().position(it))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            latLng=it
        })



    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        navController=Navigation.findNavController(view)


        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility= GONE


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.map_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

             var repo:Repository
          when(item.itemId){
             R.id.map_save-> {
                 if (latLng!=null){
                 repo=Repository.getInstance(requireActivity())
                 repo.insertFavorite(latLng!!)
                 makeText(requireActivity(), "saved", Toast.LENGTH_SHORT).show()
                     navController.navigate(R.id.navigation_favorite)
                 }
                 }
              else->navController.navigate(R.id.navigation_favorite)
          }

      return true
    }

    override fun onSendLatLng(lat: Double, lng: Double) {
        TODO("Not yet implemented")
    }
}