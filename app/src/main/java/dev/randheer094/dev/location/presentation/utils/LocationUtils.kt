package dev.randheer094.dev.location.presentation.utils

import android.location.Location
import android.location.LocationManager
import android.location.provider.ProviderProperties
import android.os.Build
import android.os.SystemClock

class LocationUtils {
    companion object {
        const val DEFAULT_ACCURACY = 5f
        const val DEFAULT_BEARING = 0f
        const val DEFAULT_ALTITUDE = 0.0
        const val DEFAULT_SPEED = 0f
        const val BEARING_ACCURACY = 10f
        const val VERTICAL_ACCURACY = 15f
        const val SPEED_ACCURACY = 0f
    }

    private val providers by lazy {
        buildSet {
            add(LocationManager.GPS_PROVIDER)
            add(LocationManager.NETWORK_PROVIDER)
        }
    }

    fun addMockProvider(locationManager: LocationManager): Boolean {
        return try {
            with(locationManager) {
                providers.forEach {
                    addTestProvider(
                        it,
                        false, false, false, false,
                        false, true, true,
                        getPowerUsageProperty(),
                        getAccuracyProperty()
                    )
                    setTestProviderEnabled(it, true)
                }
            }
            true
        } catch (@Suppress("SwallowedException") e: SecurityException) {
            false
        }
    }

    fun removeMockProvider(locationManager: LocationManager): Boolean {
        return try {
            providers.forEach {
                locationManager.removeTestProvider(it)
            }
            true
        } catch (@Suppress("SwallowedException") e: SecurityException) {
            false
        }
    }

    fun setMockLocation(
        locationManager: LocationManager,
        lat: Double,
        lng: Double
    ) {
        providers.forEach {
            val location = Location(it).apply {
                latitude = lat
                longitude = lng
                accuracy = DEFAULT_ACCURACY
                bearing = DEFAULT_BEARING
                altitude = DEFAULT_ALTITUDE
                speed = DEFAULT_SPEED
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    bearingAccuracyDegrees = BEARING_ACCURACY
                    verticalAccuracyMeters = VERTICAL_ACCURACY
                    speedAccuracyMetersPerSecond = SPEED_ACCURACY
                }
                time = System.currentTimeMillis()
                elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
            }
            locationManager.setTestProviderLocation(it, location)
        }
    }

    private fun getPowerUsageProperty() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ProviderProperties.POWER_USAGE_LOW
    } else {
        1 // PRE_S_POWER_USAGE_LOW
    }

    private fun getAccuracyProperty() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ProviderProperties.ACCURACY_FINE
    } else {
        0 // PRE_S_ACCURACY_FINE
    }
}