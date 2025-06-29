package dev.randheer094.dev.location.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SelectMockLocationUseCase(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
) {
    suspend fun execute(location: MockLocation) {
        dataStore.edit {
            it[SELECTED_MOCK_LOCATION] = json.encodeToString(location)
        }
    }
}
