package dev.randheer094.dev.location.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit

class SetSetupInstructionStatusUseCase(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun execute(status: Boolean) {
        dataStore.edit {
            it[SETUP_INSTRUCTION_STATUS] = status
        }
    }
}
