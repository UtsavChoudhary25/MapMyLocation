package com.example.mapmylocation.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import androidx.core.content.ContextCompat
import com.example.mapmylocation.data.models.LocationData
import com.example.mapmylocation.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.Locale

class LocationManager(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val _currentLocation = MutableStateFlow(
        LocationData(
            latitude = Constants.DEFAULT_LAT,
            longitude = Constants.DEFAULT_LNG,
            name = "San Francisco",
            city = Constants.DEFAULT_CITY
        )
    )
    val currentLocation: StateFlow<LocationData> = _currentLocation.asStateFlow()

    fun hasLocationPermission(): Boolean {
        val finePermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarsePermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return finePermission || coarsePermission
    }

    @SuppressLint("MissingPermission")
    suspend fun fetchCurrentLocation(): LocationData = withContext(Dispatchers.IO) {
        if (!hasLocationPermission()) {
            return@withContext _currentLocation.value
        }

        try {
            val cancellationTokenSource = CancellationTokenSource()
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                cancellationTokenSource.token
            ).addOnSuccessListener { location ->
                if (location != null) {
                    val cityName = getCityNameFromCoordinates(location.latitude, location.longitude)
                    val newLocation = LocationData(
                        latitude = location.latitude,
                        longitude = location.longitude,
                        name = "Current Location",
                        city = cityName,
                        isCurrentLocation = true
                    )
                    _currentLocation.value = newLocation
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext _currentLocation.value
    }

    private fun getCityNameFromCoordinates(lat: Double, lng: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                addresses?.firstOrNull()?.locality
                    ?: addresses?.firstOrNull()?.subAdminArea
                    ?: addresses?.firstOrNull()?.adminArea
                    ?: "San Francisco"
            } else {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                addresses?.firstOrNull()?.locality
                    ?: addresses?.firstOrNull()?.subAdminArea
                    ?: addresses?.firstOrNull()?.adminArea
                    ?: "San Francisco"
            }
        } catch (e: Exception) {
            "San Francisco"
        }
    }
}
