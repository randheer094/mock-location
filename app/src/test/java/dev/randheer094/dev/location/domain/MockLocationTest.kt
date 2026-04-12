package dev.randheer094.dev.location.domain

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pure JVM unit tests for the [MockLocation] model. Covers the @Serializable contract that
 * the app relies on for DataStore persistence and the `m_l.json` bundled asset.
 */
class MockLocationTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `mock location holds the supplied values`() {
        val location = MockLocation(name = "Paris", lat = 48.8566, long = 2.3522)

        assertEquals("Paris", location.name)
        assertEquals(48.8566, location.lat, 0.0)
        assertEquals(2.3522, location.long, 0.0)
    }

    @Test
    fun `data class equality is based on all fields`() {
        val a = MockLocation(name = "Paris", lat = 48.8566, long = 2.3522)
        val b = MockLocation(name = "Paris", lat = 48.8566, long = 2.3522)
        val c = MockLocation(name = "Paris", lat = 48.8566, long = 2.3523)

        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
        assertNotEquals(a, c)
    }

    @Test
    fun `serializes to json with the short field names used on disk`() {
        val location = MockLocation(name = "London", lat = 51.5074, long = -0.1278)

        val encoded = json.encodeToString(location)

        assertTrue("name field should be present: $encoded", encoded.contains("\"name\""))
        assertTrue("lat field should be present: $encoded", encoded.contains("\"lat\""))
        assertTrue("long field should be present: $encoded", encoded.contains("\"long\""))
        assertTrue("value should be present: $encoded", encoded.contains("London"))
    }

    @Test
    fun `deserializes from canonical json shape`() {
        val raw = "{\"name\":\"Tokyo\",\"lat\":35.6762,\"long\":139.6503}"

        val decoded = json.decodeFromString<MockLocation>(raw)

        assertEquals(MockLocation("Tokyo", 35.6762, 139.6503), decoded)
    }

    @Test
    fun `deserializes lists from the bundled asset shape`() {
        val raw = """
            [
              {"name":"Paris","lat":48.8566,"long":2.3522},
              {"name":"New York","lat":40.7128,"long":-74.006}
            ]
        """.trimIndent()

        val decoded = json.decodeFromString<List<MockLocation>>(raw)

        assertEquals(2, decoded.size)
        assertEquals("Paris", decoded[0].name)
        assertEquals(-74.006, decoded[1].long, 0.0)
    }

    @Test
    fun `ignores unknown json keys to stay forward-compatible`() {
        val raw = """
            {"name":"Berlin","lat":52.52,"long":13.405,"continent":"Europe"}
        """.trimIndent()

        val decoded = json.decodeFromString<MockLocation>(raw)

        assertEquals(MockLocation("Berlin", 52.52, 13.405), decoded)
    }

    @Test
    fun `json round trip preserves the value`() {
        val original = MockLocation(name = "Sydney", lat = -33.8688, long = 151.2093)

        val roundTripped = json.decodeFromString<MockLocation>(json.encodeToString(original))

        assertEquals(original, roundTripped)
    }

    @Test
    fun `negative coordinates survive serialization`() {
        val original = MockLocation(name = "Santiago", lat = -33.4489, long = -70.6693)

        val roundTripped = json.decodeFromString<MockLocation>(json.encodeToString(original))

        assertEquals(original, roundTripped)
    }
}
