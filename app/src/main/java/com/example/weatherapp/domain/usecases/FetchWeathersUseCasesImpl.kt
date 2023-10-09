package com.example.weatherapp.domain.usecases

import android.content.Context
import android.location.Geocoder
import androidx.compose.ui.graphics.vector.Path
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.domain.managers.LocationTrackerManager
import com.example.weatherapp.domain.models.WeatherDomain
import com.example.weatherapp.presentation.models.CountryInfo
import java.util.Locale

class FetchWeathersUseCasesImpl(
    private val context: Context,
    private val locationTrackerManager: LocationTrackerManager,
    private val repository: WeatherRepository
) : FetchWeathersUseCases {

    override suspend fun invoke(): Pair<WeatherDomain, CountryInfo> {
        return try {
            val location = locationTrackerManager.fetchCurrentLocation()
                ?: return Pair(WeatherDomain.unknown, CountryInfo.unknown)
            val geocoder = Geocoder(context, Locale.getDefault())
            val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val cityName = address?.get(0)?.locality ?: "Unknown"
            val countyName = address?.get(0)?.countryName ?: "Unknown"

            val weather = repository.fetchWeatherFor16Day(
                longitude = location.longitude.toString(),
                latitude = location.latitude.toString()
            )
            Pair(
                weather,
                CountryInfo(
                    countryName = countyName,
                    cityName = cityName
                )
            )
        } catch (e: Throwable) {
            Pair(WeatherDomain.unknown, CountryInfo.unknown)
        }
    }
}