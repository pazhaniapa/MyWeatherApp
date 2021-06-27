package com.palmah.myweatherapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.palmah.myweatherapp.R

class WeatherDetailsListAdapter(private val context: Context, private val weatherDetailsMapList : ArrayList<Map<String, String>>) : BaseAdapter() {

    private lateinit var labeltextView : TextView
    private lateinit var valueTextView : TextView

    override fun getCount(): Int {
       return weatherDetailsMapList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.weather_details_list_row, parent, false)
        labeltextView = convertView.findViewById(R.id.labelTextView)
        valueTextView = convertView.findViewById(R.id.valueTextView)

        weatherDetailsMapList?.let { list->
            list.get(position).let { map ->
                map.forEach {
                    labeltextView.text = it.key
                    valueTextView.text = it.value
                }
            }

        }
        return convertView

    }


}