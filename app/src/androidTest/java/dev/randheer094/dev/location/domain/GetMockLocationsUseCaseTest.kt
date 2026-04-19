package dev.randheer094.dev.location.domain

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumentation test that reads the real `m_l.json` asset shipped with the APK, exercising
 * the asset-loading path that cannot be covered by a plain JVM test.
 */
@RunWith(AndroidJUnit4::class)
class GetMockLocationsUseCaseTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun bundled_asset_parses_into_a_non_empty_list_of_mock_locations() = runBlocking {
        val useCase = GetMockLocationsUseCase(context, json)

        val locations = useCase.execute().first()

        assertFalse("Bundled m_l.json should not be empty", locations.isEmpty())
        locations.forEach { location ->
            assertTrue("Name must be non-blank", location.name.isNotBlank())
            assertTrue(
                "Latitude out of range for ${location.name}",
                location.lat in -90.0..90.0,
            )
            assertTrue(
                "Longitude out of range for ${location.name}",
                location.long in -180.0..180.0,
            )
        }
    }

    @Test
    fun repeated_calls_are_cached_and_return_the_same_instance() = runBlocking {
        val useCase = GetMockLocationsUseCase(context, json)

        val first = useCase.execute().first()
        val second = useCase.execute().first()

        assertEquals(first.size, second.size)
        // GetMockLocationsUseCase caches the parsed list, so the same reference is re-emitted.
        assertTrue(
            "Cached list should be re-used across calls",
            first === second,
        )
    }
}
