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
    private val isMockAppAuthorized: () -> Boolean,
) {
    fun execute(): Flow<Boolean> = dataStore.data
        .map { it[SETUP_INSTRUCTION_STATUS] ?: false }
        .map { forceShow -> forceShow || !isMockAppAuthorized() }
        .distinctUntilChanged()
        .catch { emit(true) }
        .flowOn(Dispatchers.IO)
}
