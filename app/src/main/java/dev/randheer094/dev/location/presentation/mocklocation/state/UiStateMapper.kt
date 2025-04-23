package dev.randheer094.dev.location.presentation.mocklocation.state

import dev.randheer094.dev.location.domain.MockLocation
import dev.shreyaspatil.permissionFlow.PermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UiStateMapper {

    suspend fun mapToUiState(
        showInstructions: Boolean,
        status: Boolean,
        selected: MockLocation?,
        locations: List<MockLocation>,
        hasNotificationPermission: Boolean,
    ): UiState {

        fun mockLocationNStatusItem() = MockLocationNStatus(
            location = selected,
            status = status,
        )

        return withContext(Dispatchers.Default) {
            UiState(
                showInstructions = showInstructions,
                status = status,
                hasNotificationPermission = hasNotificationPermission,
                items = buildList {
                    add(SectionHeader("Mock location: (${if (status) "ON" else "OFF"})"))
                    add(mockLocationNStatusItem())
                    if (locations.isNotEmpty()) {
                        add(SectionHeader("Select locations"))
                    }
                    addAll(locations.map { Location(it) })
                },
            )
        }
    }
}