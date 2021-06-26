package com.palmah.myweatherapp.usecase

import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.repo.FirestoreWeatherRepoImpl
import com.palmah.myweatherapp.repo.IWeatherRepo
import com.palmah.myweatherapp.repo.OpenWeatherRepoImpl

class WeatherInfoUseCase {

    suspend fun getCurrentWeatherByCity(city: String) : Weather?{
        var weatherRepo : IWeatherRepo = OpenWeatherRepoImpl()
        val weather = weatherRepo.getCurrentWeatherByCity(city).await()
        weather?.let {
            saveWeatherInfoInFirestore(it)
        }
        return weather
    }

    suspend fun saveWeatherInfoInFirestore(weatherInfo : Weather){
        val firestoreRepo : IWeatherRepo = FirestoreWeatherRepoImpl()
        firestoreRepo.saveWeatherInfo(weatherInfo)


    }
}