package dev.randheer094.dev.location.presentation.mocklocation.state

import dev.randheer094.dev.location.domain.MockLocation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pure JVM unit tests for [UiStateMapper]. The mapper has no Android dependencies and is
 * effectively a pure function, which makes it a good fit for fast unit tests that cover all
 * the branches of UI-state construction.
 */
class UiStateMapperTest {

    private val london = MockLocation(name = "London", lat = 51.5074, long = -0.1278)
    private val newYork = MockLocation(name = "New York", lat = 40.7128, long = -74.006)
    private val tokyo = MockLocation(name = "Tokyo", lat = 35.6762, long = 139.6503)

    @Test
    fun `empty inputs produce the minimum two items and preserve flags`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = false,
            selected = null,
            locations = emptyList(),
            hasNotificationPermission = true,
            elapsedLabel = "",
        )

        assertFalse(state.showInstructions)
        assertFalse(state.status)
        assertTrue(state.hasNotificationPermission)
        // [status header, selected row] with no "Select locations" header because the list is
        // empty.
        assertEquals(2, state.items.size)
        assertTrue(state.items[0] is SectionHeader.MockLocationStatus)
        assertTrue(state.items[1] is MockLocationNStatus)
    }

    @Test
    fun `status header reflects on state`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = true,
            selected = null,
            locations = emptyList(),
            hasNotificationPermission = true,
            elapsedLabel = "",
        )

        val header = state.items.first() as SectionHeader.MockLocationStatus
        assertTrue(header.isOn)
        assertTrue(state.status)
    }

    @Test
    fun `status header reflects off state`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = false,
            selected = null,
            locations = emptyList(),
            hasNotificationPermission = true,
            elapsedLabel = "",
        )

        val header = state.items.first() as SectionHeader.MockLocationStatus
        assertFalse(header.isOn)
        assertFalse(state.status)
    }

    @Test
    fun `selected location is placed right after the status header`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = true,
            selected = london,
            locations = emptyList(),
            hasNotificationPermission = true,
            elapsedLabel = "",
        )

        val selectedRow = state.items[1] as MockLocationNStatus
        assertEquals(london, selectedRow.location)
        assertTrue(selectedRow.status)
    }

    @Test
    fun `null selection keeps a placeholder MockLocationNStatus with null location`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = true,
            status = false,
            selected = null,
            locations = emptyList(),
            hasNotificationPermission = false,
            elapsedLabel = "",
        )

        val selectedRow = state.items[1] as MockLocationNStatus
        assertNull(selectedRow.location)
        assertFalse(selectedRow.status)
    }

    @Test
    fun `non empty locations inserts select locations header before the list`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = false,
            selected = null,
            locations = listOf(london, newYork),
            hasNotificationPermission = true,
            elapsedLabel = "",
        )

        // [status header, MockLocationNStatus, SelectLocations, Location(london), Location(newYork)]
        assertEquals(5, state.items.size)
        assertSame(SectionHeader.SelectLocations, state.items[2])
    }

    @Test
    fun `empty locations list omits the select locations header`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = false,
            selected = london,
            locations = emptyList(),
            hasNotificationPermission = true,
            elapsedLabel = "",
        )

        assertFalse(state.items.any { it === SectionHeader.SelectLocations })
    }

    @Test
    fun `locations are mapped in order to Location items`() {
        val ordered = listOf(london, newYork, tokyo)

        val state = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = false,
            selected = null,
            locations = ordered,
            hasNotificationPermission = true,
            elapsedLabel = "",
        )

        val locationItems = state.items.filterIsInstance<Location>().map { it.location }
        assertEquals(ordered, locationItems)
    }

    @Test
    fun `show instructions flag is preserved`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = true,
            status = false,
            selected = null,
            locations = emptyList(),
            hasNotificationPermission = true,
            elapsedLabel = "",
        )

        assertTrue(state.showInstructions)
    }

    @Test
    fun `notification permission flag is preserved`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = false,
            selected = null,
            locations = emptyList(),
            hasNotificationPermission = false,
            elapsedLabel = "",
        )

        assertFalse(state.hasNotificationPermission)
    }

    @Test
    fun `full state includes all items in the expected order`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = true,
            selected = london,
            locations = listOf(newYork, tokyo),
            hasNotificationPermission = true,
            elapsedLabel = "",
        )

        val items = state.items
        assertEquals(5, items.size)
        assertTrue(items[0] is SectionHeader.MockLocationStatus)
        assertTrue((items[0] as SectionHeader.MockLocationStatus).isOn)
        val selected = items[1] as MockLocationNStatus
        assertEquals(london, selected.location)
        assertTrue(selected.status)
        assertSame(SectionHeader.SelectLocations, items[2])
        assertEquals(newYork, (items[3] as Location).location)
        assertEquals(tokyo, (items[4] as Location).location)
    }

    @Test
    fun `UiState Empty constant has sensible defaults`() {
        val empty = UiState.Empty
        assertFalse(empty.showInstructions)
        assertFalse(empty.status)
        assertFalse(empty.hasNotificationPermission)
        assertTrue(empty.items.isEmpty())
    }

    @Test
    fun `elapsedLabel is passed through to UiState`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = true,
            selected = null,
            locations = emptyList(),
            hasNotificationPermission = true,
            elapsedLabel = "01:23:45",
        )

        assertEquals("01:23:45", state.elapsedLabel)
    }

    @Test
    fun `empty elapsedLabel is passed through to UiState`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = false,
            selected = null,
            locations = emptyList(),
            hasNotificationPermission = true,
            elapsedLabel = "",
        )

        assertEquals("", state.elapsedLabel)
    }

    @Test
    fun `selected is passed through to UiState`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = true,
            selected = london,
            locations = emptyList(),
            hasNotificationPermission = true,
            elapsedLabel = "",
        )

        assertEquals(london, state.selected)
    }

    @Test
    fun `null selected is passed through to UiState`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = false,
            selected = null,
            locations = emptyList(),
            hasNotificationPermission = true,
            elapsedLabel = "",
        )

        assertNull(state.selected)
    }

    @Test
    fun `getCountryCode returns correct code for known countries`() {
        assertEquals("SG", UiStateMapper.getCountryCode("Singapore, Singapore"))
        assertEquals("SE", UiStateMapper.getCountryCode("Stockholm, Sweden"))
        assertEquals("TR", UiStateMapper.getCountryCode("Istanbul, Türkiye"))
        assertEquals("HK", UiStateMapper.getCountryCode("Kowloon, Hong Kong"))
        assertEquals("MY", UiStateMapper.getCountryCode("Kuala Lumpur, Malaysia"))
        assertEquals("NO", UiStateMapper.getCountryCode("Oslo, Norway"))
        assertEquals("BD", UiStateMapper.getCountryCode("Dhaka, Bangladesh"))
        assertEquals("PK", UiStateMapper.getCountryCode("Karachi, Pakistan"))
        assertEquals("PH", UiStateMapper.getCountryCode("Manila, Philippines"))
        assertEquals("TW", UiStateMapper.getCountryCode("Taipei, Taiwan"))
        assertEquals("KH", UiStateMapper.getCountryCode("Phnom Penh, Cambodia"))
        assertEquals("LA", UiStateMapper.getCountryCode("Vientiane, Laos"))
        assertEquals("MM", UiStateMapper.getCountryCode("Yangon, Myanmar"))
        assertEquals("CZ", UiStateMapper.getCountryCode("Prague, Czechia"))
        assertEquals("HU", UiStateMapper.getCountryCode("Budapest, Hungary"))
        assertEquals("AT", UiStateMapper.getCountryCode("Vienna, Austria"))
    }

    @Test
    fun `getCountryCode returns fallback code for unknown country`() {
        assertEquals("??", UiStateMapper.getCountryCode("Paris, France"))
        assertEquals("??", UiStateMapper.getCountryCode("London, UK"))
        assertEquals("??", UiStateMapper.getCountryCode("NoCommaHere"))
    }

    @Test
    fun `A_TO_Z sort order produces locations sorted alphabetically ascending`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = false,
            selected = null,
            locations = listOf(tokyo, london, newYork),
            hasNotificationPermission = true,
            elapsedLabel = "",
            sortOrder = SortOrder.A_TO_Z,
        )

        val locationItems = state.items.filterIsInstance<Location>().map { it.location }
        assertEquals(listOf(london, newYork, tokyo), locationItems)
    }

    @Test
    fun `Z_TO_A sort order produces locations sorted reverse-alphabetically descending`() {
        val state = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = false,
            selected = null,
            locations = listOf(tokyo, london, newYork),
            hasNotificationPermission = true,
            elapsedLabel = "",
            sortOrder = SortOrder.Z_TO_A,
        )

        val locationItems = state.items.filterIsInstance<Location>().map { it.location }
        assertEquals(listOf(tokyo, newYork, london), locationItems)
    }

    @Test
    fun `sortOrder is reflected in UiState`() {
        val stateAtoZ = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = false,
            selected = null,
            locations = emptyList(),
            hasNotificationPermission = true,
            elapsedLabel = "",
            sortOrder = SortOrder.A_TO_Z,
        )
        val stateZtoA = UiStateMapper.mapToUiState(
            showInstructions = false,
            status = false,
            selected = null,
            locations = emptyList(),
            hasNotificationPermission = true,
            elapsedLabel = "",
            sortOrder = SortOrder.Z_TO_A,
        )

        assertEquals(SortOrder.A_TO_Z, stateAtoZ.sortOrder)
        assertEquals(SortOrder.Z_TO_A, stateZtoA.sortOrder)
    }
}
