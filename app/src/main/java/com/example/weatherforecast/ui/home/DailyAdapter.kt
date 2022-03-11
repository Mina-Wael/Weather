package com.example.weatherforecast.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.howsweather.model.Daily
import com.example.weatherforecast.R

class DailyAdapter(var dailyList: List<Daily>) :RecyclerView.Adapter<DailyAdapter.Holder>() {

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
        holder.tvDay.text=dailyList.get(position).dt.toString()
    }

    fun setList(dailyList: List<Daily>)
    {
        this.dailyList=dailyList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dailyList.size
    }

}