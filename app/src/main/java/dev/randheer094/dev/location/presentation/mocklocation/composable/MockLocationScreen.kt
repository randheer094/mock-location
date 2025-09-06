package dev.randheer094.dev.location.presentation.mocklocation.composable

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import dev.randheer094.dev.location.presentation.mocklocation.MockLocationViewModel
import dev.randheer094.dev.location.presentation.mocklocation.state.Location
import dev.randheer094.dev.location.presentation.mocklocation.state.MockLocationNStatus
import dev.randheer094.dev.location.presentation.mocklocation.state.SectionHeader
import dev.randheer094.dev.location.presentation.mocklocation.state.UiState
import dev.randheer094.dev.location.presentation.service.IMockLocationService
import dev.randheer094.dev.location.presentation.service.MockLocationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockLocationScreen(
    viewModel: MockLocationViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val service = useMockLocationService()

    when {
        state.showInstructions -> SetupInstruction { viewModel.onInstructionDismiss() }
        !state.hasNotificationPermission -> NotificationPermission()
        else -> ScreenContent(
            sheetState = sheetState,
            scope = scope,
            viewModel = viewModel,
            state = state,
            service = service,
        )
    }
}

@Composable
private fun useMockLocationService(): IMockLocationService? {
    val context = LocalContext.current
    var boundService by remember { mutableStateOf<IMockLocationService?>(null) }

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

    DisposableEffect(Unit) {
        Intent(context, MockLocationService::class.java).also { intent ->
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        onDispose {
            context.unbindService(connection)
        }
    }
    return boundService
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ScreenContent(
    sheetState: SheetState,
    scope: CoroutineScope,
    viewModel: MockLocationViewModel,
    state: UiState,
    service: IMockLocationService?,
) {
    Box {
        Scaffold {
            LazyColumn(
                modifier = Modifier.padding(it),
                contentPadding = PaddingValues(vertical = 12.dp),
            ) {
                items(state.items) { item ->
                    when (item) {
                        is MockLocationNStatus -> MockLocationNStatus(
                            state = item,
                            onEdit = { scope.launch { sheetState.show() } },
                        ) {
                            viewModel.setMockLocationStatus(item.status, item.location, service)
                        }

                        is SectionHeader -> SectionHeader(item)
                        is Location -> Location(
                            state = item,
                            modifier = Modifier.clickable { viewModel.onItemClick(item.location, service) },
                        )
                    }
                }
            }
        }
        if (sheetState.isVisible) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { scope.launch { sheetState.hide() } }
            ) {
                AddMockLocationBottomSheet {
                    scope.launch {
                        viewModel.onManualLocation(it, service)
                        sheetState.hide()
                    }
                }
            }
        }
        BackHandler(sheetState.isVisible) { scope.launch { sheetState.hide() } }
    }
}

