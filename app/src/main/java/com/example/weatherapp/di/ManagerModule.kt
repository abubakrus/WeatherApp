package com.example.weatherapp.di

import android.app.Application
import com.example.weatherapp.domain.managers.LocationTrackerManager
import com.example.weatherapp.domain.managers.LocationTrackerManagerImpl
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ManagerModule {
    @Provides
    fun providesLocationTrackerManager(
        application: Application
    ):LocationTrackerManager = LocationTrackerManagerImpl(
        application = application,
        locationClient = LocationServices.getFusedLocationProviderClient(application)
    )
}