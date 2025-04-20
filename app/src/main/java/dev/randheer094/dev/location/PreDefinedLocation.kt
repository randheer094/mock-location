package dev.randheer094.dev.location

data class MockLocation(
    val name: String,
    val lat: Double,
    val lng: Double
) {
    companion object {
        fun getPredefinedLocations(): List<MockLocation> {
            return listOf(
                MockLocation("SG", 1.2788982, 103.846496),
                MockLocation("TW", 25.0415595, 121.5604264),
                MockLocation("MY", 3.1115726, 101.6633107),
                MockLocation("HK", 22.2827247, 114.1552671),
                MockLocation("SE", 59.3331073, 18.0416096),
            )
        }
    }
}