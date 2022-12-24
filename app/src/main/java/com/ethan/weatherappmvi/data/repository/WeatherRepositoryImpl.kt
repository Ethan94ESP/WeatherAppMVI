package com.ethan.weatherappmvi.data.repository

import android.content.Context
import com.ethan.weatherappmvi.data.mappers.toWeatherInfo
import com.ethan.weatherappmvi.data.remote.WeatherApi
import com.ethan.weatherappmvi.domain.repository.WeatherRepository
import com.ethan.weatherappmvi.domain.util.Resource
import com.ethan.weatherappmvi.domain.weather.WeatherInfo
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
): WeatherRepository {

    override suspend fun getWeatherData(lat: Double, long: Double, context: Context): Resource<WeatherInfo> {
        return try {
            Resource.Success(
                data = api.getWeatherData(
                    lat = lat,
                    long = long
                ).toWeatherInfo(lat, long, context)
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}