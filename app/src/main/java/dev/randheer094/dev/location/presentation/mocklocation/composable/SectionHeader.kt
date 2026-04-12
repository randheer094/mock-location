package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.randheer094.dev.location.R
import dev.randheer094.dev.location.presentation.mocklocation.state.SectionHeader

@Composable
fun SectionHeader(
    state: SectionHeader,
    modifier: Modifier = Modifier,
) {
    val text = when (state) {
        is SectionHeader.MockLocationStatus -> stringResource(
            R.string.section_mock_location_status,
            stringResource(if (state.isOn) R.string.status_on else R.string.status_off),
        )
        SectionHeader.SelectLocations -> stringResource(R.string.section_select_locations)
    }
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(horizontal = 8.dp, vertical = 12.dp),
    )
}
