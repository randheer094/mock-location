package dev.randheer094.dev.location.presentation.mocklocation.composable

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.randheer094.dev.location.presentation.mocklocation.Event
import dev.randheer094.dev.location.presentation.mocklocation.MockLocationViewModel
import dev.randheer094.dev.location.presentation.mocklocation.state.Location
import dev.randheer094.dev.location.presentation.mocklocation.state.MockLocationNStatus
import dev.randheer094.dev.location.presentation.mocklocation.state.SectionHeader
import dev.randheer094.dev.location.presentation.mocklocation.state.UiState
import dev.randheer094.dev.location.presentation.service.IMockLocationService
import dev.randheer094.dev.location.presentation.service.MockLocationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockLocationScreen(
    viewModel: MockLocationViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val scope = rememberCoroutineScope()

    LocationService(viewModel.eventFlow)

    when {
        state.showInstructions -> SetupInstruction { viewModel.onInstructionDismiss() }
        !state.hasNotificationPermission -> NotificationPermission()
        else -> ScreenContent(
            sheetState = sheetState,
            scope = scope,
            viewModel = viewModel,
            state = state,
        )
    }
}

@Composable
private fun LocationService(eventFlow: SharedFlow<Event>) {
    val context = LocalContext.current
    var boundService by remember { mutableStateOf<IMockLocationService?>(null) } // State to hold the service instance

    // ServiceConnection implementation
    val connection = remember {
        object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                val binder = service as MockLocationService.LocalBinder
                boundService = binder.getService()
            }

            override fun onServiceDisconnected(arg0: ComponentName) {
                boundService = null
            }
        }
    }

    // Effect to bind and unbind the service tied to the composable lifecycle
    DisposableEffect(Unit) {
        // Create Intent for the service
        Intent(context, MockLocationService::class.java).also { intent ->
            // Bind to the service
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        // Unbind from the service when the composable leaves the composition
        onDispose {
            context.unbindService(connection)
            boundService = null // Clear reference on dispose
        }
    }

    LaunchedEffect(eventFlow, boundService) { // Restart collection if flow or service changes
        eventFlow.collect { event ->
            val service = boundService // Capture the current state of boundService
            if (service != null) {
                when (event) {
                    is Event.StartMocking -> {
                        Intent(context, MockLocationService::class.java).also { intent ->
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                context.startForegroundService(intent)
                            } else {
                                context.startService(intent)
                            }
                        }
                        service.startMocking(event.location.lat, event.location.long)
                    }

                    Event.StopMocking -> {
                        service.stopMocking()
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ScreenContent(
    sheetState: SheetState,
    scope: CoroutineScope,
    viewModel: MockLocationViewModel,
    state: UiState,
) {
    Box {
        Scaffold { padding ->
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(vertical = 12.dp),
            ) {
                items(state.items) {
                    when (it) {
                        is MockLocationNStatus -> MockLocationNStatus(
                            state = it,
                            onEdit = {
                                scope.launch {
                                    sheetState.show()
                                }
                            },
                        ) {
                            viewModel.setMockLocationNStatus(it.status, it.location)
                        }

                        is SectionHeader -> SectionHeader(it)
                        is Location -> Location(
                            state = it,
                            modifier = Modifier.clickable {
                                viewModel.onItemCLick(it.location)
                            },
                        )
                    }
                }
            }
        }
        if (sheetState.isVisible) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                    }
                }
            ) {
                AddMockLocationBottomSheet {
                    scope.launch {
                        viewModel.onManualLocation(it)
                        sheetState.hide()
                    }
                }
            }
        }
    }
}

