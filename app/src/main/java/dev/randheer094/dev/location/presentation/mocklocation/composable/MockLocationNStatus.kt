package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.randheer094.dev.location.presentation.mocklocation.state.MockLocationNStatus

@Composable
fun MockLocationNStatus(
    state: MockLocationNStatus,
    modifier: Modifier = Modifier,
) {
    if (state.location != null) {
        ListItem(
            modifier = modifier,
            headlineContent = { Text(state.location.name) },
            supportingContent = { Text("${state.location.lat}, ${state.location.long}") },
            trailingContent = {
                Icon(
                    imageVector = if (state.status) {
                        Icons.Default.Clear
                    } else {
                        Icons.Rounded.PlayArrow
                    },
                    contentDescription = null,
                )
            },
        )
    }
}