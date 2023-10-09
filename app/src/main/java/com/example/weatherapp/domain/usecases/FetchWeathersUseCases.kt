package com.example.weatherapp.domain.usecases

import com.example.weatherapp.domain.models.WeatherDomain
import com.example.weatherapp.presentation.models.CountryInfo

interface FetchWeathersUseCases {

    suspend operator fun invoke(): Pair<WeatherDomain, CountryInfo>
}