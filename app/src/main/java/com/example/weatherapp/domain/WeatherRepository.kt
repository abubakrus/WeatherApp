package com.example.weatherapp.domain

import com.example.weatherapp.domain.models.WeatherDomain

interface WeatherRepository {
    suspend fun fetchWeatherFor16Day(
        longitude: String,
        latitude: String
    ): WeatherDomain
}
