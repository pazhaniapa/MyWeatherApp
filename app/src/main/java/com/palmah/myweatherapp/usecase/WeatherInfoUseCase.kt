package com.palmah.myweatherapp.usecase

import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.repo.FirestoreWeatherRepoImpl
import com.palmah.myweatherapp.repo.IWeatherRepo
import com.palmah.myweatherapp.repo.OpenWeatherRepoImpl
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherInfoUseCase {

    suspend fun getCurrentWeatherByCity(city: String) : Weather?{
        var weatherRepo : IWeatherRepo = OpenWeatherRepoImpl()
        var weather = weatherRepo.getCurrentWeatherByCity(city).await()
        weather?.let {
            saveWeatherInfoInFirestore(it)
        }
        if(weather == null){
            weatherRepo = FirestoreWeatherRepoImpl()
            weather = weatherRepo.getCurrentWeatherByCity(city).await()
        }
        return weather
    }

    suspend fun saveWeatherInfoInFirestore(weatherInfo : Weather){
        val firestoreRepo : IWeatherRepo = FirestoreWeatherRepoImpl()
        firestoreRepo.saveWeatherInfo(weatherInfo)

    }

    suspend fun getFavoriteCitiesWeatherInfoList() : ArrayList<Weather>{
        val firestoreRepo : IWeatherRepo = FirestoreWeatherRepoImpl()
        val favoriteCitiesWeatherInfoList = firestoreRepo.getFavoriteCitiesWeatherInfoList().await()
        return favoriteCitiesWeatherInfoList
    }

    suspend fun getAllCities(): ArrayList<String>{
        val firestoreRepo : IWeatherRepo = FirestoreWeatherRepoImpl()
        val cityList = firestoreRepo.getAllCities().await()
        return cityList
    }


}