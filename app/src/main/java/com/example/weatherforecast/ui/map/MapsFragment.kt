package com.example.weatherforecast.ui.map

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weatherforecast.OnDrawerListener
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentMapsBinding
import com.example.weatherforecast.repository.Repository
import com.example.weatherforecast.util.showSnackbar

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import io.paperdb.Paper

class MapsFragment : Fragment(), OnSendData {

    var latLng: LatLng? = null
    lateinit var navController: NavController

    lateinit var onDrawerListener: OnDrawerListener

    lateinit var binding: FragmentMapsBinding


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
        val sydney = LatLng(30.033333, 31.233334)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Egypt"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        googleMap.setOnMapClickListener(GoogleMap.OnMapClickListener {
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(it))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(it))
            latLng = it
            Log.i("map", ":lat " + latLng!!.latitude + " lng " + latLng!!.longitude)


        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        onDrawerListener = activity as OnDrawerListener

        onDrawerListener.disableDrawer()

        binding = FragmentMapsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        navController = Navigation.findNavController(view)


        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = GONE

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        requireActivity().findViewById<Toolbar>(R.id.mainToolbar).setNavigationOnClickListener {
            showDialog()
        }
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.map_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Paper.init(requireContext())
        var language = Paper.book().read("language", "en")
        var temp = Paper.book().read("temp", "metric")
        var repo: Repository
        when (item.itemId) {
            R.id.map_save -> {
                if (latLng != null) {
                    if (Paper.book().read<Boolean>("map", false)!!||Paper.book().read<Boolean>("settingMap", false)!!) {
                        Paper.book().write("mapLat",latLng!!.latitude)
                        Paper.book().write("mapLng",latLng!!.longitude)
                        Paper.book().write("map",false)
                        Paper.book().write("settingMap",false)
                        navController.navigate(R.id.Home)
                    } else {
                        repo = Repository.getInstance(requireActivity())
                        repo.insertFavorite(latLng!!, language, temp)
                        makeText(requireActivity(), "saved", Toast.LENGTH_SHORT).show()
                        navController.navigate(R.id.navigation_favorite)
                    }
                } else
                    binding.root.showSnackbar("No Location Selected", Snackbar.LENGTH_SHORT)

            }
        }
        return true
    }

    override fun onSendLatLng(lat: Double, lng: Double) {
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    fun showDialog() {
        var alert = AlertDialog.Builder(activity)
        alert.setTitle("Are you Sure to exit").setIcon(R.drawable.info_icon).setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialogInterface, i ->
                onDrawerListener.enableDrawer()
                navController.navigate(R.id.navigation_favorite)

            }).setNegativeButton("Cancel", DialogInterface.OnClickListener
        { dialogInterface, i -> }).create().show()
    }

    override fun onPause() {
        onDrawerListener.enableDrawer()
        super.onPause()
    }

}