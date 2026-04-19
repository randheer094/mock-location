package dev.randheer094.dev.location.presentation.utils

import android.content.Context
import android.location.LocationManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Smoke tests for [LocationUtils]. The app under test has not been granted the system
 * "Select mock location app" privilege on instrumentation, so `addTestProvider` is expected
 * to throw — we assert on the safe boolean return rather than attempting to verify provider
 * enablement.
 */
@RunWith(AndroidJUnit4::class)
class LocationUtilsTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val utils = LocationUtils()

    @Test
    fun addMockProvider_returns_false_when_app_is_not_the_system_mock_location_provider() {
        val result = utils.addMockProvider(locationManager)
        // Without Settings → Developer Options → "Select mock location app" pointing at the
        // debug build, addTestProvider throws SecurityException. The utility wraps that into
        // a false result so callers can surface setup instructions instead of crashing.
        assertFalse("Expected false without mock-location privilege", result)
    }

    @Test
    fun removeMockProvider_returns_false_when_providers_were_never_registered() {
        val result = utils.removeMockProvider(locationManager)
        assertFalse("Expected false when no test providers are registered", result)
    }
}
