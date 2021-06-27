package com.palmah.myweatherapp.entity

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(val cityName: String, val id:Int, val temp: Double, val minTemp: Double, val maxTemp: Double,
                    val pressure : Int, val humidity :Int, val weatherDescription : String, val windSpeed : Double,
                    val windDegree : Int, val visibility : Int, val country : String, val timeStamp : Long, var favorite : Boolean) :Parcelable
{
    constructor(): this("",-1,-1.0,-1.0,-1.0,-1,-1,"",-1.0,
              -1,-1,"",-1L,false)
    @get:Exclude
    var formattedTime : String = ""
    @get:Exclude
    var formattedPressure : String = ""
    @get:Exclude
    var formattedHumidity : String = ""
    @get:Exclude
    var formattedWindSpeed : String = ""
    @get:Exclude
    var formattedTemp : String = ""
    @get:Exclude
    var formattedMinTemp : String = ""
    @get:Exclude
    var formattedMaxTemp : String = ""
    @get:Exclude
    var formattedVisibility : String = ""

}
