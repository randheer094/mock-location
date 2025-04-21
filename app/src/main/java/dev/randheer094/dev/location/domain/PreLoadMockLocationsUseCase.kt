package dev.randheer094.dev.location.domain

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

class PreLoadMockLocationsUseCase(
    private val dataStore: DataStore<Preferences>,
    private val context: Context,
) {
    suspend fun execute() {
        kotlin.runCatching {
            context.assets.open("m_l.json").bufferedReader().use { reader ->
                val string = reader.readText()
                dataStore.updateData {
                    it.toMutablePreferences().apply {
                        this[MOCK_LOCATIONS_DATA_KEY] = string
                    }
                }
            }
        }.onFailure {
            coroutineContext.ensureActive()
            Log.e("PreLoadMockLocationsUseCase", it.message.orEmpty())
        }
    }
}