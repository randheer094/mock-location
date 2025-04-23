package dev.randheer094.dev.location.presentation.service

import android.app.Service
import android.content.Intent
import android.location.LocationManager
import android.os.Binder
import android.os.IBinder
import dev.randheer094.dev.location.presentation.utils.LocationUtils
import dev.randheer094.dev.location.presentation.utils.NotificationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

interface IMockLocationService {
    fun startMocking(lat: Double, long: Double)
    fun stopMocking()
    fun isMocking(): Boolean
}


class MockLocationService : Service(), IMockLocationService {
    private val notificationUtils by inject<NotificationUtils>()
    private val locationUtils by inject<LocationUtils>()
    private val locationManager by inject<LocationManager>()
    private var job: Job? = null

    companion object {
        private const val MOCK_LOCATION_UPDATE_INTERVAL_MS = 2000L
    }

    inner class LocalBinder : Binder() {
        fun getService(): IMockLocationService = this@MockLocationService
    }

    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val intent = Intent(applicationContext, MockLocationService::class.java).apply {
            action = rootIntent?.action
        }
        startService(intent)
        super.onTaskRemoved(rootIntent)
    }

    override fun startMocking(lat: Double, long: Double) {
        notificationUtils.createNotificationChannel()
        startForeground(
            NotificationUtils.NOTIFICATION_ID,
            notificationUtils.createForegroundNotification(lat, long)
        )
        startPeriodicUpdates(lat, long)
    }

    override fun stopMocking() {
        job?.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun isMocking(): Boolean = job?.isActive == true

    private fun startPeriodicUpdates(lat: Double, long: Double) {
        job = CoroutineScope(IO).launch {
            while (isActive) {
                locationUtils.setMockLocation(locationManager, lat, long)
                delay(MOCK_LOCATION_UPDATE_INTERVAL_MS)
            }
        }
    }
}