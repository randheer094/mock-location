package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.randheer094.dev.location.presentation.mocklocation.state.MockLocationNStatus

@Composable
fun MockLocationNStatus(
    state: MockLocationNStatus,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
    onStartStop: () -> Unit,
) {
    if (state.location != null) {
        ListItem(
            modifier = modifier,
            headlineContent = { Text(state.location.name) },
            supportingContent = { Text("${state.location.lat}, ${state.location.long}") },
            trailingContent = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.clickable(onClick = onEdit),
                    )
                    Icon(
                        imageVector = if (state.status) {
                            Icons.Default.Clear
                        } else {
                            Icons.Rounded.PlayArrow
                        },
                        contentDescription = null,
                        modifier = Modifier.clickable(onClick = onStartStop),
                    )
                }
            },
        )
    }
}