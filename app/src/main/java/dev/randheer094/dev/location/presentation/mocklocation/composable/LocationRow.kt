package dev.randheer094.dev.location.presentation.mocklocation.composable

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.randheer094.dev.location.R
import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.presentation.mocklocation.state.UiStateMapper
import dev.randheer094.dev.location.presentation.theme.InterFamily
import dev.randheer094.dev.location.presentation.theme.JetBrainsMonoFamily
import dev.randheer094.dev.location.presentation.theme.LocalMockColors
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme

@Composable
fun LocationRow(
    location: MockLocation,
    isSelected: Boolean,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = LocalMockColors.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "row_scale",
    )

    val bgColor = if (isSelected) colors.card else Color.Transparent
    val borderStroke = if (isSelected) BorderStroke(1.dp, colors.borderStrong) else null

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = borderStroke,
        interactionSource = interactionSource,
        modifier = modifier
            .fillMaxWidth()
            .scale(scale),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Country code tile
            val countryCode = UiStateMapper.getCountryCode(location.name)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isActive) colors.accentSoft else colors.chipBg,
                border = BorderStroke(1.dp, colors.border),
                modifier = Modifier.size(44.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = countryCode,
                        style = TextStyle(
                            fontFamily = JetBrainsMonoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp,
                        ),
                        color = if (isActive) colors.accent else colors.textDim,
                    )
                }
            }

            // City name + coordinates column
            Column(modifier = Modifier.weight(1f)) {
                if (isActive) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .drawBehind {
                                    drawCircle(
                                        color = colors.live,
                                        radius = size.minDimension / 2f,
                                    )
                                },
                        )
                        Text(
                            text = stringResource(R.string.badge_live),
                            style = TextStyle(
                                fontFamily = InterFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.6.sp,
                            ),
                            color = colors.accent,
                        )
                    }
                    Spacer(Modifier.height(1.dp))
                }
                Text(
                    text = location.name.substringBefore(','),
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        letterSpacing = (-0.2).sp,
                    ),
                    color = colors.text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "%.4f, %.4f".format(location.lat, location.long),
                    style = TextStyle(
                        fontFamily = JetBrainsMonoFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                    ),
                    color = colors.textDim,
                )
            }

            // MiniMap thumbnail
            MiniMap(
                lat = location.lat,
                lng = location.long,
                modifier = Modifier
                    .size(width = 64.dp, height = 44.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
        }
    }
}

@Preview(name = "LocationRow selected active – Light", showBackground = true)
@Composable
private fun LocationRowSelectedActiveLightPreview() {
    MockLocationTheme(darkTheme = false) {
        LocationRow(
            location = MockLocation(name = "Singapore, Singapore", lat = 1.3521, long = 103.8198),
            isSelected = true,
            isActive = true,
            onClick = {},
        )
    }
}

@Preview(name = "LocationRow idle – Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LocationRowIdleDarkPreview() {
    MockLocationTheme(darkTheme = true) {
        LocationRow(
            location = MockLocation(name = "Stockholm, Sweden", lat = 59.3293, long = 18.0686),
            isSelected = false,
            isActive = false,
            onClick = {},
        )
    }
}
