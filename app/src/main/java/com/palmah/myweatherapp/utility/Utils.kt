package com.palmah.myweatherapp.utility

import kotlin.math.round

object Utils {
    /**
     * @param kelvin - temperature in kelvin.
     * This methods converts the temperature value from kelvin to celcius
     * Returns the temperature value in celcius.
     */
    fun convertToCelcius(kelvin : Double) : Double{
        return kelvin - 273.15
    }

    /**
     * @param meter - value in meter unit.
     * This methods converts meters to kilometers.
     * Returns the kilometer value of the given meter.
     */
    fun convertToKm(meter : Int) : Int{
        return meter/1000
    }

    /**
     * @param meterPerSecond - speed in meter per second
     * This method converts the given value to kilometer per hour.
     * Returns kilometer per hour value of the given meter per second.
     */
    fun convertToKmph(meterPerSecond : Double) : Int{
        return round (meterPerSecond * 3.6).toInt()
    }
}