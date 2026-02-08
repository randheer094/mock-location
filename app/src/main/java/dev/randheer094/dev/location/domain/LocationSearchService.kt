package dev.randheer094.dev.location.domain

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.net.URLEncoder

/**
 * Service for searching locations using OpenStreetMap Nominatim API
 */
class LocationSearchService {

    companion object {
        private const val TAG = "LocationSearchService"
        private const val NOMINATIM_URL = "https://nominatim.openstreetmap.org/search"
        private const val USER_AGENT = "MockLocationApp/1.0"
    }

    /**
     * Search for locations by query
     */
    suspend fun search(query: String): List<SearchResult> = withContext(Dispatchers.IO) {
        try {
            val encodedQuery = URLEncoder.encode(query, "UTF-8")
            val url = "$NOMINATIM_URL?q=$encodedQuery&format=json&addressdetails=1&limit=10"

            val connection = java.net.URL(url).openConnection()
            connection.setRequestProperty("User-Agent", USER_AGENT)

            val jsonResponse = connection.getInputStream().bufferedReader().use { it.readText() }
            Log.d(TAG, "Search response: $jsonResponse")

            parseSearchResults(jsonResponse)
        } catch (e: Exception) {
            Log.e(TAG, "Search failed", e)
            emptyList()
        }
    }

    /**
     * Parse search results from JSON response
     */
    private fun parseSearchResults(json: String): List<SearchResult> {
        try {
            val results = kotlinx.serialization.json.Json.decodeFromString<List<NominatimResult>>(json)
            return results.map { it.toSearchResult() }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse search results", e)
            return emptyList()
        }
    }

    @Serializable
    private data class NominatimResult(
        val place_id: Long,
        val licence: String,
        val osm_type: String,
        val osm_id: Long,
        val boundingbox: List<String>,
        val lat: String,
        val lon: String,
        val display_name: String,
        @SerialName("class") val clazz: String,
        val type: String,
        val importance: Double,
        val address: Address? = null
    ) {
        fun toSearchResult(): SearchResult {
            return SearchResult(
                name = getAddressDisplayName(address, display_name),
                address = display_name,
                lat = lat.toDoubleOrNull() ?: 0.0,
                lng = lon.toDoubleOrNull() ?: 0.0,
                placeId = place_id.toString()
            )
        }

        private fun getAddressDisplayName(address: Address?, displayName: String): String {
            return address?.let {
                buildString {
                    if (!it.city.isNullOrEmpty()) append(it.city)
                    else if (!it.town.isNullOrEmpty()) append(it.town)
                    else if (!it.village.isNullOrEmpty()) append(it.village)

                    if (!it.state.isNullOrEmpty()) {
                        if (isNotEmpty()) append(", ")
                        append(it.state)
                    }

                    if (!it.country.isNullOrEmpty()) {
                        if (isNotEmpty()) append(", ")
                        append(it.country)
                    }

                    if (isEmpty()) append(displayName.take(50) + if (displayName.length > 50) "..." else "")
                }
            } ?: run {
                // Fallback to extracting location name from display_name
                val parts = displayName.split(",")
                if (parts.isNotEmpty()) parts[0].trim() else displayName.take(30)
            }
        }
    }

    @Serializable
    private data class Address(
        val city: String? = null,
        val town: String? = null,
        val village: String? = null,
        val state: String? = null,
        val country: String? = null,
        val postcode: String? = null,
        val road: String? = null
    )
}