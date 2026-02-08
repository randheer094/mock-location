package dev.randheer094.dev.location.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for searching locations
 */
class SearchLocationsUseCase(
    private val locationSearchService: LocationSearchService
) {
    fun execute(query: String): Flow<List<SearchResult>> = flow {
        val results = if (query.isNotBlank()) {
            locationSearchService.search(query)
        } else {
            emptyList()
        }
        emit(results)
    }
}