package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.randheer094.dev.location.presentation.mocklocation.MockLocationViewModel
import dev.randheer094.dev.location.presentation.mocklocation.state.Location
import dev.randheer094.dev.location.presentation.mocklocation.state.MockLocationNStatus
import dev.randheer094.dev.location.presentation.mocklocation.state.SectionHeader
import dev.randheer094.dev.location.presentation.mocklocation.state.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MockLocationScreen(
    viewModel: MockLocationViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberModalBottomSheetState(),
    )
    val coroutineScope = rememberCoroutineScope()

    if (state.showInstructions) {
        SetupInstruction { viewModel.onInstructionDismiss() }
    } else {
        ScreenContent(scaffoldState, coroutineScope, viewModel, state)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ScreenContent(
    scaffoldState: BottomSheetScaffoldState,
    coroutineScope: CoroutineScope,
    viewModel: MockLocationViewModel,
    state: UiState
) {
    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            AddMockLocationBottomSheet {
                coroutineScope.launch {
                    viewModel.onManualLocation(it)
                    scaffoldState.bottomSheetState.hide()
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        ) {
            items(state.items) {
                when (it) {
                    is MockLocationNStatus -> MockLocationNStatus(
                        state = it,
                        onEdit = {
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
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
}

