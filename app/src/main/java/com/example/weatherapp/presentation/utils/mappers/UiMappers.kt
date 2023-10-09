package com.example.weatherapp.presentation.utils.mappers

import com.example.weatherapp.domain.models.WeatherDayInfoDomain
import com.example.weatherapp.domain.models.WeatherDomain
import com.example.weatherapp.domain.models.WeatherHourInfoDomain
import com.example.weatherapp.presentation.models.WeatherConditionType
import com.example.weatherapp.presentation.models.WeatherDayInfoUi
import com.example.weatherapp.presentation.models.WeatherHourInfoUi
import com.example.weatherapp.presentation.models.WeatherUi

fun WeatherHourInfoDomain.toUi(): WeatherHourInfoUi {
    return this.run {
        WeatherHourInfoUi(
            temperature = temperature,
            weatherConditionType = WeatherConditionType.findWeatherTypeByCode(weatherCode),
            windSpeed = windSpeed,
            date = date
        )
    }
}

fun WeatherDayInfoDomain.toUi(): WeatherDayInfoUi {
    return if (this == WeatherDayInfoDomain.unknown)
        WeatherDayInfoUi.unknown
    else this.run {
        WeatherDayInfoUi(
            temperature = temperature,
            weatherConditionType = WeatherConditionType.findWeatherTypeByCode(weatherCode),
            windSpeed = windSpeed,
            time = time,
            hourlyWeathers = hourlyWeathers.map { it.toUi() }
        )
    }
}

fun WeatherDomain.toUi(): WeatherUi {
    return if (this.isUnKnown()) WeatherUi.unknown
    else WeatherUi(
        currentWeather = currentWeather.toUi(),
        otherWeathers = otherWeathers.map { it.toUi() }
    )
}