package dev.randheer094.dev.location.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class MockLocationData(
    @SerialName("name") val name: String,
    @SerialName("lat") val lat: Double,
    @SerialName("long") val long: Double
)