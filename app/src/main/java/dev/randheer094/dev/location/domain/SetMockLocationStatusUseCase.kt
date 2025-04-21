package dev.randheer094.dev.location.domain

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

class SetMockLocationStatusUseCase(
    private val dataStore: DataStore<Preferences>,
) {
    suspend fun execute(status: Boolean) {
        kotlin.runCatching {
            dataStore.updateData {
                it.toMutablePreferences().apply {
                    this[MOCK_LOCATION_STATUS] = status
                }
            }
        }.onFailure {
            coroutineContext.ensureActive()
            Log.e("PreLoadMockLocationsUseCase", it.message.orEmpty())
        }
    }
}