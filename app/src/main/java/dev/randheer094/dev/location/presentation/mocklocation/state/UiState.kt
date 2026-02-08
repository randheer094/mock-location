package dev.randheer094.dev.location.presentation.mocklocation.state

import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.domain.SearchResult

sealed interface UiItem

data class MockLocationNStatus(
    val location: MockLocation?,
    val status: Boolean,
) : UiItem

data class Location(val location: MockLocation) : UiItem
data class SectionHeader(val text: String) : UiItem
data class SearchLocation(val searchResult: SearchResult) : UiItem


data class UiState(
    val showInstructions: Boolean,
    val status: Boolean,
    val hasNotificationPermission: Boolean,
    val items: List<UiItem>,
    val isSearching: Boolean = false,
    val searchQuery: String = "",
) {
    companion object {
        val Empty = UiState(
            showInstructions = false,
            status = false,
            hasNotificationPermission = false,
            items = emptyList(),
        )
    }
}
