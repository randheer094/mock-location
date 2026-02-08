package dev.randheer094.dev.location.presentation.mocklocation.state

import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.domain.SearchResult

object UiStateMapper {

    fun mapToUiState(
        showInstructions: Boolean,
        status: Boolean,
        selected: MockLocation?,
        locations: List<MockLocation>,
        hasNotificationPermission: Boolean,
        isSearching: Boolean = false,
        searchQuery: String = "",
        searchResults: List<SearchResult> = emptyList(),
    ): UiState {
        val items = buildList {
            add(SectionHeader("Mock location: (${if (status) "ON" else "OFF"})"))
            add(MockLocationNStatus(selected, status))

            if (isSearching && searchQuery.isNotBlank()) {
                if (searchResults.isEmpty()) {
                    add(SectionHeader("No results found for \"$searchQuery\""))
                } else {
                    add(SectionHeader("Search results for \"$searchQuery\""))
                    addAll(searchResults.map { SearchLocation(it) })
                }
            } else {
                if (locations.isNotEmpty()) {
                    add(SectionHeader("Select locations"))
                }
                addAll(locations.map { Location(it) })
            }
        }

        return UiState(
            showInstructions = showInstructions,
            status = status,
            hasNotificationPermission = hasNotificationPermission,
            items = items,
            isSearching = isSearching,
            searchQuery = searchQuery,
        )
    }
}
