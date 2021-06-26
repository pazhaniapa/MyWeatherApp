package com.palmah.myweatherapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.usecase.WeatherInfoUseCase
import com.palmah.myweatherapp.utility.Constants.TAG
import kotlinx.coroutines.launch

class WeatherInfoViewModel : ViewModel() {

    private var weatherInfoUseCase : WeatherInfoUseCase? = null

    var weatherMutableLiveData : MutableLiveData<Weather> = MutableLiveData<Weather>()

    init {
        weatherInfoUseCase = WeatherInfoUseCase()
    }

    fun getCurrentWeatherByCity(city : String){
        viewModelScope.launch {
            val currentWeatherInfo = weatherInfoUseCase?.getCurrentWeatherByCity(city)
            Log.d(TAG,"Current Weather Info: $currentWeatherInfo")
            weatherMutableLiveData.postValue(currentWeatherInfo)
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

        }
    }


}