package dev.randheer094.dev.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.location.provider.ProviderProperties
import android.os.Build
import android.os.SystemClock
import android.widget.Toast
import androidx.core.app.NotificationCompat

class MockLocationService : Service() {
    private lateinit var locationManager: LocationManager
    private val channelId = "location_mock_channel"

    companion object {
        private const val DEFAULT_ACCURACY = 5f
        private const val PRE_S_POWER_USAGE_LOW = 1
        private const val PRE_S_ACCURACY_FINE = 0
        private const val NOTIFICATION_ID = 1
        private const val DEFAULT_LATITUDE = 0.0
        private const val DEFAULT_LONGITUDE = 0.0
        private const val DEFAULT_BEARING = 0f
        private const val DEFAULT_ALTITUDE = 0.0
        private const val DEFAULT_SPEED = 0f
        private const val BEARING_ACCURACY = 10f
        private const val VERTICAL_ACCURACY = 15f
        private const val SPEED_ACCURACY = 0f
    }

    override fun onBind(intent: Intent?) = null

    override fun onCreate() {
        super.onCreate()
        locationManager = getSystemService(LocationManager::class.java)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Location Mocking",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Running location mock service"
            }
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Mocking Location")
            .setContentText("Active mock location service")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(NOTIFICATION_ID, notification)

        if (setupMockProvider() && intent != null) {
            val lat = intent.getDoubleExtra("latitude", DEFAULT_LATITUDE)
            val lng = intent.getDoubleExtra("longitude", DEFAULT_LONGITUDE)
            updateMockLocation(lat, lng)
        }
        return START_STICKY
    }

    private fun setupMockProvider(): Boolean {
        return try {
            with(locationManager) {
                addTestProvider(
                    LocationManager.GPS_PROVIDER,
                    /* requiresNetwork = */ false,
                    /* requiresSatellite = */ false,
                    /* requiresCell = */ false,
                    /* hasMonetaryCost = */ false,
                    /* supportsAltitude = */ false,
                    /* supportsSpeed = */ true,
                    /* supportsBearing = */ true,
                    getPowerUsageProperty(),
                    getAccuracyProperty()
                )
                setTestProviderEnabled(LocationManager.GPS_PROVIDER, true)
            }
            true
        } catch (@Suppress("SwallowedException") e: SecurityException) {
            showMockLocationError()
            stopSelf() // Stop service if permissions aren't granted
            false
        }
    }

    private fun getPowerUsageProperty() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ProviderProperties.POWER_USAGE_LOW
    } else {
        PRE_S_POWER_USAGE_LOW
    }

    private fun getAccuracyProperty() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ProviderProperties.ACCURACY_FINE
    } else {
        PRE_S_ACCURACY_FINE
    }

    private fun showMockLocationError() {
        Toast.makeText(
            this,
            "Enable Mock Locations: Developer Options → Select Mock Location App",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun updateMockLocation(lat: Double, lng: Double) {
        Location(LocationManager.GPS_PROVIDER).apply {
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
            locationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, this)
        }
    }
}