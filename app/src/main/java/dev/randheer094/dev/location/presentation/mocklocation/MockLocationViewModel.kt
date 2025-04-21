package dev.randheer094.dev.location.presentation.mocklocation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.randheer094.dev.location.domain.GetMockLocationsUseCase
import dev.randheer094.dev.location.domain.MockLocation
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MockLocationViewModel(
    getMockLocationsUseCase: GetMockLocationsUseCase,
) : ViewModel() {

    val state = getMockLocationsUseCase.execute()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList(),
        )

    fun onItemCLick(mockLocation: MockLocation) {
        Log.e("mockLocation", mockLocation.toString())
    }
}