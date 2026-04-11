package dev.randheer094.dev.location.presentation.service

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import dev.randheer094.dev.location.domain.MockLocation

/**
 * Thin wrapper around [ContextCompat.startForegroundService] / [Context.stopService] so the
 * ViewModel can promote the bound [MockLocationService] into a started (foreground) service
 * that outlives the UI.
 *
 * Binding alone is not enough: when the last client unbinds, Android tears the service down
 * and the mock-location loop is cancelled. Explicitly starting the service keeps it alive
 * until [stop] is called (or the user toggles mocking off).
 */
class MockLocationServiceStarter(private val context: Context) {

    fun start(location: MockLocation) {
        val intent = Intent(context, MockLocationService::class.java).apply {
            action = MockLocationService.ACTION_START
            putExtra(MockLocationService.EXTRA_NAME, location.name)
            putExtra(MockLocationService.EXTRA_LAT, location.lat)
            putExtra(MockLocationService.EXTRA_LONG, location.long)
        }
        ContextCompat.startForegroundService(context, intent)
    }

    fun stop() {
        val intent = Intent(context, MockLocationService::class.java).apply {
            action = MockLocationService.ACTION_STOP
        }
        context.startService(intent)
    }
}
