package com.palmah.myweatherapp.usecase

import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.repo.FirestoreWeatherRepoImpl
import com.palmah.myweatherapp.repo.IWeatherRepo
import com.palmah.myweatherapp.repo.OpenWeatherRepoImpl
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherInfoUseCase {

    /**
     * @param city - name of the city for which we need to fetch the weather data.
     * @param isNetworkAvailable - true if network is available otherwise false.
     * This method fetches the weather data from server if network is available otherwise fetch data from firestore if available.
     */
    suspend fun getCurrentWeatherByCity(city: String, isNetworkAvailable: Boolean) : Weather?{
        var weatherRepo : IWeatherRepo
        weatherRepo = if(isNetworkAvailable){
            OpenWeatherRepoImpl()
        }else{
            FirestoreWeatherRepoImpl()
        }
        var weather = weatherRepo.getCurrentWeatherByCity(city).await()
        weather?.apply {
            weatherRepo = FirestoreWeatherRepoImpl()
            var weatherFromCache = weatherRepo.getCurrentWeatherByCity(city).await()
            weatherFromCache?.let { cachedWeather->
                this.favorite = cachedWeather.favorite
            }
            saveWeatherInfoInFirestore(this)
        }
        if(weather == null){
            weatherRepo = FirestoreWeatherRepoImpl()
            weather = weatherRepo.getCurrentWeatherByCity(city).await()
        }
        return weather
    }

    /**
     * @param weatherInfo - weather data which is needed to cache in firestore.
     * This method saves the weather information to firestore.
     */
    suspend fun saveWeatherInfoInFirestore(weatherInfo : Weather){
        val firestoreRepo : IWeatherRepo = FirestoreWeatherRepoImpl()
        firestoreRepo.saveWeatherInfo(weatherInfo)
    }

    /**
     * This methods fetches the list of weather information of favorite cities.
     */
    suspend fun getFavoriteCitiesWeatherInfoList() : ArrayList<Weather>{
        val firestoreRepo : IWeatherRepo = FirestoreWeatherRepoImpl()
        val favoriteCitiesWeatherInfoList = firestoreRepo.getFavoriteCitiesWeatherInfoList().await()
        return favoriteCitiesWeatherInfoList
    }

    /**
     * This method fetches the list of all cities from firstore that user searched earlier.
     */
    suspend fun getAllCities(): ArrayList<String>{
        val firestoreRepo : IWeatherRepo = FirestoreWeatherRepoImpl()
        val cityList = firestoreRepo.getAllCities().await()
        return cityList
    }
}