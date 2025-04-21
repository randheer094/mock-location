package dev.randheer094.dev.location.presentation.mocklocation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.randheer094.dev.location.domain.GetMockLocationsUseCase
import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.domain.MockLocationStatusUseCase
import dev.randheer094.dev.location.domain.SelectMockLocationUseCase
import dev.randheer094.dev.location.domain.SelectedMockLocationUseCase
import dev.randheer094.dev.location.domain.SetMockLocationStatusUseCase
import dev.randheer094.dev.location.presentation.mocklocation.state.UiStateMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MockLocationViewModel(
    getMockLocationsUseCase: GetMockLocationsUseCase,
    selectedMockLocationUseCase: SelectedMockLocationUseCase,
    mockLocationStatusUseCase: MockLocationStatusUseCase,
    private val selectMockLocationUseCase: SelectMockLocationUseCase,
    private val setMockLocationStatusUseCase: SetMockLocationStatusUseCase,
    private val uiStateMapper: UiStateMapper,
) : ViewModel() {

    val state = combine(
        mockLocationStatusUseCase.execute(),
        selectedMockLocationUseCase.execute(),
        getMockLocationsUseCase.execute(),
        transform = { status, selected, locations ->
            uiStateMapper.mapToUiState(status, selected, locations)
        }
    )
        .distinctUntilChanged()
        .flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList(),
        )

    fun onItemCLick(mockLocation: MockLocation) {
        viewModelScope.launch {
            selectMockLocationUseCase.execute(mockLocation)
        }
    }

    fun setMockLocationStatus(status: Boolean) {
        viewModelScope.launch {
            setMockLocationStatusUseCase.execute(status)
        }
    }
}