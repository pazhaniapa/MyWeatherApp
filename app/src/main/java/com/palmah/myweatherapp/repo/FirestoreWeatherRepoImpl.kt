package com.palmah.myweatherapp.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.palmah.myweatherapp.entity.Weather
import com.palmah.myweatherapp.utility.Constants.CITY_COLLECTION
import com.palmah.myweatherapp.utility.Constants.CURRENT_WEATHER_DATA_DOCUMENT
import com.palmah.myweatherapp.utility.Constants.TAG
import com.palmah.myweatherapp.utility.Constants.WEATHER_COLLECTION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirestoreWeatherRepoImpl : IWeatherRepo {

    private val firestoreDb : FirebaseFirestore = FirebaseFirestore.getInstance()

    private val favoriteField = "favorite"

    override suspend fun getCurrentWeatherByCity(city: String): Deferred<Weather?> {
        return CoroutineScope(Dispatchers.IO).async {
            suspendCoroutine { continuation ->
                val dbBasePath = firestoreDb.collection(WEATHER_COLLECTION).document(
                    CURRENT_WEATHER_DATA_DOCUMENT)

                dbBasePath.collection(CITY_COLLECTION).document(city).get(Source.CACHE).addOnSuccessListener { cityWeatherDocumentSnapshot->
                val weather = cityWeatherDocumentSnapshot.toObject(Weather::class.java)
                continuation.resume(weather)

                }.addOnFailureListener {
                    Log.d(TAG,it.toString())
                    continuation.resume(null)
                }
            }
        }
    }

    override suspend fun saveWeatherInfo(weatherInfo: Weather) {
        val dbBasePath = firestoreDb.collection(WEATHER_COLLECTION).document(
            CURRENT_WEATHER_DATA_DOCUMENT).collection(CITY_COLLECTION)
        dbBasePath.document(weatherInfo.cityName).set(weatherInfo)
    }

    override suspend fun getFavoriteCitiesWeatherInfoList(): Deferred<ArrayList<Weather>> {
        return CoroutineScope(Dispatchers.IO).async {
            suspendCoroutine { continuation->
                val favoriteCityWeatherInfoList = ArrayList<Weather>()
                val dbBasePath = firestoreDb.collection(WEATHER_COLLECTION).document(
                    CURRENT_WEATHER_DATA_DOCUMENT)
                val cityCollection = dbBasePath.collection(CITY_COLLECTION)
                cityCollection.whereEqualTo(favoriteField,true).get().addOnSuccessListener { cityQuerySnapshot->
                    cityQuerySnapshot.documents.forEach { weatherDocument->
                        val weather = weatherDocument.toObject(Weather::class.java)
                        weather?.let {
                            favoriteCityWeatherInfoList.add(weather)
                        }

                    }
                    continuation.resume(favoriteCityWeatherInfoList)
                }.addOnFailureListener {
                    Log.e(TAG,it.toString())
                    continuation.resume(favoriteCityWeatherInfoList)
                }
            }
        }
    }

    override suspend fun getAllCities(): Deferred<ArrayList<String>> {
        return CoroutineScope(Dispatchers.IO).async {
            suspendCoroutine { continuation->
                val cityList = ArrayList<String>()
                val dbBasePath = firestoreDb.collection(WEATHER_COLLECTION).document(
                    CURRENT_WEATHER_DATA_DOCUMENT)
                val cityCollection = dbBasePath.collection(CITY_COLLECTION)
                cityCollection.get().addOnSuccessListener { cityQuerySnapshot->
                    cityQuerySnapshot.documents.forEach { weatherDocument->
                        val weather = weatherDocument.toObject(Weather::class.java)
                        weather?.let {
                            cityList.add(weather.cityName)
                        }

                    }
                    continuation.resume(cityList)
                }.addOnFailureListener {
                    Log.e(TAG,it.toString())
                    continuation.resume(cityList)
                }
            }
        }
    }
}