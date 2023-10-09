package com.example.weatherapp.domain.managers

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Snackbar
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.concurrent.BlockingQueue
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationTrackerManagerImpl(
    private val locationClient: FusedLocationProviderClient,
    private val application: Application

) : LocationTrackerManager {
    @OptIn(InternalCoroutinesApi::class)
    override suspend fun fetchCurrentLocation(): Location? {
        val isLocationPermissionGranted = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val isCoarseLocationPermissionGranted = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!isLocationPermissionGranted || !isCoarseLocationPermissionGranted) {
            showToast("Needed Location Permission!")
            return null
        }

        val cancellationTokenSource = CancellationTokenSource()

        val currentLocationTask: Task<Location> = locationClient.getCurrentLocation(
            PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        )

        return suspendCancellableCoroutine { cons ->
            currentLocationTask.addOnCompleteListener { task: Task<Location> ->
                val result = if (task.isSuccessful) {
                    val result: Location? = task.result
                    cons.resume(result)
                    "Location (success): ${result?.latitude}, ${result?.longitude}"
                } else {
                    val exception = task.exception
                    cons.resume(null)
                    "Location (failure): $exception"
                }
                Log.d("Joseph", "getCurrentLocation() result: $result")
            }
        }
    }

    private suspend fun showToast(
        message: String
    ) {
        withContext(Dispatchers.Main) {
            Toast.makeText(application, message, Toast.LENGTH_SHORT).show()
        }
    }
}