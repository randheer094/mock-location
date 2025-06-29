package dev.randheer094.dev.location.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class SelectedMockLocationUseCase(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
) {
    fun execute(): Flow<MockLocation?> = dataStore.data
        .map {
            it[SELECTED_MOCK_LOCATION]?.let {
                json.decodeFromString<MockLocation>(it)
            }
        }
        .distinctUntilChanged()
        .catch { emit(null) }
        .flowOn(Dispatchers.IO)
}
