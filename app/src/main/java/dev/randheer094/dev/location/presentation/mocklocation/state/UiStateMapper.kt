package dev.randheer094.dev.location.presentation.mocklocation.state

import dev.randheer094.dev.location.domain.MockLocation

class UiStateMapper {

    fun mapToUiState(
        showInstructions: Boolean,
        status: Boolean,
        selected: MockLocation?,
        locations: List<MockLocation>,
        hasNotificationPermission: Boolean,
    ): UiState {
        val items = buildList {
            add(SectionHeader("Mock location: (${if (status) "ON" else "OFF"})"))
            add(MockLocationNStatus(selected, status))
            if (locations.isNotEmpty()) {
                add(SectionHeader("Select locations"))
            }
            addAll(locations.map { Location(it) })
        }

        return UiState(
            showInstructions = showInstructions,
            status = status,
            hasNotificationPermission = hasNotificationPermission,
            items = items,
        )
    }
}
