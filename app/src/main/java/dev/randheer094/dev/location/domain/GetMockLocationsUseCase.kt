package dev.randheer094.dev.location.domain

class GetMockLocationsUseCase(
    private val mockLocationRepository: MockLocationRepository,
) {
    fun execute() = mockLocationRepository.getMockLocations()
}