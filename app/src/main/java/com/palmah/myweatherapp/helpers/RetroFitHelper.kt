package com.palmah.myweatherapp.helpers

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroFitHelper {
    fun initializeRetrofit() : Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit
    }
}