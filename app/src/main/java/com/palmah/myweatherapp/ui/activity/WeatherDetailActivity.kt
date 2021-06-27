package com.palmah.myweatherapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.palmah.myweatherapp.R
import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.ui.fragment.WeatherInfoFragment
import com.palmah.myweatherapp.utility.Constants.TAG
import com.palmah.myweatherapp.utility.Constants.WEATHER_DATA
import com.palmah.myweatherapp.utility.Constants.WEATHER_DATA_BUNDLE

class WeatherDetailActivity : AppCompatActivity() {

    private var weather : Weather? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_detail)

        val weatherInfoFragment = WeatherInfoFragment.newInstance()
        intent.getBundleExtra(WEATHER_DATA_BUNDLE)?.let {
            weatherInfoFragment.arguments = it
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.weather_detail_fragment_container, weatherInfoFragment)
            .commitNow()
    }
}