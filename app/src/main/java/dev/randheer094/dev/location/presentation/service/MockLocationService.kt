package dev.randheer094.dev.location.presentation.service

import android.app.Service
import android.content.Intent
import android.location.LocationManager
import android.os.Binder
import android.os.IBinder
import android.util.Log
import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.presentation.utils.LocationUtils
import dev.randheer094.dev.location.presentation.utils.NotificationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

interface IMockLocationService {
    fun startMocking(location: MockLocation)
    fun stopMocking()
    fun isMocking(): Boolean
}


class MockLocationService : Service(), IMockLocationService {
    private val notificationUtils by inject<NotificationUtils>()
    private val locationUtils by inject<LocationUtils>()
    private val locationManager by inject<LocationManager>()
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var job: Job? = null

    companion object {
        private const val TAG = "MockLocationService"
        private const val MOCK_LOCATION_UPDATE_INTERVAL_MS = 1000L
        const val ACTION_START = "dev.randheer094.dev.location.action.START"
        const val ACTION_STOP = "dev.randheer094.dev.location.action.STOP"
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_LAT = "extra_lat"
        const val EXTRA_LONG = "extra_long"
    }

    inner class LocalBinder : Binder() {
        fun getService(): IMockLocationService = this@MockLocationService
    }

    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                // We MUST call startForeground within ~5 seconds of startForegroundService,
                // otherwise ForegroundServiceDidNotStartInTimeException is thrown. We always
                // show a placeholder notification first, then let [startMocking] replace it
                // with the real location-aware one.
                val lat = intent.getDoubleExtra(EXTRA_LAT, Double.NaN)
                val lng = intent.getDoubleExtra(EXTRA_LONG, Double.NaN)
                val name = intent.getStringExtra(EXTRA_NAME) ?: ""

                notificationUtils.createNotificationChannel()
                startForeground(
                    NotificationUtils.NOTIFICATION_ID,
                    notificationUtils.createForegroundNotification(
                        lat = lat.takeIf { !it.isNaN() } ?: 0.0,
                        long = lng.takeIf { !it.isNaN() } ?: 0.0,
                    ),
                )

                if (!lat.isNaN() && !lng.isNaN()) {
                    startMocking(MockLocation(name = name, lat = lat, long = lng))
                }
            }
            ACTION_STOP -> {
                stopMocking()
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        job?.cancel()
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        // Keep the service alive across task swipes so mocking continues after the user
        // closes the activity. onTaskRemoved is only invoked if android:stopWithTask="false".
        if (isMocking()) {
            val intent = Intent(applicationContext, MockLocationService::class.java).apply {
                action = rootIntent?.action
            }
            startService(intent)
        }
        super.onTaskRemoved(rootIntent)
    }

    override fun startMocking(location: MockLocation) {
        notificationUtils.createNotificationChannel()
        startForeground(
            NotificationUtils.NOTIFICATION_ID,
            notificationUtils.createForegroundNotification(location.lat, location.long)
        )
        startPeriodicUpdates(location)
    }

    override fun stopMocking() {
        job?.cancel()
        job = null
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun isMocking(): Boolean = job?.isActive == true

    private fun startPeriodicUpdates(location: MockLocation) {
        job?.cancel()
        job = serviceScope.launch(Dispatchers.IO) {
            while (isActive) {
                runCatching {
                    locationUtils.setMockLocation(locationManager, location.lat, location.long)
                }.onFailure {
                    // SecurityException can be thrown if the user removes this app as the
                    // mock location provider while we're running. Log and exit the loop so
                    // the service can be cleanly stopped by the UI/ViewModel.
                    Log.w(TAG, "Failed to push mock location, stopping loop", it)
                    return@launch
                }
                delay(MOCK_LOCATION_UPDATE_INTERVAL_MS)
            }
        }
    }
}
