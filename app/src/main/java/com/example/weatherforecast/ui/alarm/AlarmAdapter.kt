package com.example.weatherforecast.ui.alarm

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import java.util.*
import kotlin.collections.ArrayList

class AlarmAdapter(var context: Context,var alarmList:ArrayList<AlarmModel>,var onItemClicked: OnItemClicked) :RecyclerView.Adapter<AlarmAdapter.Holder>() {


    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
           var tvTimeFrom:TextView=itemView.findViewById(R.id.alarmRowFromTime)
           var tvTimeTo:TextView=itemView.findViewById(R.id.alarmRowToTime)
           var tvDatTo:TextView=itemView.findViewById(R.id.alarmRowToDate)
           var tvDateFrom:TextView=itemView.findViewById(R.id.alarmRowFromDate)
           var img:ImageView=itemView.findViewById(R.id.alarmRomImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var v=LayoutInflater.from(parent.context).inflate(R.layout.custom_alarm_row,null,false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
      holder.tvTimeFrom.setText(getTime(alarmList.get(position).getCalenderFRom()))
      holder.tvTimeTo.setText(getTime(alarmList.get(position).getCalenderTO()))
      holder.tvDatTo.setText(getDate(alarmList.get(position).getCalenderTO()))
      holder.tvDateFrom.setText(getDate(alarmList.get(position).getCalenderFRom()))
        holder.img.setOnClickListener(View.OnClickListener {

            onItemClicked.OnDelete(position,it)
        })

    }

    fun getDate(calendar: Calendar):String{
       return android.text.format.DateFormat.format("dd-MM", calendar).toString()
    }

    fun getTime(calendar: Calendar):String{

        return calendar.time.hours.toString()+":"+calendar.time.minutes.toString()
    }
    fun deleteItem(position: Int)
    {
        alarmList.removeAt(position)
        notifyDataSetChanged()
    }
    fun addItem(alarmModel: AlarmModel)
    {
        alarmList.add(alarmModel)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }
}