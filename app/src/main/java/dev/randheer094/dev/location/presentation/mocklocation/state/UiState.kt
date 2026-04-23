package dev.randheer094.dev.location.presentation.mocklocation.state

import dev.randheer094.dev.location.domain.MockLocation

sealed interface UiItem

data class MockLocationNStatus(
    val location: MockLocation?,
    val status: Boolean,
) : UiItem

data class Location(val location: MockLocation) : UiItem

/**
 * Identifies which section header to show. Actual strings are resolved in the Composable
 * layer so UiStateMapper stays free of Android resource dependencies.
 */
sealed interface SectionHeader : UiItem {
    /** Header above the currently selected mock location, includes on/off status. */
    data class MockLocationStatus(val isOn: Boolean) : SectionHeader

    /** Header above the list of preloaded locations. */
    data object SelectLocations : SectionHeader
}


data class UiState(
    val showInstructions: Boolean,
    val status: Boolean,
    val hasNotificationPermission: Boolean,
    val items: List<UiItem>,
    val elapsedLabel: String = "",
    val selected: MockLocation? = null,
    val sortOrder: SortOrder = SortOrder.A_TO_Z,
) {
    companion object {
        val Empty = UiState(
            showInstructions = false,
            status = false,
            hasNotificationPermission = false,
            items = emptyList(),
            elapsedLabel = "",
            selected = null,
            sortOrder = SortOrder.A_TO_Z,
        )
    }
}
