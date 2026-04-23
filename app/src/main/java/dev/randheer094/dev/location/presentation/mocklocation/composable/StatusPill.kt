package dev.randheer094.dev.location.presentation.mocklocation.composable

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.randheer094.dev.location.presentation.theme.InterFamily
import dev.randheer094.dev.location.presentation.theme.LocalMockColors
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme

@Composable
fun StatusPill(
    label: String,
    active: Boolean,
    modifier: Modifier = Modifier,
) {
    val colors = LocalMockColors.current
    val fillColor = if (active) colors.accentSoft else colors.chipBg
    val borderColor = if (active) colors.accentGhost else colors.border
    val dotColor = if (active) colors.live else colors.textMute
    val glowColor = colors.liveGlow
    val labelColor = if (active) colors.text else colors.textDim

    val infiniteTransition = rememberInfiniteTransition(label = "pill_dot")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "glow_alpha",
    )

    Surface(
        shape = CircleShape,
        color = fillColor,
        modifier = modifier.border(1.dp, borderColor, CircleShape),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(7.dp)
                    .drawBehind {
                        if (active) {
                            drawCircle(
                                color = glowColor.copy(alpha = glowAlpha),
                                radius = (size.minDimension / 2f) + 3.dp.toPx(),
                            )
                        }
                        drawCircle(
                            color = dotColor,
                            radius = size.minDimension / 2f,
                        )
                    },
            )
            Text(
                text = label,
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = InterFamily,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                    fontSize = 12.sp,
                    letterSpacing = 0.2.sp,
                ),
                color = labelColor,
            )
        }
    }
}

@Preview(name = "Active – Light", showBackground = true)
@Composable
private fun StatusPillActiveLightPreview() {
    MockLocationTheme(darkTheme = false) {
        StatusPill(label = "LIVE · GPS + NET", active = true)
    }
}

@Preview(name = "Active – Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StatusPillActiveDarkPreview() {
    MockLocationTheme(darkTheme = true) {
        StatusPill(label = "LIVE · GPS + NET", active = true)
    }
}

@Preview(name = "Inactive – Light", showBackground = true)
@Composable
private fun StatusPillInactiveLightPreview() {
    MockLocationTheme(darkTheme = false) {
        StatusPill(label = "Mock location off", active = false)
    }
}

@Preview(name = "Inactive – Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StatusPillInactiveDarkPreview() {
    MockLocationTheme(darkTheme = true) {
        StatusPill(label = "Mock location off", active = false)
    }
}
