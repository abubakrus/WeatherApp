package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.domain.WeatherRepository
import com.example.weatherapp.domain.managers.LocationTrackerManager
import com.example.weatherapp.domain.usecases.FetchWeathersUseCases
import com.example.weatherapp.domain.usecases.FetchWeathersUseCasesImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCasesModule {
    @Provides
    fun providesFetchWeathersUseCases(
        @ApplicationContext context: Context,
        repository: WeatherRepository,
        locationTrackerManager: LocationTrackerManager
    ): FetchWeathersUseCases = FetchWeathersUseCasesImpl(
        repository = repository,
        locationTrackerManager = locationTrackerManager,
        context = context
    )
}