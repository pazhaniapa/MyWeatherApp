package com.palmah.myweatherapp.ui.adapter

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.palmah.myweatherapp.R
import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.ui.activity.WeatherDetailActivity
import com.palmah.myweatherapp.utility.Constants.TAG
import com.palmah.myweatherapp.utility.Constants.WEATHER_DATA
import com.palmah.myweatherapp.utility.Constants.WEATHER_DATA_BUNDLE

internal class FavoriteCityWeatherListAdapter(val androidApplication: Application, var activity : Activity, var weatherList: List<Weather>) :
    RecyclerView.Adapter<FavoriteCityWeatherListAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cityNameTv: TextView = view.findViewById(R.id.city_info_textview)
        var weatherInfoTv: TextView = view.findViewById(R.id.weather_info_textview)
        var dateTimeTv: TextView = view.findViewById(R.id.weather_updated_time_textview)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_favorite_city_weather_row, parent, false)
        itemView.setOnClickListener {

        }
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val weather = weatherList[position]
        holder.cityNameTv.text = String.format(androidApplication.resources.getString(R.string.city_info),weather.cityName,weather.country)
        holder.weatherInfoTv.text = weather.formattedTemp
        holder.dateTimeTv.text = weather.formattedTime

        holder.itemView.setOnClickListener {
            Log.d(TAG,"RecyclerView OnClick ${weather}")

            Intent(activity,WeatherDetailActivity::class.java).let {
                val bundle = Bundle().also {
                    it.putParcelable(WEATHER_DATA,weather)
                }
                it.putExtra(WEATHER_DATA_BUNDLE,bundle)
                activity.startActivity(it)
            }
        }
    }
    override fun getItemCount(): Int {
        return weatherList.size
    }
}