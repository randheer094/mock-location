package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.randheer094.dev.location.presentation.mocklocation.state.SectionHeader

@Composable
fun SectionHeader(
    state: SectionHeader,
    modifier: Modifier = Modifier,
) {
    Text(
        text = state.text,
        style = MaterialTheme.typography.titleLarge,
        modifier = modifier.padding(all = 16.dp),
    )
}