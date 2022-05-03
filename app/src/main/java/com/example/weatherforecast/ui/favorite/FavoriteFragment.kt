package com.example.weatherforecast.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.howsweather.model.Forecast
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentFavoriteBinding
import com.example.weatherforecast.util.showSnackbar
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

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

    override fun onStart() {
        navController.popBackStack(R.id.mapsFragment,true)
        navController.popBackStack(R.id.Details,true)
        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         navController = Navigation.findNavController(view)

        binding.favoriteFab.setOnClickListener(View.OnClickListener {
            favoritrViewModel.checkNetwork()

        })

        favoritrViewModel.net.observe(requireActivity(), Observer {
            if (it)
                navController.navigate(R.id.mapsFragment)
           else
            binding.root.showSnackbar(getString(R.string.NoInternetMsg),Snackbar.LENGTH_SHORT)

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
                    context, "Deleted", Toast.LENGTH_SHORT
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

    }


}