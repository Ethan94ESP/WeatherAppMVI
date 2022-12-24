package com.ethan.weatherappmvi.presentation

import com.ethan.weatherappmvi.domain.weather.WeatherInfo

data class WeatherState(
    val weatherInfo: WeatherInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
