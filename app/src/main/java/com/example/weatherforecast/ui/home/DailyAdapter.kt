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
import io.paperdb.Paper
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter(var context: Context, var dailyList: List<Daily>) :
    RecyclerView.Adapter<DailyAdapter.Holder>() {
    var language: String = "en"
    var temp: String = ""

    var tempValue: String = ""


    init {
        Paper.init(context)
        language = Paper.book().read("language", "en")!!
        temp = Paper.book().read("temp", "metric")!!
        when (temp) {
            "metric" -> tempValue = "ْ c"
            "imperial" -> tempValue = "ْ f"
            "kel" -> tempValue = "kel"
        }

    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvDay: TextView = itemView.findViewById(R.id.customHomeRv2TvDay)
        var img: ImageView = itemView.findViewById(R.id.customHomeRv2Img)
        var tvTempText: TextView = itemView.findViewById(R.id.customHomeRv2TvTempText)
        var tvTempNum: TextView = itemView.findViewById(R.id.customHomeRv2TvTempNum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.custom_item_home_rv2, null)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (giveDate().equals(getDate(dailyList.get(position).dt)))
            if (language.equals("en"))
                holder.tvDay.text = "today"
            else holder.tvDay.text = "اليوم"
        else
            holder.tvDay.text = getDayName(getDateNum(dailyList.get(position).dt)!!)

        holder.tvTempText.text = dailyList.get(position).weather.get(0).description
        holder.tvTempNum.text =
            changeEnglishNum((Math.round(dailyList.get(position).temp.max)).toString()) + "/" +
                    changeEnglishNum((Math.round(dailyList.get(position).temp.min)).toString()) + tempValue
        var url =
            "http://openweathermap.org/img/wn/${dailyList.get(position).weather.get(0).icon}@2x.png"
        Glide.with(context).load(url).into(holder.img)

    }

    fun giveDate(): String? {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        return sdf.format(cal.time)
    }

    fun setList(dailyList: List<Daily>) {
        language = Paper.book().read("language", "en")!!
        this.dailyList = dailyList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return dailyList.size
    }

    fun getDayName(dayOfWeek: Int): String {

        var weekDay: String = ""
        if (language.equals("en")) {
            if (dayOfWeek == 2) weekDay = "mon"
            else if (dayOfWeek == 3) weekDay = "tue"
            else if (dayOfWeek == 4) weekDay = "wed"
            else if (dayOfWeek == 5) weekDay = "thu"
            else if (dayOfWeek == 6) weekDay = "fri"
            else if (dayOfWeek == 7) weekDay = "sat"
            else if (dayOfWeek == 1) weekDay = "sun"
            else weekDay = ""
        } else {
            if (Calendar.MONDAY == dayOfWeek) weekDay = "الاثنين"
            else if (Calendar.TUESDAY == dayOfWeek) weekDay = "الثلاثاء"
            else if (Calendar.WEDNESDAY == dayOfWeek) weekDay = "الاريعاء"
            else if (Calendar.THURSDAY == dayOfWeek) weekDay = "الخميس"
            else if (Calendar.FRIDAY == dayOfWeek) weekDay = "الجمعه"
            else if (dayOfWeek == 7) weekDay = "السبت"
            else if (Calendar.SUNDAY == dayOfWeek) weekDay = "الاحد"
        }

        return weekDay
    }

    fun changeEnglishNum(str: String): String {
        if (language.equals("ar")) {
            var result = ""
            var en = '0'
            for (ch in str) {
                en = ch
                when (ch) {
                    '0' -> en = '۰'
                    '1' -> en = '۱'
                    '2' -> en = '۲'
                    '3' -> en = '۳'
                    '4' -> en = '٤'
                    '5' -> en = '۵'
                    '6' -> en = '٦'
                    '7' -> en = '۷'
                    '8' -> en = '۸'
                    '9' -> en = '۹'
                }
                result = "${result}$en"
            }
            return result
        } else {
            return str
        }
    }


    private fun getDate(time: Long): String? {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = time * 1000
        var daynum = cal.get(Calendar.DAY_OF_WEEK)
        Log.i("date1", "getDate: " + daynum)
        return android.text.format.DateFormat.format("dd-MM", cal).toString()
    }

    private fun getDateNum(time: Long): Int? {
        val cal = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = time * 1000
        return cal.get(Calendar.DAY_OF_WEEK)
    }


}