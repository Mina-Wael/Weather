package com.example.weatherforecast.ui.favorite

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howsweather.model.Forecast
import com.example.weatherforecast.OnDrawerListener
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentFavoriteBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView

class FavoriteFragment : Fragment(), OnDeleteListener ,OnItemClick{

    private var _binding: FragmentFavoriteBinding? = null
    lateinit var adapter: FavoriteAdapter
    lateinit var favoriteList: List<Forecast>
    lateinit var favoritrViewModel: FavoriteViewModel
    lateinit var layoutManager: LinearLayoutManager

    private val binding get() = _binding!!
    lateinit var navController:NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var factory = FavoriteViewModelFactory(requireContext())
        favoritrViewModel =
            ViewModelProvider(this, factory).get(FavoriteViewModel::class.java)

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNoPlaces

        favoriteList = ArrayList<Forecast>()
        adapter = FavoriteAdapter(requireContext(), favoriteList, this,this)
        layoutManager=LinearLayoutManager(requireContext())
        binding.favoriteRv.setHasFixedSize(true)
        binding.favoriteRv.layoutManager = layoutManager
        binding.favoriteRv.adapter = adapter
//        var dividerItemDecoration=DividerItemDecoration(requireContext(),layoutManager.orientation)
//
//        binding.favoriteRv.addItemDecoration(dividerItemDecoration)


        favoritrViewModel.favoriteList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                adapter.setList(favoriteList)
                hideFields(false)
            } else {
                adapter.setList(it)
                hideFields(true)
            }
        }
        return root
    }

    fun hideFields(hide: Boolean) {
        if (hide) {
            binding.noPlacesImage.visibility = View.GONE
            binding.textNoPlaces.visibility = View.GONE
        } else {
            binding.noPlacesImage.visibility = View.VISIBLE
            binding.textNoPlaces.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         navController = Navigation.findNavController(view)

        binding.favoriteFab.setOnClickListener(View.OnClickListener {
            navController.popBackStack()
            navController.navigate(R.id.mapsFragment)
        })
        favoritrViewModel.getFavoriteList(this)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility == View.GONE)
            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility =
                View.VISIBLE

//        var cl:OnDrawerListener=activity as OnDrawerListener
//        cl.enableDrawer()


//        requireActivity().findViewById<DrawerLayout>(R.id.mainDrawer).setDrawe
//        setOnClickListener(View.OnClickListener {
//            Toast.makeText(requireContext(), "cli", Toast.LENGTH_SHORT).show()
//        })



    }

    override fun onClick(v: View, position: Int, forecast: Forecast) {
        openOptionMenu(v, position, forecast)

    }

    fun openOptionMenu(v: View, position: Int, forecast: Forecast) {
        val popup = PopupMenu(v.context, v)
        popup.getMenuInflater().inflate(R.menu.rv_favorite_delete, popup.getMenu())
        popup.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
            PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(p0: MenuItem?): Boolean {
                Toast.makeText(
                    context, "You selected the action : " + p0!!.getTitle()
                        .toString() + " position " + position, Toast.LENGTH_SHORT
                ).show()
                favoritrViewModel.deleteFavorite(forecast)
                return true
            }
        })
        popup.show()
    }

    override fun onClick(id: Int,latLng: LatLng) {
       var action=FavoriteFragmentDirections.actionNavigationFavoriteToFavoriteDetails(latLng, id)
        navController.navigate(action)
//       requireActivity().findViewById<DrawerLayout>(R.id.mainDrawer).setOnClickListener(View.OnClickListener {
//           Toast.makeText(requireContext(), "cli", Toast.LENGTH_SHORT).show()
//       })
    //        .setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

//        setupWithNavController(navController,requireActivity().findViewById(R.id.mainDrawer))
//        requireActivity().findViewById<Toolbar>(R.id.mainToolbar).setid

    }


}