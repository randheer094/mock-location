package dev.randheer094.dev.location.presentation.mocklocation.state

import dev.randheer094.dev.location.domain.MockLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UiStateMapper {

    suspend fun mapToUiState(
        status: Boolean,
        selected: MockLocation?,
        locations: List<MockLocation>,
    ): UiState {

        fun mockLocationNStatusItem() = MockLocationNStatus(
            location = selected,
            status = status,
        )

        return withContext(Dispatchers.Default) {
            UiState(
                status = status,
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