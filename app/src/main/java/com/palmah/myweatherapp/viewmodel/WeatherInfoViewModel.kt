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
    var isErrorOccuredMutableLiveData : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var isDataAvailableMutableLiveData : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val androidApplication = application

    init {
        weatherInfoUseCase = WeatherInfoUseCase()
        isErrorOccuredMutableLiveData.postValue(true)
    }

    /**
     * @param city - name of the city for which we need to fetch the weather data.
     * @param isNetworkAvailable - true if network is available otherwise false.
     * This method fetches the weather data from server if network is available otherwise fetch data from firestore if available.
     */
    fun getCurrentWeatherByCity(city : String, isNetworkAvailable : Boolean){
        viewModelScope.launch {
            val currentWeatherInfo = weatherInfoUseCase?.getCurrentWeatherByCity(city,isNetworkAvailable)
            currentWeatherInfo?.let {
                formatWeatherObject(currentWeatherInfo).let {
                    Log.d(TAG,"Current Weather Info: $currentWeatherInfo")
                    weatherMutableLiveData.postValue(it)
                    isErrorOccuredMutableLiveData.postValue(false)
                    isDataAvailableMutableLiveData.postValue(true)
                }
            }
            if(currentWeatherInfo == null){
                isErrorOccuredMutableLiveData.postValue(true)
                isDataAvailableMutableLiveData.postValue(false)
            }
        }
    }

    /**
     * This method fetches the list of all cities from firstore that user searched earlier.
     */
    fun getAllCities(){
        viewModelScope.launch {
            val cityList = weatherInfoUseCase?.getAllCities()
            Log.d(TAG,"Cities: ${cityList}")
            cityListMutableLiveData.postValue(cityList)
        }
    }

    /**
     * @param weatherInfo - weather data which is needed to cache in firestore.
     * This method updates the favorite value and saves the weather information to firestore.
     */
    fun saveToFavorites(weather: Weather){
        viewModelScope.launch {
            weatherInfoUseCase?.saveWeatherInfoInFirestore(weather)
        }
    }

    /**
     * @param weather - The weather object to be displayed.
     * This methods formats the weather data and populates the UI parameters which is required for UI binding.
     */
    fun formatWeatherObject(weather : Weather) : Weather{
        return WeatherUtility.formatWeatherObject(weather,androidApplication)
    }
}