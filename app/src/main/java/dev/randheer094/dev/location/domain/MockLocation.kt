package dev.randheer094.dev.location.domain

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MockLocation(
    @SerialName("name") val name: String,
    @SerialName("lat") val lat: Double,
    @SerialName("long") val long: Double
)

val SELECTED_MOCK_LOCATION = stringPreferencesKey("m_l")
val MOCK_LOCATION_STATUS = booleanPreferencesKey("m_l_e")
val SETUP_INSTRUCTION_STATUS = booleanPreferencesKey("setup")