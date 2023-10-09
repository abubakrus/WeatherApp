package com.example.weatherapp.presentation.models

data class WeatherUi(
    val currentWeather: WeatherDayInfoUi,
    val otherWeathers: List<WeatherDayInfoUi>
) {
    fun isUnKnown() = this == unknown


    companion object {
        val unknown = WeatherUi(
            currentWeather = WeatherDayInfoUi.unknown,
            otherWeathers = listOf(WeatherDayInfoUi.unknown)
        )
    }
}