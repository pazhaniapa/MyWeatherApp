package com.palmah.myweatherapp.repo

import com.palmah.myweatherapp.entity.Weather
import kotlinx.coroutines.Deferred

interface IWeatherRepo {

    suspend fun getCurrentWeatherByCity(city : String) : Deferred<Weather?>

    suspend fun saveWeatherInfo(weatherInfo: Weather)

    suspend fun getFavoriteCitiesWeatherInfoList() : Deferred<ArrayList<Weather>>

    suspend fun getAllCities() : Deferred<ArrayList<String>>
}