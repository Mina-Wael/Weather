package com.example.weatherforecast.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherforecast.R
import com.example.weatherforecast.model.Hourly
import io.paperdb.Paper
import java.util.*

class HourlyAdapter(var context: Context, var houlyList: List<Hourly>) :
    RecyclerView.Adapter<HourlyAdapter.Holder>() {
    var temp:String=""
    var  language:String="en"
    var tempValue:String=""

    init {
        Paper.init(context)
        language= Paper.book().read("language","en")!!
        temp = Paper.book().read("temp","metric")!!
        when(temp)
        {
            "metric" ->tempValue="ْ c"
            "imperial" ->tempValue="ْ f"
            "kel" ->tempValue="kel"
        }

    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvTime: TextView = itemView.findViewById(R.id.customItemRvHomeTime)
        var img: ImageView = itemView.findViewById(R.id.customItemRvHomeImg)
        var tvTempNum: TextView = itemView.findViewById(R.id.customItemRvHomeTemp)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_item_home_rv, null)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val cal = Calendar.getInstance()
        cal.timeInMillis = houlyList.get(position).dt * 1000
        val hours: String
        if (cal.time.hours - 12 > 0) {
            hours = ((cal.time.hours - 12).toString() + " pm")
        } else if (cal.time.hours - 12 == 0)
            hours = ((12).toString() + " pm")
        else {
            hours = ((-(cal.time.hours - 12)).toString() + " am")
        }
        holder.tvTime.text = hours

        holder.tvTempNum.text = Math.round(houlyList.get(position).temp).toString() + " " + tempValue
        var url =
            "http://openweathermap.org/img/wn/${houlyList.get(position).weather.get(0).icon}@2x.png"
        Glide.with(context).load(url).into(holder.img)


    }

    public fun setList(hourlyList: List<Hourly>) {
        this.houlyList = hourlyList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return houlyList.size
    }

}