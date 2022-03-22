package com.example.weatherforecast.splash

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.PermissionFragmentBinding
import com.example.weatherforecast.ui.map.MapsFragment
import com.example.weatherforecast.util.showSnackbar
import com.google.android.material.snackbar.Snackbar
import io.paperdb.Paper

class PermissionFragment : Fragment(), OnRequestResult {
    val PERMISSION_ID = 4
    var isConnected = false
    var source: Int = 5
    lateinit var binding: PermissionFragmentBinding


    companion object {
        fun newInstance() = PermissionFragment()
    }

    private lateinit var viewModel: PermissionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PermissionFragmentBinding.inflate(inflater, container, false)
        requestPer()
        Paper.init(requireContext())
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PermissionViewModel::class.java)

        viewModel.net.observe(requireActivity(), Observer {
            if (it) {
                if (source == 0) {
                    Paper.book().write("gps",true)
                    Paper.book().write("comeFrom","gps")
                    Paper.book().write("location","gps")
                    Paper.book().write("source","gps")

                } else if (source == 1) {
                    Paper.book().write("map",true)
                    Paper.book().write("comeFrom","map")
                    Paper.book().write("location","map")

                }
                Paper.book().write("first",false)
                startActivity(Intent(activity, MainActivity::class.java))
                requireActivity().finish()

            } else
                binding.root.showSnackbar("No Internet", Snackbar.LENGTH_SHORT)

        })

    }

    override fun onResume() {
           if(checkPer())
               showDialog()
        super.onResume()
    }


    fun requestPer() //ask user for location permession
    {

        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }


    fun checkPer(): Boolean // check if location permession is granted
    {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
            PackageManager.PERMISSION_GRANTED
        )
            return true
        else
            return false
    }


    override fun onSendResult() {
//        showDialog()
        Toast.makeText(requireContext(), "true", Toast.LENGTH_SHORT).show()

    }

    fun showDialog() {
        var dialog = AlertDialog.Builder(activity)
        var v: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.custom_init_dialog, null, false)
        var radioGroup: RadioGroup = v.findViewById(R.id.dialog_RG)
        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.dialog_RB_Gps -> {
                    viewModel.checkNetwork()
                    source = 0

                }
                R.id.dialog_RB_map -> {
                    viewModel.checkNetwork()
                    source = 1
                }
            }
        })
        dialog.setView(v).create().show()

    }
//fun showDialog(){
//    var alert=AlertDialog.Builder(activity)
//    alert.setTitle("Are you Sure to exit").setIcon(R.drawable.info_icon).setPositiveButton("Ok",
//        DialogInterface.OnClickListener { dialogInterface, i ->
////            onDrawerListener.enableDrawer()
////            navController.navigate(R.id.navigation_favorite)
//
//        }).setNegativeButton("Cancel", DialogInterface.OnClickListener
//    { dialogInterface, i ->  }).create().show()
//}

}