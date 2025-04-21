package dev.randheer094.dev.location.domain

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.coroutines.coroutineContext

class SelectMockLocationUseCase(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
) {
    suspend fun execute(location: MockLocation) {
        kotlin.runCatching {
            dataStore.updateData {
                it.toMutablePreferences().apply {
                    this[SELECTED_MOCK_LOCATION] = json.encodeToString(location)
                }
            }
        }.onFailure {
            coroutineContext.ensureActive()
            Log.e("PreLoadMockLocationsUseCase", it.message.orEmpty())
        }
    }
}