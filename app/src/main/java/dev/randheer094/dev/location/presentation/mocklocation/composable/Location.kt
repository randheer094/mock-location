package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.randheer094.dev.location.presentation.mocklocation.state.Location

@Composable
fun Location(
    state: Location,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier,
        headlineContent = {
            Text(state.location.name)
        },
        supportingContent = {
            Text("${state.location.lat}, ${state.location.long}")
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
            )
        },
    )
}