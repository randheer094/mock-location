package dev.randheer094.dev.location.presentation.service

import android.app.Service
import android.content.Intent
import android.location.LocationManager
import android.os.Binder
import android.os.IBinder
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
        private const val MOCK_LOCATION_UPDATE_INTERVAL_MS = 1000L
    }

    inner class LocalBinder : Binder() {
        fun getService(): IMockLocationService = this@MockLocationService
    }

    private val binder = LocalBinder()

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        val intent = Intent(applicationContext, MockLocationService::class.java).apply {
            action = rootIntent?.action
        }
        startService(intent)
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
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    override fun isMocking(): Boolean = job?.isActive == true

    private fun startPeriodicUpdates(location: MockLocation) {
        job = serviceScope.launch {
            while (isActive) {
                locationUtils.setMockLocation(locationManager, location.lat, location.long)
                delay(MOCK_LOCATION_UPDATE_INTERVAL_MS)
            }
        }
    }
}
