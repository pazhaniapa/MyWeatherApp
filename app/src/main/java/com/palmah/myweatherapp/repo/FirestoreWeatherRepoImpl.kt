package com.palmah.myweatherapp.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.utility.Constants.TAG
import kotlinx.coroutines.Deferred

class FirestoreWeatherRepoImpl : IWeatherRepo {

    private val firestoreDb : FirebaseFirestore = FirebaseFirestore.getInstance()

    override suspend fun getCurrentWeatherByCity(city: String): Deferred<Weather> {
        TODO("Not yet implemented")
    }

    override suspend fun saveWeatherInfo(weatherInfo: Weather) {
        val dbBasePath = firestoreDb.collection("Weather").document("City")
        dbBasePath.collection(weatherInfo.cityName).document(weatherInfo.id.toString()).set(weatherInfo)
    }
}