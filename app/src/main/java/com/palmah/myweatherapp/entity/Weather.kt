package com.palmah.myweatherapp.entity

data class Weather(val cityName: String, val id:Int, val temp: Double, val minTemp: Double, val maxTemp: Double,
                    val pressure : Int, val humidity :Int, val weatherDescription : String, val windSpeed : Double,
                    val windDegree : Int, val country : String, val timeStamp : Long, val favorite : Boolean)
{
    constructor(): this("",-1,-1.0,-1.0,-1.0,-1,-1,"",-1.0,
              -1,"",-1L,false)

}
