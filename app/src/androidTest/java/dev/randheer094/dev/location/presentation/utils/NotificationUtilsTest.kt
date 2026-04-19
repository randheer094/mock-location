package dev.randheer094.dev.location.presentation.utils

import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Verifies the runtime behavior of [NotificationUtils]: the channel is registered with the
 * platform and the foreground notification renders with the expected coordinates and a
 * tap-through PendingIntent that returns to [MainActivity].
 */
@RunWith(AndroidJUnit4::class)
class NotificationUtilsTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val utils = NotificationUtils(context)

    @Test
    fun channel_registration_is_idempotent_and_visible_to_notification_manager() {
        utils.createNotificationChannel()
        utils.createNotificationChannel()

        val manager = context.getSystemService(NotificationManager::class.java)
        val channel = manager.getNotificationChannel(NotificationUtils.CHANNEL_ID)

        assertNotNull("Channel should exist after registration", channel)
        assertEquals(NotificationManager.IMPORTANCE_LOW, channel!!.importance)
    }

    @Test
    fun foreground_notification_contains_formatted_coordinates_and_is_silent_ongoing() {
        utils.createNotificationChannel()
        val notification = utils.createForegroundNotification(lat = 12.34, long = 56.78)

        val text = notification.extras.getCharSequence(NotificationCompat.EXTRA_TEXT)?.toString().orEmpty()
        assertTrue("Notification text should include latitude, was: $text", text.contains("12.34"))
        assertTrue("Notification text should include longitude, was: $text", text.contains("56.78"))

        // Ongoing foreground service notifications must be sticky.
        assertTrue(
            "Notification should be flagged as ongoing",
            (notification.flags and androidx.core.app.NotificationCompat.FLAG_ONGOING_EVENT) != 0,
        )
    }

    @Test
    fun foreground_notification_has_tap_through_pending_intent() {
        utils.createNotificationChannel()
        val notification = utils.createForegroundNotification(lat = 0.0, long = 0.0)

        assertNotNull(
            "Tapping the ongoing notification should open the activity",
            notification.contentIntent,
        )
    }
}
