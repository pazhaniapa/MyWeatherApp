package com.palmah.myweatherapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.usecase.WeatherInfoUseCase
import com.palmah.myweatherapp.utility.Constants
import com.palmah.myweatherapp.utility.WeatherUtility
import kotlinx.coroutines.launch

class FavoritesViewModel (application: Application) : AndroidViewModel(application) {
    // TODO: Implement the ViewModel
    private var weatherInfoUseCase : WeatherInfoUseCase? = null
    val androidApplication = application
    var weatherMutableList : MutableLiveData<ArrayList<Weather>> = MutableLiveData<ArrayList<Weather>>()

    init {
        weatherInfoUseCase = WeatherInfoUseCase()
    }

    fun getFavoriteCitiesWeatherInfoList(){
        viewModelScope.launch {
            val favoriteCitiesWeatherInfoList = weatherInfoUseCase?.getFavoriteCitiesWeatherInfoList()
            Log.d(Constants.TAG,"Favorite Cities WetherInfo List: ${favoriteCitiesWeatherInfoList.toString()}")
            favoriteCitiesWeatherInfoList?.forEach {
                formatWeatherObject(it)
            }
            weatherMutableList.postValue(favoriteCitiesWeatherInfoList)
        }
    }

    fun formatWeatherObject(weather : Weather) : Weather{
        return WeatherUtility.formatWeatherObject(weather,androidApplication)
    }
}