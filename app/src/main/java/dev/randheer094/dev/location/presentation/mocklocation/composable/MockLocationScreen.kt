package dev.randheer094.dev.location.presentation.mocklocation.composable

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.randheer094.dev.location.R
import dev.randheer094.dev.location.presentation.mocklocation.MockLocationViewModel
import dev.randheer094.dev.location.presentation.mocklocation.state.Location
import dev.randheer094.dev.location.presentation.mocklocation.state.UiState
import dev.randheer094.dev.location.presentation.service.IMockLocationService
import dev.randheer094.dev.location.presentation.service.MockLocationService
import dev.randheer094.dev.location.presentation.theme.InterFamily
import dev.randheer094.dev.location.presentation.theme.LocalMockColors
import dev.randheer094.dev.location.presentation.ui.icons.MockIcons
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
        else -> HomeLayout(
            state = state,
            sheetState = sheetState,
            scope = scope,
            viewModel = viewModel,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeLayout(
    state: UiState,
    sheetState: SheetState,
    scope: CoroutineScope,
    viewModel: MockLocationViewModel,
    service: IMockLocationService?,
) {
    val colors = LocalMockColors.current
    val locations = state.items
        .filterIsInstance<Location>()
        .map { it.location }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.bg),
        ) {
            TopBar(onAddClick = { scope.launch { sheetState.show() } })

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 32.dp),
            ) {
                item {
                    HeroCard(
                        state = state,
                        onStop = { viewModel.setMockLocationStatus(true, state.selected, service) },
                        onStart = { viewModel.setMockLocationStatus(false, state.selected, service) },
                    )
                }

                item {
                    SectionHeader()
                }

                items(locations, key = { it.name }) { location ->
                    LocationRow(
                        location = location,
                        isSelected = state.selected?.name == location.name,
                        isActive = state.status && state.selected?.name == location.name,
                        onClick = { viewModel.onItemClick(location, service) },
                        modifier = Modifier
                            .animateItem()
                            .padding(horizontal = 16.dp, vertical = 3.dp),
                    )
                }

                item {
                    Spacer(Modifier.navigationBarsPadding())
                }
            }
        }

        if (sheetState.isVisible) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { scope.launch { sheetState.hide() } },
                containerColor = colors.bgElev,
            ) {
                AddMockLocationBottomSheet { location ->
                    scope.launch {
                        viewModel.onManualLocation(location, service)
                        sheetState.hide()
                    }
                }
            }
        }
        BackHandler(sheetState.isVisible) { scope.launch { sheetState.hide() } }
    }
}

@Composable
private fun TopBar(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalMockColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp)
            .padding(top = 18.dp, bottom = 8.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f),
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = colors.accent,
                modifier = Modifier.size(30.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = MockIcons.Pin,
                        contentDescription = null,
                        tint = colors.accentInk,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
            Text(
                text = stringResource(R.string.wordmark_mock),
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = (-0.6).sp,
                ),
                color = colors.text,
            )
            Text(
                text = stringResource(R.string.wordmark_location),
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                ),
                color = colors.textDim,
            )
        }

        Box(
            modifier = Modifier
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(12.dp),
                    spotColor = colors.liveGlow,
                )
                .size(40.dp),
        ) {
            FilledIconButton(
                onClick = onAddClick,
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(12.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = colors.accent,
                    contentColor = colors.accentInk,
                ),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

@Composable
private fun HeroCard(
    state: UiState,
    onStop: () -> Unit,
    onStart: () -> Unit,
) {
    val colors = LocalMockColors.current

    Surface(
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.dp, colors.border),
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 6.dp),
    ) {
        AnimatedContent(
            targetState = state.status,
            transitionSpec = {
                fadeIn(tween(durationMillis = 250)) togetherWith fadeOut(tween(durationMillis = 250))
            },
            label = "hero_transition",
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(colors.bgElev2, colors.bgElev),
                        start = Offset(Float.POSITIVE_INFINITY, 0f),
                        end = Offset(0f, Float.POSITIVE_INFINITY),
                    ),
                )
                .padding(18.dp),
        ) { isActive ->
            if (isActive) {
                BroadcastingHero(uiState = state, onStop = onStop)
            } else {
                IdleHero(uiState = state, onStart = onStart)
            }
        }
    }
}
