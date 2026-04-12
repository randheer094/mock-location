package dev.randheer094.dev.location.presentation.mocklocation.state

import dev.randheer094.dev.location.domain.MockLocation

object UiStateMapper {

    fun mapToUiState(
        showInstructions: Boolean,
        status: Boolean,
        selected: MockLocation?,
        locations: List<MockLocation>,
        hasNotificationPermission: Boolean,
    ): UiState {
        val items = buildList {
            add(SectionHeader.MockLocationStatus(isOn = status))
            add(MockLocationNStatus(selected, status))
            if (locations.isNotEmpty()) {
                add(SectionHeader.SelectLocations)
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
