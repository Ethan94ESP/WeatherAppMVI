package com.ethan.weatherappmvi.data.mappers

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.ethan.weatherappmvi.data.remote.WeatherDataDto
import com.ethan.weatherappmvi.data.remote.WeatherDto
import com.ethan.weatherappmvi.domain.weather.WeatherData
import com.ethan.weatherappmvi.domain.weather.WeatherInfo
import com.ethan.weatherappmvi.domain.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)

fun WeatherDataDto.toWeatherDataMap(place: String): Map<Int, List<WeatherData>> {
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeeds[index]
        val pressure = pressures[index]
        val humidity = humidities[index]
        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperatureCelsius = temperature,
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity,
                place = place,
                weatherType = WeatherType.fromWMO(weatherCode)
            )
        )
    }.groupBy {
        it.index / 24
    }.mapValues {
        it.value.map { it.data }
    }
}

fun WeatherDto.toWeatherInfo(lat: Double, long: Double, context: Context): WeatherInfo {
    val geocoder = Geocoder(context, Locale.getDefault())
    var place =""
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        geocoder.getFromLocation(lat,long,1,object : Geocoder.GeocodeListener{
            override fun onGeocode(addresses: MutableList<Address>) {

                place = addresses.get(0).getAddressLine(0)
                // code
            }
            override fun onError(errorMessage: String?) {
                super.onError(errorMessage)

            }

        })
    }else{
        val addresses = geocoder.getFromLocation(lat, long, 1)
        place = addresses?.get(0)?.getAddressLine(0).toString()
    }
    val weatherDataMap = weatherData.toWeatherDataMap(place)
    val now = LocalDateTime.now()
    val currentWeatherData = weatherDataMap[0]?.find {
        val hour = if(now.minute < 30)
            now.hour
        else if(now.hour == 23)
            00.00
        else
            now.hour+1

        it.time.hour == hour
    }
    return WeatherInfo(
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentWeatherData
    )
}