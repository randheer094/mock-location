package dev.randheer094.dev.location.domain

import kotlinx.coroutines.flow.Flow

interface MockLocationRepository {
    fun getMockLocations(): Flow<List<MockLocation>>
    suspend fun initMockLocations()
}