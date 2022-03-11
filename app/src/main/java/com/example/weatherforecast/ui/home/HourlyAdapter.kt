package com.example.weatherforecast.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Hourly
import java.util.*

class HourlyAdapter(var houlyList:List<Hourly>) : RecyclerView.Adapter<HourlyAdapter.Holder>() {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvTime: TextView =itemView.findViewById(R.id.customItemRvHomeTime)
        var img: ImageView =itemView.findViewById(R.id.customItemRvHomeImg)
        var tvTempNum: TextView =itemView.findViewById(R.id.customItemRvHomeTemp)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v:View=LayoutInflater.from(parent.context).inflate(R.layout.custom_item_home_rv,null)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = houlyList.get(position).dt * 1000
        val hours :String
        if (cal.time.hours-12>0) {
            hours=((cal.time.hours-12).toString()+" pm")
        } else {
            hours=((-(cal.time.hours-12)).toString()+" am")
        }
         holder.tvTime.text=hours

        holder.tvTempNum.text=houlyList.get(position).temp.toString()


    }

    public fun setList(hourlyList:List<Hourly>)
    {
        this.houlyList=hourlyList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return houlyList.size
    }

}