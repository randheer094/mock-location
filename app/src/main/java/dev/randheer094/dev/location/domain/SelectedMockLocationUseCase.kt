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
    fun execute(): Flow<MockLocation?> {
        return dataStore.data.map {
            kotlin.runCatching {
                it[SELECTED_MOCK_LOCATION]?.let { loc ->
                    json.decodeFromString<MockLocation>(loc)
                }
            }.getOrElse { null }
        }
            .flowOn(Dispatchers.IO)
            .catch { emit(null) }
            .distinctUntilChanged()
            .flowOn(Dispatchers.Default)
    }
}