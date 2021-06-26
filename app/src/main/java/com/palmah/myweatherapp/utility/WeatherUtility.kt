package com.palmah.myweatherapp.utility

import android.util.Log
import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.utility.Constants.TAG
import com.palmah.myweatherapp.utility.OpenWeatherConstants.ID
import com.palmah.myweatherapp.utility.OpenWeatherConstants.MAIN
import com.palmah.myweatherapp.utility.OpenWeatherConstants.NAME
import com.palmah.myweatherapp.utility.OpenWeatherConstants.TEMP
import com.palmah.myweatherapp.utility.OpenWeatherConstants.TEMP_MAX
import com.palmah.myweatherapp.utility.OpenWeatherConstants.TEMP_MIN
import org.json.JSONObject

object WeatherUtility {

    fun getWeatherFromOpenWeatherResponse(weatherresponse : String): Weather?
    {

        try {
            val weatherResponseJson = JSONObject(weatherresponse)
            val mainJsonObject = weatherResponseJson.getJSONObject(MAIN)

            val cityName = weatherResponseJson.getString(NAME)
            val cityId = weatherResponseJson.getInt(ID)
            val temp = mainJsonObject.getDouble(TEMP)
            val minTemp = mainJsonObject.getDouble(TEMP_MIN)
            val maxTemp = mainJsonObject.getDouble(TEMP_MAX)

            return Weather(cityName,cityId,temp,minTemp,maxTemp)

        }catch (e : Exception){
            Log.e(TAG,e.toString())
            return null
        }

    }
}