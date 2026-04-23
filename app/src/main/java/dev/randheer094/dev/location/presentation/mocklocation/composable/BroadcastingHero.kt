package dev.randheer094.dev.location.presentation.mocklocation.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.randheer094.dev.location.R
import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.presentation.mocklocation.state.UiState
import dev.randheer094.dev.location.presentation.theme.InterFamily
import dev.randheer094.dev.location.presentation.theme.JetBrainsMonoFamily
import dev.randheer094.dev.location.presentation.theme.LocalMockColors
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme
import kotlin.math.abs

@Composable
fun BroadcastingHero(
    uiState: UiState,
    onStop: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalMockColors.current
    val location = uiState.selected

    Column(modifier = modifier) {
        // Row 1: StatusPill + timer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StatusPill(
                label = stringResource(R.string.status_live),
                active = true,
            )
            Text(
                text = stringResource(R.string.timer_format, uiState.elapsedLabel),
                style = TextStyle(
                    fontFamily = JetBrainsMonoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                ),
                color = colors.textDim,
            )
        }

        Spacer(Modifier.height(14.dp))

        // Row 2: Radar + caption column
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Radar(modifier = Modifier.size(120.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.overline_currently_spoofing),
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.6.sp,
                    ),
                    color = colors.textDim,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = location?.name?.substringBefore(',') ?: "—",
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 26.sp,
                        letterSpacing = (-0.5).sp,
                    ),
                    color = colors.text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                val country = location?.name?.substringAfter(',')?.trim().orEmpty()
                if (country.isNotEmpty()) {
                    Text(
                        text = country,
                        style = TextStyle(
                            fontFamily = InterFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                        ),
                        color = colors.textDim,
                    )
                }
                if (location != null) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = fmtCoord(location.lat, location.long),
                        style = TextStyle(
                            fontFamily = JetBrainsMonoFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 11.sp,
                        ),
                        color = colors.textMute,
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Stop button
        Button(
            onClick = onStop,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.text,
                contentColor = colors.bg,
            ),
        ) {
            Icon(
                imageVector = Icons.Filled.Stop,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.btn_stop_broadcasting),
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                ),
            )
        }
    }
}

private fun fmtCoord(lat: Double, lng: Double): String {
    val latDir = if (lat >= 0) "N" else "S"
    val lngDir = if (lng >= 0) "E" else "W"
    return "%.4f° %s   %.4f° %s".format(abs(lat), latDir, abs(lng), lngDir)
}

@Preview(name = "BroadcastingHero – Light", showBackground = true)
@Composable
private fun BroadcastingHeroLightPreview() {
    MockLocationTheme(darkTheme = false) {
        BroadcastingHero(
            uiState = UiState(
                showInstructions = false,
                status = true,
                hasNotificationPermission = true,
                items = emptyList(),
                elapsedLabel = "00:05:30",
                selected = MockLocation(name = "Singapore, Singapore", lat = 1.3521, long = 103.8198),
            ),
            onStop = {},
        )
    }
}

@Preview(name = "BroadcastingHero – Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BroadcastingHeroDarkPreview() {
    MockLocationTheme(darkTheme = true) {
        BroadcastingHero(
            uiState = UiState(
                showInstructions = false,
                status = true,
                hasNotificationPermission = true,
                items = emptyList(),
                elapsedLabel = "01:22:05",
                selected = MockLocation(name = "Singapore, Singapore", lat = 1.3521, long = 103.8198),
            ),
            onStop = {},
        )
    }
}
