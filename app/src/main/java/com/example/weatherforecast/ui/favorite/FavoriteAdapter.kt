package com.example.weatherforecast.ui.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.howsweather.model.Forecast
import com.example.weatherforecast.R


class FavoriteAdapter(var context: Context,var forecastList: List<Forecast>,var onDeleteListener: OnDeleteListener) :RecyclerView.Adapter<FavoriteAdapter.Holder>() {


    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var tv=itemView.findViewById<TextView>(R.id.favoriteCustomTv)
        var img:ImageView=itemView.findViewById(R.id.favoriteCustomImgMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var v=LayoutInflater.from(parent.context).inflate(R.layout.rv_favorite_custom_row,null)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
       holder.tv.text=forecastList.get(position).timezone
        holder.img.setOnClickListener(View.OnClickListener {
                onDeleteListener.onClick(it,position,forecastList.get(position))
        })
    }

    override fun getItemCount(): Int {
       return forecastList.size
    }

    fun setList(list: List<Forecast>)
    {
        this.forecastList=list
        notifyDataSetChanged()
    }

    fun openOptionMenu(v: View, position: Int) {
        val popup = PopupMenu(v.context, v)
        popup.getMenuInflater().inflate(R.menu.rv_favorite_delete, popup.getMenu())
        popup.setOnMenuItemClickListener(object : MenuItem.OnMenuItemClickListener,
            PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(p0: MenuItem?): Boolean {
                Toast.makeText(context,"You selected the action : " + p0!!.getTitle()
                        .toString() + " position " + position,Toast.LENGTH_SHORT).show()
                    return true
            }
        })
        popup.show()
    }
}