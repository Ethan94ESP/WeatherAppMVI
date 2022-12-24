package com.ethan.weatherappmvi.domain.repository

import android.content.Context
import com.ethan.weatherappmvi.domain.util.Resource
import com.ethan.weatherappmvi.domain.weather.WeatherInfo

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, long: Double, context: Context): Resource<WeatherInfo>
}