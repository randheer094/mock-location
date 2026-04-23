package dev.randheer094.dev.location.presentation.mocklocation

import android.location.LocationManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.randheer094.dev.location.domain.GetMockLocationsUseCase
import dev.randheer094.dev.location.domain.MOCK_STARTED_AT
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
import dev.randheer094.dev.location.presentation.service.MockLocationServiceStarter
import dev.randheer094.dev.location.presentation.utils.LocationUtils
import dev.randheer094.dev.location.presentation.utils.PermissionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
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
    private val serviceStarter: MockLocationServiceStarter,
    private val dataStore: DataStore<Preferences>,
) : ViewModel() {

    companion object {
        private const val STOP_TIMEOUT_MILLIS = 5_000L
    }

    private val elapsedLabelFlow: Flow<String> = dataStore.data
        .map { it[MOCK_STARTED_AT] ?: 0L }
        .flatMapLatest { startedAt ->
            if (startedAt == 0L) {
                flow { emit("") }
            } else {
                flow {
                    while (true) {
                        val elapsed = System.currentTimeMillis() - startedAt
                        emit(formatElapsed(elapsed))
                        delay(1000)
                    }
                }
            }
        }

    val state = combine(
        combine(
            setupInstructionStatusUseCase.execute(),
            mockLocationStatusUseCase.execute(),
            selectedMockLocationUseCase.execute(),
        ) { showInstructions, status, selected -> Triple(showInstructions, status, selected) },
        getMockLocationsUseCase.execute(),
        permissionUtils.getNotificationPermissionState(),
        elapsedLabelFlow,
    ) { triple, locations, notiPermissionState, elapsedLabel ->
        uiStateMapper.mapToUiState(
            showInstructions = triple.first,
            status = triple.second,
            selected = triple.third,
            locations = locations,
            hasNotificationPermission = notiPermissionState.isGranted,
            elapsedLabel = elapsedLabel,
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

    /**
     * Toggle mocking. [currentStatus] is the state BEFORE the tap: if mocking is currently
     * on, we stop it; otherwise we start mocking with [location] (when non-null).
     */
    fun setMockLocationStatus(currentStatus: Boolean, location: MockLocation?, service: IMockLocationService?) {
        if (currentStatus) {
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
        // Tear down the started (foreground) service so it doesn't stay alive in the background
        // after the user disables mocking.
        serviceStarter.stop()
        setMockLocationStatusUseCase.execute(false)
        dataStore.edit { it[MOCK_STARTED_AT] = 0L }
    }

    private fun startMockLocation(location: MockLocation, service: IMockLocationService?) = viewModelScope.launch(Dispatchers.Default) {
        if (!locationUtils.addMockProvider(locationManager)) {
            setSetupInstructionStatusUseCase.execute(true)
            return@launch
        }

        // Promote the bound service to a started foreground service so it survives the UI
        // leaving composition (navigation / process moves to background). Without this the
        // mocking loop is cancelled as soon as MockLocationScreen unbinds. We also pass the
        // location via the intent so the service can start the mocking loop even if the
        // binder hasn't connected yet (race at first launch).
        serviceStarter.start(location)
        service?.startMocking(location)
        setMockLocationStatusUseCase.execute(true)
        dataStore.edit { it[MOCK_STARTED_AT] = System.currentTimeMillis() }
    }

    private fun formatElapsed(millis: Long): String {
        val totalSeconds = millis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return "%02d:%02d:%02d".format(hours, minutes, seconds)
    }
}
