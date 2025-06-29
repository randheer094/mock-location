package dev.randheer094.dev.location.domain

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json

class GetMockLocationsUseCase(
    private val context: Context,
    private val json: Json,
) {
    fun execute(): Flow<List<MockLocation>> = flow {
        val locations = runCatching {
            context.assets.open("m_l.json").bufferedReader().use { reader ->
                json.decodeFromString<List<MockLocation>>(reader.readText())
            }
        }.getOrElse { emptyList() }
        emit(locations)
    }.flowOn(Dispatchers.IO)
}
