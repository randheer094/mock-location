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
import dev.randheer094.dev.location.presentation.service.IMockLocationService
import dev.randheer094.dev.location.presentation.utils.LocationUtils
import dev.randheer094.dev.location.presentation.utils.PermissionUtils
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
    permissionUtils: PermissionUtils,
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
        permissionUtils.getNotificationPermissionState(),
    ) { showInstructions, status, selected, locations, notiPermissionState ->
        uiStateMapper.mapToUiState(
            showInstructions = showInstructions,
            status = status,
            selected = selected,
            locations = locations,
            hasNotificationPermission = notiPermissionState.isGranted,
        )
    }.distinctUntilChanged().flowOn(Dispatchers.Default).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = UiState.Empty,
    )

    fun onItemClick(location: MockLocation, service: IMockLocationService?) = viewModelScope.launch {
        selectMockLocationUseCase.execute(location)
        if (state.value.status) {
            startMockLocation(location, service)
        }
    }

    fun onManualLocation(location: MockLocation, service: IMockLocationService?) = viewModelScope.launch {
        selectMockLocationUseCase.execute(location)
        if (state.value.status) {
            startMockLocation(location, service)
        }
    }

    fun setMockLocationStatus(status: Boolean, location: MockLocation?, service: IMockLocationService?) {
        if (status) {
            stopMockLocation(service)
        } else {
            location?.let { startMockLocation(it, service) }
        }
    }

    fun onInstructionDismiss() = viewModelScope.launch(Dispatchers.Default) {
        setSetupInstructionStatusUseCase.execute(false)
    }

    private fun stopMockLocation(service: IMockLocationService?) = viewModelScope.launch(Dispatchers.Default) {
        if (!locationUtils.removeMockProvider(locationManager)) {
            setSetupInstructionStatusUseCase.execute(true)
            return@launch
        }
        service?.stopMocking()
        setMockLocationStatusUseCase.execute(false)
    }

    private fun startMockLocation(location: MockLocation, service: IMockLocationService?) = viewModelScope.launch(Dispatchers.Default) {
        if (!locationUtils.addMockProvider(locationManager)) {
            setSetupInstructionStatusUseCase.execute(true)
            return@launch
        }

        service?.startMocking(location)
        setMockLocationStatusUseCase.execute(true)
    }
}
