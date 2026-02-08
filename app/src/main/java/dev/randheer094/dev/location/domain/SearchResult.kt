package dev.randheer094.dev.location.domain

/**
 * Represents a search result from geocoding services
 */
data class SearchResult(
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val placeId: String? = null
)