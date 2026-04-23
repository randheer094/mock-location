package dev.randheer094.dev.location.presentation.mocklocation.composable

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import dev.randheer094.dev.location.presentation.theme.LocalMockColors
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme

@Composable
fun Radar(modifier: Modifier = Modifier) {
    val colors = LocalMockColors.current
    var isRunning by remember { mutableStateOf(true) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            isRunning = event != Lifecycle.Event.ON_PAUSE
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "radar")

    val sweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "sweep",
    )

    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 3f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "pulse_radius",
    )

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "pulse_alpha",
    )

    val accentColor = colors.accent
    val borderColor = colors.borderStrong

    Canvas(modifier = modifier.size(120.dp)) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val maxRadius = size.minDimension / 2f

        // Concentric rings
        for (i in 1..4) {
            val r = maxRadius * i / 4f
            drawCircle(
                color = borderColor,
                radius = r,
                center = Offset(cx, cy),
                style = Stroke(width = 1.dp.toPx()),
            )
        }

        // Sweep gradient arc
        val displaySweep = if (isRunning) sweepAngle else 0f
        drawArc(
            brush = Brush.sweepGradient(
                colors = listOf(
                    accentColor.copy(alpha = 0f),
                    accentColor.copy(alpha = 0.5f),
                    accentColor,
                ),
                center = Offset(cx, cy),
            ),
            startAngle = displaySweep - 90f,
            sweepAngle = 90f,
            useCenter = true,
            topLeft = Offset(cx - maxRadius, cy - maxRadius),
            size = Size(maxRadius * 2f, maxRadius * 2f),
        )

        // Pulsing center ring
        val displayPulseRadius = if (isRunning) pulseRadius else 3f
        val displayPulseAlpha = if (isRunning) pulseAlpha else 0.8f
        drawCircle(
            color = accentColor.copy(alpha = displayPulseAlpha),
            radius = displayPulseRadius.dp.toPx(),
            center = Offset(cx, cy),
            style = Stroke(width = 1.dp.toPx()),
        )

        // Center dot
        drawCircle(
            color = accentColor,
            radius = 3.dp.toPx(),
            center = Offset(cx, cy),
        )
    }
}

@Preview(name = "Radar – Light", showBackground = true)
@Composable
private fun RadarLightPreview() {
    MockLocationTheme(darkTheme = false) {
        Radar()
    }
}

@Preview(name = "Radar – Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RadarDarkPreview() {
    MockLocationTheme(darkTheme = true) {
        Radar()
    }
}
