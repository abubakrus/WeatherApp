package com.example.weatherapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.example.weatherapp.presentation.navigation.AppNavGraph
import com.example.weatherapp.presentation.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

private const val PERMISSIONS_ACTIVITY_KEY = "PERMISSIONS_ACTIVITY_KEY"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityResultRegistry.register(
            PERMISSIONS_ACTIVITY_KEY,
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            Log.i("AbuAcademy", "result = $result")
        }.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )

        setContent {
            WeatherAppTheme {
                AppNavGraph()
            }
        }
    }
}

