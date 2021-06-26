package com.palmah.myweatherapp.webservice.`interface`

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IWeatherService {
    @GET("data/2.5/weather?")
    fun getCurrentWeatherByCity(@Query("q") city: String, @Query("appid") app_id: String) : Call<JsonElement>

}