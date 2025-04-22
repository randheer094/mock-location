package dev.randheer094.dev.location.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class SetupInstructionStatusUseCase(
    private val dataStore: DataStore<Preferences>,
) {
    fun execute(): Flow<Boolean> {
        return dataStore.data.map {
            it[SETUP_INSTRUCTION_STATUS] ?: true
        }
            .flowOn(Dispatchers.IO)
            .catch { emit(true) }
            .distinctUntilChanged()
            .flowOn(Dispatchers.Default)
    }
}