package dev.randheer094.dev.location.presentation.utils

import android.location.Location
import android.location.LocationManager
import android.location.provider.ProviderProperties
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
        listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
    }

    fun addMockProvider(locationManager: LocationManager): Boolean = runCatching {
        providers.forEach {
            locationManager.addTestProvider(
                it,
                false, false, false, false,
                false, true, true,
                ProviderProperties.POWER_USAGE_LOW,
                ProviderProperties.ACCURACY_FINE
            )
            locationManager.setTestProviderEnabled(it, true)
        }
    }.isSuccess

    fun removeMockProvider(locationManager: LocationManager): Boolean = runCatching {
        providers.forEach(locationManager::removeTestProvider)
    }.isSuccess

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
                bearingAccuracyDegrees = BEARING_ACCURACY
                verticalAccuracyMeters = VERTICAL_ACCURACY
                speedAccuracyMetersPerSecond = SPEED_ACCURACY
                time = System.currentTimeMillis()
                elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos()
            }
            locationManager.setTestProviderLocation(it, location)
        }
    }
}
