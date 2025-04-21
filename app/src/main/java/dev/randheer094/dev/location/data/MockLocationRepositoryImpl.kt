package dev.randheer094.dev.location.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.domain.MockLocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import java.io.IOException

class MockLocationRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
    private val context: Context,
) : MockLocationRepository {
    companion object {
        private val MOCK_LOCATIONS_DATA_KEY = stringPreferencesKey("m_ls")
    }

    override fun getMockLocations() =
        dataStore.data.map {
            json.decodeFromString<List<MockLocationData>>(it[MOCK_LOCATIONS_DATA_KEY].orEmpty())
        }
            .flowOn(Dispatchers.IO)
            .map {
                it.map { item -> MockLocation(item.name, item.lat, item.long) }
            }.catch {
                emit(emptyList())
            }

    override suspend fun initMockLocations() {
        try {
            context.assets.open("m_l.json").bufferedReader().use { reader ->
                val string = reader.readText()
                dataStore.updateData {
                    it.toMutablePreferences().apply {
                        this[MOCK_LOCATIONS_DATA_KEY] = string
                    }
                }
            }
        } catch (ioException: IOException) {
            Log.e("MockLocationRepositoryImpl", ioException.message.orEmpty())
        }
    }
}