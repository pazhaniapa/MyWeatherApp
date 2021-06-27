package com.palmah.myweatherapp.utility

import kotlin.math.round

object Utils {
    fun convertToCelcius(kelvin : Double) : Double{
        return kelvin - 273.15
    }

    fun convertToKm(meter : Int) : Int{
        return meter/1000
    }

    fun convertToKmph(meterPerSecond : Double) : Int{
        return round (meterPerSecond * 3.6).toInt()
    }
}