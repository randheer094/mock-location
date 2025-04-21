package dev.randheer094.dev.location.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class MockLocationStatusUseCase(
    private val dataStore: DataStore<Preferences>,
) {
    fun execute(): Flow<Boolean> {
        return dataStore.data.map {
            it[MOCK_LOCATION_STATUS] ?: false
        }
            .flowOn(Dispatchers.IO)
            .catch {
                emit(false)
            }
    }
}