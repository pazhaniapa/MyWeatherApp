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
import com.palmah.myweatherapp.utility.WeatherUtility
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

    fun saveToFavorites(weather: Weather){
        viewModelScope.launch {
            weatherInfoUseCase?.saveWeatherInfoInFirestore(weather)
        }
    }

    fun formatWeatherObject(weather : Weather) : Weather{
        return WeatherUtility.formatWeatherObject(weather,androidApplication)
    }



}