package com.palmah.myweatherapp.repo

import android.util.Log
import com.google.gson.JsonElement
import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.helpers.RetroFitHelper
import com.palmah.myweatherapp.utility.Constants.OPEN_WEATHER_API_TOKEN
import com.palmah.myweatherapp.utility.Constants.TAG
import com.palmah.myweatherapp.utility.WeatherUtility
import com.palmah.myweatherapp.webservice.`interface`.IWeatherService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class OpenWeatherRepoImpl : IWeatherRepo {

    private var retrofit : Retrofit = RetroFitHelper().initializeRetrofit()

    override suspend fun getCurrentWeatherByCity(city: String): Deferred<Weather?> {
        return CoroutineScope(Dispatchers.IO).async {
             suspendCoroutine { continuation ->
                val service = retrofit.create(IWeatherService::class.java)
                val call = service.getCurrentWeatherByCity(city, OPEN_WEATHER_API_TOKEN)

                call.enqueue(object : Callback<JsonElement> {
                    override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                        Log.d(TAG, response.body().toString())
                        val weather = WeatherUtility.getWeatherFromOpenWeatherResponse(response.body().toString())
                        continuation.resume(weather)
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        Log.d(TAG, t.toString())
                        continuation.resume(null)
                    }
                })
            }
        }
    }

    override suspend fun saveWeatherInfo(weatherInfo: Weather) {
        TODO("Not yet implemented")
    }

    override suspend fun getFavoriteCitiesWeatherInfoList(): Deferred<ArrayList<Weather>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCities(): Deferred<ArrayList<String>> {
        TODO("Not yet implemented")
    }
}