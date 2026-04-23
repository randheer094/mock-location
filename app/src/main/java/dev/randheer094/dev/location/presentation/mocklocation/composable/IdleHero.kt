package dev.randheer094.dev.location.presentation.mocklocation.composable

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.randheer094.dev.location.R
import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.presentation.mocklocation.state.UiState
import dev.randheer094.dev.location.presentation.theme.InterFamily
import dev.randheer094.dev.location.presentation.theme.LocalMockColors
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme

@Composable
fun IdleHero(
    uiState: UiState,
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalMockColors.current
    val location = uiState.selected

    Column(modifier = modifier) {
        // Row 1: StatusPill + "Ready"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StatusPill(
                label = stringResource(R.string.status_mock_off),
                active = false,
            )
            Text(
                text = stringResource(R.string.status_ready),
                style = TextStyle(
                    fontFamily = InterFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                ),
                color = colors.textMute,
            )
        }

        if (location != null) {
            Spacer(Modifier.height(12.dp))

            // MiniMap
            MiniMap(
                lat = location.lat,
                lng = location.long,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(14.dp)),
            )

            Spacer(Modifier.height(12.dp))

            // Bottom row: LAST USED + city name on left, Start button on right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.overline_last_used),
                        style = TextStyle(
                            fontFamily = InterFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.6.sp,
                        ),
                        color = colors.textDim,
                    )
                    Text(
                        text = location.name.substringBefore(','),
                        style = TextStyle(
                            fontFamily = InterFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            letterSpacing = (-0.2).sp,
                        ),
                        color = colors.text,
                    )
                }

                // Start pill button
                Button(
                    onClick = onStart,
                    modifier = Modifier
                        .height(52.dp)
                        .shadow(
                            elevation = 14.dp,
                            shape = RoundedCornerShape(26.dp),
                            spotColor = colors.liveGlow,
                        ),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.accent,
                        contentColor = colors.accentInk,
                    ),
                ) {
                    Icon(imageVector = Icons.Filled.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.btn_start),
                        style = TextStyle(
                            fontFamily = InterFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                        ),
                    )
                }
            }
        }
    }
}

@Preview(name = "IdleHero with location – Light", showBackground = true)
@Composable
private fun IdleHeroWithLocationLightPreview() {
    MockLocationTheme(darkTheme = false) {
        IdleHero(
            uiState = UiState(
                showInstructions = false,
                status = false,
                hasNotificationPermission = true,
                items = emptyList(),
                elapsedLabel = "",
                selected = MockLocation(name = "Singapore, Singapore", lat = 1.3521, long = 103.8198),
            ),
            onStart = {},
        )
    }
}

@Preview(name = "IdleHero no location – Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun IdleHeroNoLocationDarkPreview() {
    MockLocationTheme(darkTheme = true) {
        IdleHero(
            uiState = UiState(
                showInstructions = false,
                status = false,
                hasNotificationPermission = true,
                items = emptyList(),
                elapsedLabel = "",
                selected = null,
            ),
            onStart = {},
        )
    }
}
