package dev.randheer094.dev.location.presentation.mocklocation

import android.location.LocationManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.randheer094.dev.location.domain.GetMockLocationsUseCase
import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.domain.MockLocationStatusUseCase
import dev.randheer094.dev.location.domain.SelectMockLocationUseCase
import dev.randheer094.dev.location.domain.SelectedMockLocationUseCase
import dev.randheer094.dev.location.domain.SetMockLocationStatusUseCase
import dev.randheer094.dev.location.domain.SetSetupInstructionStatusUseCase
import dev.randheer094.dev.location.domain.SetupInstructionStatusUseCase
import dev.randheer094.dev.location.presentation.mocklocation.state.UiState
import dev.randheer094.dev.location.presentation.mocklocation.state.UiStateMapper
import dev.randheer094.dev.location.presentation.utils.LocationUtils
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
    setupInstructionStatusUseCase: SetupInstructionStatusUseCase,
    private val selectMockLocationUseCase: SelectMockLocationUseCase,
    private val setMockLocationStatusUseCase: SetMockLocationStatusUseCase,
    private val setSetupInstructionStatusUseCase: SetSetupInstructionStatusUseCase,
    private val uiStateMapper: UiStateMapper,
    private val locationUtils: LocationUtils,
    private val locationManager: LocationManager,
) : ViewModel() {

    companion object {
        private const val STOP_TIMEOUT_MILLIS = 5_000L
    }

    val state = combine(
        setupInstructionStatusUseCase.execute(),
        mockLocationStatusUseCase.execute(),
        selectedMockLocationUseCase.execute(),
        getMockLocationsUseCase.execute(),
        transform = { showInstructions, status, selected, locations ->
            uiStateMapper.mapToUiState(showInstructions, status, selected, locations)
        }).distinctUntilChanged().flowOn(Dispatchers.Default).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = UiState.Empty,
    )

    fun onItemCLick(location: MockLocation) {
        viewModelScope.launch {
            selectMockLocationUseCase.execute(location)
            if (state.value.status) {
                startMockLocation(location)
            }
        }
    }

    fun onManualLocation(location: MockLocation) {
        viewModelScope.launch {
            selectMockLocationUseCase.execute(location)
            if (state.value.status) {
                startMockLocation(location)
            }
        }
    }

    fun setMockLocationNStatus(status: Boolean, location: MockLocation?) {
        if (status) {
            stopMockLocation()
        } else {
            location?.let { startMockLocation(it) }
        }
    }

    fun onInstructionDismiss() {
        viewModelScope.launch(Dispatchers.Default) {
            setSetupInstructionStatusUseCase.execute(false)
        }
    }

    private fun stopMockLocation() {
        viewModelScope.launch(Dispatchers.Default) {
            locationUtils.removeMockProvider(locationManager)
            setMockLocationStatusUseCase.execute(false)
        }
    }

    private fun startMockLocation(location: MockLocation) {
        viewModelScope.launch(Dispatchers.Default) {
            if (!locationUtils.addMockProvider(locationManager)) {
                setSetupInstructionStatusUseCase.execute(true)
                return@launch
            }

            locationUtils.setMockLocation(locationManager, location.lat, location.long)
            setMockLocationStatusUseCase.execute(true)
        }
    }
}