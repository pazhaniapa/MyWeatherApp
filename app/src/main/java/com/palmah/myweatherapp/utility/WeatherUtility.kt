package com.palmah.myweatherapp.utility

import android.app.Application
import android.util.Log
import com.palmah.myweatherapp.R
import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.utility.Constants.TAG
import com.palmah.myweatherapp.utility.OpenWeatherConstants.COUNTRY
import com.palmah.myweatherapp.utility.OpenWeatherConstants.HUMIDITY
import com.palmah.myweatherapp.utility.OpenWeatherConstants.ID
import com.palmah.myweatherapp.utility.OpenWeatherConstants.MAIN
import com.palmah.myweatherapp.utility.OpenWeatherConstants.NAME
import com.palmah.myweatherapp.utility.OpenWeatherConstants.PRESSURE
import com.palmah.myweatherapp.utility.OpenWeatherConstants.SYSTEM
import com.palmah.myweatherapp.utility.OpenWeatherConstants.TEMP
import com.palmah.myweatherapp.utility.OpenWeatherConstants.TEMP_MAX
import com.palmah.myweatherapp.utility.OpenWeatherConstants.TEMP_MIN
import com.palmah.myweatherapp.utility.OpenWeatherConstants.VISIBILITY
import com.palmah.myweatherapp.utility.OpenWeatherConstants.WEATHER
import com.palmah.myweatherapp.utility.OpenWeatherConstants.WEATHER_DESCRIPTION
import com.palmah.myweatherapp.utility.OpenWeatherConstants.WIND
import com.palmah.myweatherapp.utility.OpenWeatherConstants.WIND_DEGREE
import com.palmah.myweatherapp.utility.OpenWeatherConstants.WIND_SPEED
import org.json.JSONObject
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object WeatherUtility {

    fun getWeatherFromOpenWeatherResponse(weatherResponse : String): Weather?
    {
        try {
            val weatherResponseJson = JSONObject(weatherResponse)
            val mainJsonObject = weatherResponseJson.getJSONObject(MAIN)
            val weatherJsonArray = weatherResponseJson.getJSONArray(WEATHER)
            val weatherJsonObject : JSONObject = weatherJsonArray.get(0) as JSONObject
            val windJsonObject = weatherResponseJson.getJSONObject(WIND)
            val systemJsonObject = weatherResponseJson.getJSONObject(SYSTEM)

            val cityName = weatherResponseJson.getString(NAME)
            val cityId = weatherResponseJson.getInt(ID)
            val temp = mainJsonObject.getDouble(TEMP)
            val minTemp = mainJsonObject.getDouble(TEMP_MIN)
            val maxTemp = mainJsonObject.getDouble(TEMP_MAX)
            val humidity = mainJsonObject.getInt(HUMIDITY)
            val pressure = mainJsonObject.getInt(PRESSURE)
            val weatherDescription = weatherJsonObject.getString(WEATHER_DESCRIPTION)
            val windSpeed = windJsonObject.getDouble(WIND_SPEED)
            val windDegree = windJsonObject.getInt(WIND_DEGREE)
            val visibility = weatherResponseJson.getInt(VISIBILITY)
            val country = systemJsonObject.getString(COUNTRY)

            return Weather(cityName,cityId,temp,minTemp,maxTemp,pressure,humidity,weatherDescription,windSpeed,windDegree,visibility,country,
                           System.currentTimeMillis(),false)

        }catch (e : Exception){
            Log.e(TAG,e.toString())
            return null
        }
    }

    fun formatWeatherObject(weather : Weather, androidApplication : Application) : Weather{
        //format time
        val sdf = SimpleDateFormat("dd-MM-yy HH:mm")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = weather.timeStamp
        sdf.format(calendar.time).let {
            weather.formattedTime = it
        }
        //format humidity
        weather.formattedHumidity = weather.humidity.toString().plus("%")
        //format pressure
        weather.formattedPressure = String.format(androidApplication.resources.getString(R.string.details_pressure),weather.pressure.toString())

        //format avg temp
        weather.formattedTemp = Utils.convertToCelcius(weather.temp).let {tempInCelcius->
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.FLOOR
            String.format(androidApplication.resources.getString(R.string.details_temp),df.format(tempInCelcius).plus(0x00B0.toChar()))
        }
        //format min temp
        weather.formattedMinTemp = Utils.convertToCelcius(weather.minTemp).let {tempInCelcius->
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.FLOOR
            String.format(androidApplication.resources.getString(R.string.details_temp),df.format(tempInCelcius).plus(0x00B0.toChar()))
        }
        //format max temp
        weather.formattedMaxTemp = Utils.convertToCelcius(weather.maxTemp).let {tempInCelcius->
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.FLOOR
            String.format(androidApplication.resources.getString(R.string.details_temp),df.format(tempInCelcius).plus(0x00B0.toChar()))
        }
        //format Visibility
        weather.formattedVisibility = Utils.convertToKm(weather.visibility).let {
            String.format(androidApplication.resources.getString(R.string.details_visibility),it.toString())
        }
        //format wind
        weather.formattedWindSpeed = Utils.convertToKmph(weather.windSpeed).let {
            String.format(androidApplication.resources.getString(R.string.details_wind_speed),it.toString())
        }
        return weather
    }
}