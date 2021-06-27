package com.palmah.myweatherapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.palmah.myweatherapp.R
import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.usecase.WeatherInfoUseCase
import com.palmah.myweatherapp.utility.Constants.TAG
import com.palmah.myweatherapp.utility.Utils
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class WeatherInfoViewModel(application: Application) : AndroidViewModel(application) {

    private var weatherInfoUseCase : WeatherInfoUseCase? = null

    var weatherMutableLiveData : MutableLiveData<Weather> = MutableLiveData<Weather>()
    var cityListMutableLiveData : MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    val androidApplication = application

    init {
        weatherInfoUseCase = WeatherInfoUseCase()
    }

    fun getCurrentWeatherByCity(city : String){
        viewModelScope.launch {
            val currentWeatherInfo = weatherInfoUseCase?.getCurrentWeatherByCity(city)
            currentWeatherInfo?.let {
                formatWeatherObject(currentWeatherInfo).let {
                    Log.d(TAG,"Current Weather Info: $currentWeatherInfo")
                    weatherMutableLiveData.postValue(it)

                }
            }

        }
    }

    fun getFavoriteCitiesWeatherInfoList(){
        viewModelScope.launch {
            val favoriteCitiesWeatherInfoList = weatherInfoUseCase?.getFavoriteCitiesWeatherInfoList()
            Log.d(TAG,"Favorite Cities WetherInfo List: ${favoriteCitiesWeatherInfoList.toString()}")
        }

    }

    fun getAllCities(){
        viewModelScope.launch {
            val cityList = weatherInfoUseCase?.getAllCities()
            Log.d(TAG,"Cities: ${cityList}")
            cityListMutableLiveData.postValue(cityList)
        }
    }

    private fun formatWeatherObject(weather : Weather) : Weather{
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