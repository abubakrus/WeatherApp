package com.example.weatherapp.data.remote

import com.example.weatherapp.data.models.WeatherRemote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServer {

    @GET("/v1/forecast?hourly=temperature_2m,weathercode,windspeed_10m&daily=weathercode,windspeed_10m_max&timezone=auto&forecast_days=16")
    suspend fun fetchWeatherFor16Day(
        @Query("latitude") latitude: String,
        @Query("longitude") longitude: String
    ): Response<WeatherRemote>

}