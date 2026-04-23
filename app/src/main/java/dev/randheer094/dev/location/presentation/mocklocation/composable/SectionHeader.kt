package dev.randheer094.dev.location.presentation.mocklocation.composable

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.randheer094.dev.location.R
import dev.randheer094.dev.location.presentation.mocklocation.state.SortOrder
import dev.randheer094.dev.location.presentation.theme.InterFamily
import dev.randheer094.dev.location.presentation.theme.LocalMockColors
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme

@Composable
fun SectionHeader(
    sortOrder: SortOrder,
    onToggleSort: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalMockColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 22.dp)
            .padding(top = 20.dp, bottom = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.section_preset_locations),
            style = TextStyle(
                fontFamily = InterFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 1.6.sp,
            ),
            color = colors.textDim,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.clickable(onClick = onToggleSort),
        ) {
            Text(
                text = if (sortOrder == SortOrder.A_TO_Z) stringResource(R.string.label_sort_az) else stringResource(R.string.label_sort_za),
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                ),
                color = colors.textDim,
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = colors.textDim,
                modifier = Modifier.padding(0.dp).rotate(if (sortOrder == SortOrder.Z_TO_A) 180f else 0f),
            )
        }
    }
}

@Preview(name = "SectionHeader – Light", showBackground = true)
@Composable
private fun SectionHeaderLightPreview() {
    MockLocationTheme(darkTheme = false) {
        SectionHeader(sortOrder = SortOrder.A_TO_Z, onToggleSort = {})
    }
}

@Preview(name = "SectionHeader – Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SectionHeaderDarkPreview() {
    MockLocationTheme(darkTheme = true) {
        SectionHeader(sortOrder = SortOrder.A_TO_Z, onToggleSort = {})
    }
}
