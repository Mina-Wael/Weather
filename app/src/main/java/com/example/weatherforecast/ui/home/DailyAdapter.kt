package com.example.weatherforecast.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howsweather.model.Daily
import com.example.weatherforecast.R
import java.util.*

class DailyAdapter(var context: Context,var dailyList: List<Daily>) :RecyclerView.Adapter<DailyAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tvDay:TextView=itemView.findViewById(R.id.customHomeRv2TvDay)
        var img:ImageView=itemView.findViewById(R.id.customHomeRv2Img)
        var tvTempText:TextView=itemView.findViewById(R.id.customHomeRv2TvTempText)
        var tvTempNum:TextView=itemView.findViewById(R.id.customHomeRv2TvTempNum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v:View=LayoutInflater.from(parent.context).inflate(R.layout.custom_item_home_rv2,null)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvDay.text=getDayName(dailyList.get(position).dt)
        holder.tvTempText.text=dailyList.get(position).weather.get(0).description
        holder.tvTempNum.text=(Math.round(dailyList.get(position).temp.max)).toString()+"/"+(Math.round(dailyList.get(position).temp.min)).toInt()
        var url="http://openweathermap.org/img/wn/${dailyList.get(position).weather.get(0).icon}@2x.png"
        Glide.with(context).load(url).into(holder.img)

    }

    fun setList(dailyList: List<Daily>)
    {
        this.dailyList=dailyList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dailyList.size
    }

    fun getDayName(dt:Long):String
    {
        var cal:Calendar= Calendar.getInstance()
        cal.timeInMillis=dt*1000
        var dayOfWeek:Int=cal.time.day
        Log.i("TAG", "getDayName: "+dayOfWeek+" "+dt)
        var weekDay:String
        if (Calendar.MONDAY == dayOfWeek) weekDay = "mon"
        else if (Calendar.TUESDAY == dayOfWeek) weekDay = "tue"
        else if (Calendar.WEDNESDAY == dayOfWeek) weekDay = "wed"
        else if (Calendar.THURSDAY == dayOfWeek) weekDay = "thu"
        else if (Calendar.FRIDAY == dayOfWeek) weekDay = "fri"
        else if (dayOfWeek==0) weekDay = "sat"
        else if (Calendar.SUNDAY == dayOfWeek) weekDay = "sun"
        else weekDay=""
        return weekDay
    }

}