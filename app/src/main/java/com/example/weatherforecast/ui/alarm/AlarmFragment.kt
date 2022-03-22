package com.example.weatherforecast.ui.alarm

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentNotificationsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import kotlin.collections.ArrayList


class AlarmFragment : Fragment(),OnItemClicked {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var alarmAdapter: AlarmAdapter
    lateinit var alarmList: ArrayList<AlarmModel>
    var comeFrom=0
     var calendar:Calendar= Calendar.getInstance()
    lateinit var alarmModel:AlarmModel
    lateinit var tvComeFromTime:TextView
    lateinit var tvComeFromDate:TextView
    lateinit var tvComeToTime:TextView
    lateinit var tvComeToDate:TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(AlarmViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        alarmList= ArrayList()

        alarmAdapter= AlarmAdapter(requireContext(),alarmList,this)
        binding.alarmRV.adapter=alarmAdapter
        binding.alarmRV.layoutManager=LinearLayoutManager(requireContext())
        binding.alarmRV.setHasFixedSize(true)
        alarmModel= AlarmModel()


        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.alarmFab.setOnClickListener(View.OnClickListener {
            showDialog()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility==View.GONE)
            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility= View.VISIBLE

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun showDialog(){
        var dialog=AlertDialog.Builder(requireActivity())
        var v:View=LayoutInflater.from(requireContext()).inflate(R.layout.alarm_dialog,null,false)
        tvComeFromDate=v.findViewById(R.id.alarmTvDateFrom)
        tvComeFromTime=v.findViewById(R.id.alarmTvTimeFrom)
        tvComeToTime=v.findViewById(R.id.alarmTvTimeTo)
        tvComeToDate=v.findViewById(R.id.alarmTvDateTo)
        var btn:Button=v.findViewById(R.id.alarmBtnSave)
        btn.visibility=View.GONE


        v.findViewById<CardView>(R.id.alarmCard1).setOnClickListener {

            comeFrom=1
             showDateDialog()
         }
        v.findViewById<CardView>(R.id.alarmCard2).setOnClickListener {
            comeFrom=2
             showDateDialog()
         }
        dialog.setView(v).setPositiveButton("Save",
            DialogInterface.OnClickListener { dialogInterface, i ->
                alarmAdapter.addItem(alarmModel)

            }).create().show()

    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun showDateDialog(){
        var v:View=LayoutInflater.from(requireContext()).inflate(R.layout.custom_date_dialog,null,false)
        var dialog=AlertDialog.Builder(requireContext())
        var datePicker:DatePicker=v.findViewById(R.id.customDialogDate)

        datePicker.minDate=Calendar.getInstance().timeInMillis
        dialog.setView(v).setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialogInterface, i ->  }).
        setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialogInterface, i ->
                calendar.set(datePicker.year,datePicker.month,datePicker.dayOfMonth)

                showTimeDialog()
            }).create().show()
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun showTimeDialog(){
        var v:View=LayoutInflater.from(requireContext()).inflate(R.layout.custom_time_dialog,null,false)
        var timePicker:TimePicker=v.findViewById(R.id.customTimePicker)


        var dialog=AlertDialog.Builder(requireContext())
        dialog.setView(v).setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialogInterface, i ->  }).setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialogInterface, i ->
                calendar.time.hours=timePicker.hour
                calendar.time.minutes=timePicker.minute
                if(comeFrom==1){
                    alarmModel.setCalenderFRom(calendar)
                    tvComeFromDate.setText(getDate(calendar))
                    tvComeFromTime.setText(getTime(calendar))
                }
                else if(comeFrom==2){
                    alarmModel.setCalenderTO(calendar)
                    tvComeToDate.setText(getDate(calendar))
                    tvComeToTime.setText(getTime(calendar))
                }

            }).create().show()
    }
    fun getDate(calendar: Calendar):String{
        return android.text.format.DateFormat.format("dd-MM", calendar).toString()
    }

    fun getTime(calendar: Calendar):String{

        return calendar.time.hours.toString()+":"+calendar.time.minutes.toString()
    }


    override fun onItemClick() {
        TODO("Not yet implemented")
    }

    override fun OnDelete(position: Int, view: View) {
        val popup = PopupMenu(view.context, view)
        popup.getMenuInflater().inflate(R.menu.rv_favorite_delete, popup.getMenu())
        popup.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
            PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(p0: MenuItem?): Boolean {
                Toast.makeText(
                    context, "Deleted", Toast.LENGTH_SHORT
                ).show()
                alarmAdapter.deleteItem(position)
                return true
            }
        })
        popup.show()
    }

}