package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.randheer094.dev.location.presentation.mocklocation.MockLocationViewModel
import dev.randheer094.dev.location.presentation.mocklocation.state.Location
import dev.randheer094.dev.location.presentation.mocklocation.state.MockLocationNStatus
import dev.randheer094.dev.location.presentation.mocklocation.state.SectionHeader
import org.koin.androidx.compose.koinViewModel

@Composable
fun MockLocationScreen(
    viewModel: MockLocationViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        ) {
            items(state) {
                when (it) {
                    is MockLocationNStatus -> MockLocationNStatus(
                        state = it,
                        modifier = Modifier.clickable {
                            viewModel.setMockLocationStatus(!it.status)
                        },
                    )

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

