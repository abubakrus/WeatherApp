package com.example.weatherapp.data.repository

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.weatherapp.data.remote.WeatherServer
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.domain.models.WeatherDayInfoDomain
import com.example.weatherapp.domain.models.WeatherDomain
import com.example.weatherapp.domain.models.WeatherHourInfoDomain
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

data class WeatherDataInfo(
    val date: Date,
    val temperature: Double,
    val weatherCode: Int,
    val windSpeed: Double,
)

class WeatherRepositoryImpl @Inject constructor(
    private val weatherService: WeatherServer
) : WeatherRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun fetchWeatherFor16Day(
        longitude: String,
        latitude: String
    ): WeatherDomain {
        try {
            val weatherResponse = weatherService.fetchWeatherFor16Day(
                longitude = longitude,
                latitude = latitude
            )
            val weatherRemote = weatherResponse.body() ?: return WeatherDomain.unknown
            val allWeatherByDay = weatherRemote.hourly.time.mapIndexed { index, time ->

                val temperature = if (weatherRemote.hourly.temperature.size > index) {
                    weatherRemote.hourly.temperature[index]
                } else {
                    0.0
                }
                val weatherCode = if (weatherRemote.hourly.weatherCode.size > index) {
                    weatherRemote.hourly.weatherCode[index]
                } else {
                    -1
                }
                val windSpeed = if (weatherRemote.hourly.windSpeed.size > index) {
                    weatherRemote.hourly.windSpeed[index]
                } else {
                    0.0
                }
                val localDate = LocalDateTime.parse(time)
                val date = Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant())
                WeatherDataInfo(
                    date = date,
                    temperature = temperature,
                    weatherCode = weatherCode,
                    windSpeed = windSpeed
                )
            }.groupBy { it.date.formatAsDayMonthYear() }
            val currentDayKey = allWeatherByDay.keys
                .firstOrNull { it == Date().formatAsDayMonthYear() }
            Log.i("Joseph", "currentDayKey = ${currentDayKey}}")

            val info = allWeatherByDay[currentDayKey]

            Log.i("Joseph", "info = ${info?.map { Pair(it.date.hours, it.weatherCode) }}")

            val currentWeather = WeatherDayInfoDomain(
                temperature = info?.map { it.temperature }?.firstOrNull() ?: 0.0,
                weatherCode = info?.map { it.weatherCode }?.firstOrNull() ?: -1,
                windSpeed = info?.map { it.windSpeed }?.firstOrNull() ?: 0.0,
                time = currentDayKey.toString(),
                hourlyWeathers = info?.map(::weatherDataInfoToDomain) ?: emptyList()
            )
            val weatherFor15Days = allWeatherByDay.map { weatherInfo ->
                WeatherDayInfoDomain(
                    temperature = weatherInfo.value.map { it.temperature }.firstOrNull() ?: 0.0,
                    weatherCode = weatherInfo.value.map { it.weatherCode }.firstOrNull() ?: -1,
                    windSpeed = weatherInfo.value.map { it.windSpeed }.firstOrNull() ?: 0.0,
                    time = weatherInfo.key,
                    hourlyWeathers = weatherInfo.value.map(::weatherDataInfoToDomain)
                )
            }
            return WeatherDomain(
                currentWeather = currentWeather,
                otherWeathers = weatherFor15Days
            )
        } catch (e: Throwable) {
            Log.e("AbuAcademy", e.message.toString())
            return WeatherDomain.unknown
        }
    }

    private fun weatherDataInfoToDomain(
        weatherDataInfo: WeatherDataInfo
    ): WeatherHourInfoDomain = weatherDataInfo.run {
        WeatherHourInfoDomain(
            temperature = temperature,
            weatherCode = weatherCode,
            windSpeed = windSpeed,
            date = date
        )
    }
}

fun Date.formatAsDayMonthYear(): String {
    val format = SimpleDateFormat("dd.MM.yyyy", Locale.US)
    format.timeZone = TimeZone.getTimeZone("GMT")
    return format.format(
        this
    )
}