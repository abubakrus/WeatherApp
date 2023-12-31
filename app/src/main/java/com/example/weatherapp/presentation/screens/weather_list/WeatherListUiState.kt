package com.example.weatherapp.presentation.screens.weather_list

import com.example.weatherapp.presentation.models.CountryInfo
import com.example.weatherapp.presentation.models.WeatherDayInfoUi

sealed class WeatherListUiState {
    object Loading : WeatherListUiState()

    data class Loaded(
        val dailyWeathers: List<WeatherDayInfoUi>,
        val countryInfo: CountryInfo
    ) : WeatherListUiState()

    data class Error(
        val message: String
    ) : WeatherListUiState()
}