package dev.randheer094.dev.location.presentation.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import dev.randheer094.dev.location.R

class NotificationUtils(private val context: Context) {
    companion object {
        const val CHANNEL_ID = "mock_location_channel"
        const val NOTIFICATION_ID = 1
    }

    fun createNotificationChannel() {
        val name = "Mock Location Channel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance)

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    fun createForegroundNotification(lat: Double, long: Double): Notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setContentTitle("Mocking Location")
        .setContentText("Latitude: $lat, Longitude: $long")
        .setSmallIcon(R.drawable.ic_location)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setOngoing(true)
        .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
        .setSilent(true)
        .build()
}
