package dev.randheer094.dev.location.presentation.mocklocation.state

import dev.randheer094.dev.location.domain.MockLocation

object UiStateMapper {

    fun mapToUiState(
        showInstructions: Boolean,
        status: Boolean,
        selected: MockLocation?,
        locations: List<MockLocation>,
        hasNotificationPermission: Boolean,
        elapsedLabel: String,
        sortOrder: SortOrder = SortOrder.A_TO_Z,
    ): UiState {
        val sortedLocations = when (sortOrder) {
            SortOrder.A_TO_Z -> locations.sortedBy { it.name }
            SortOrder.Z_TO_A -> locations.sortedByDescending { it.name }
        }
        val items = buildList {
            add(SectionHeader.MockLocationStatus(isOn = status))
            add(MockLocationNStatus(selected, status))
            if (sortedLocations.isNotEmpty()) {
                add(SectionHeader.SelectLocations)
            }
            addAll(sortedLocations.map { Location(it) })
        }

        return UiState(
            showInstructions = showInstructions,
            status = status,
            hasNotificationPermission = hasNotificationPermission,
            items = items,
            elapsedLabel = elapsedLabel,
            selected = selected,
            sortOrder = sortOrder,
        )
    }

    fun getCountryCode(locationName: String): String {
        val country = locationName.substringAfter(',').trim()
        return countryCodeMap[country] ?: "??"
    }

    private val countryCodeMap = mapOf(
        "Singapore" to "SG",
        "Sweden" to "SE",
        "Türkiye" to "TR",
        "Hong Kong" to "HK",
        "Malaysia" to "MY",
        "Norway" to "NO",
        "Bangladesh" to "BD",
        "Pakistan" to "PK",
        "Philippines" to "PH",
        "Taiwan" to "TW",
        "Cambodia" to "KH",
        "Laos" to "LA",
        "Myanmar" to "MM",
        "Czechia" to "CZ",
        "Hungary" to "HU",
        "Austria" to "AT",
    )
}
