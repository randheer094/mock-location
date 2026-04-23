package dev.randheer094.dev.location.presentation.mocklocation.composable

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.randheer094.dev.location.presentation.theme.LocalMockColors
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme

@Composable
fun MiniMap(
    lat: Double,
    lng: Double,
    modifier: Modifier = Modifier,
) {
    val colors = LocalMockColors.current
    val mapBg = colors.mapBg
    val mapLine = colors.mapLine
    val mapLineStrong = colors.mapLineStrong
    val accentColor = colors.accent

    Canvas(modifier = modifier) {
        drawRect(color = mapBg)

        val width = size.width
        val height = size.height

        // Latitude grid lines (horizontal) every 30 degrees
        for (latLine in -90..90 step 30) {
            val y = ((90 - latLine) / 180f) * height
            val isEquator = latLine == 0
            drawLine(
                color = if (isEquator) mapLineStrong else mapLine,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = if (isEquator) 1.5f else 1f,
            )
        }

        // Longitude grid lines (vertical) every 45 degrees
        for (lngLine in -180..180 step 45) {
            val x = ((lngLine + 180) / 360f) * width
            val isPrimeMeridian = lngLine == 0
            drawLine(
                color = if (isPrimeMeridian) mapLineStrong else mapLine,
                start = Offset(x, 0f),
                end = Offset(x, height),
                strokeWidth = if (isPrimeMeridian) 1.5f else 1f,
            )
        }

        // Pin using equirectangular projection
        val px = ((lng + 180) / 360.0 * width).toFloat().coerceIn(0f, width)
        val py = ((90 - lat) / 180.0 * height).toFloat().coerceIn(0f, height)

        // Glow ring
        drawCircle(
            color = accentColor.copy(alpha = 0.25f),
            radius = 8.dp.toPx(),
            center = Offset(px, py),
        )
        // Pin dot
        drawCircle(
            color = accentColor,
            radius = 4.dp.toPx(),
            center = Offset(px, py),
        )
        // Center highlight
        drawCircle(
            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.7f),
            radius = 1.5.dp.toPx(),
            center = Offset(px, py),
        )
    }
}

@Preview(name = "MiniMap – Light", showBackground = true)
@Composable
private fun MiniMapLightPreview() {
    MockLocationTheme(darkTheme = false) {
        MiniMap(lat = 1.3521, lng = 103.8198, modifier = Modifier.size(width = 200.dp, height = 150.dp))
    }
}

@Preview(name = "MiniMap – Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MiniMapDarkPreview() {
    MockLocationTheme(darkTheme = true) {
        MiniMap(lat = 1.3521, lng = 103.8198, modifier = Modifier.size(width = 200.dp, height = 150.dp))
    }
}
