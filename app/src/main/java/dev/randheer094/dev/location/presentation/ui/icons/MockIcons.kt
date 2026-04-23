package dev.randheer094.dev.location.presentation.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object MockIcons {
    val Pin: ImageVector by lazy {
        ImageVector.Builder(
            name = "Pin",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.EvenOdd
            ) {
                // Outer teardrop — round top, pointed bottom
                moveTo(12f, 2f)
                curveTo(8.13f, 2f, 5f, 5.13f, 5f, 9f)
                curveTo(5f, 14.25f, 12f, 22f, 12f, 22f)
                curveTo(12f, 22f, 19f, 14.25f, 19f, 9f)
                curveTo(19f, 5.13f, 15.87f, 2f, 12f, 2f)
                close()
                // Inner circle cutout
                moveTo(14.5f, 9f)
                curveTo(14.5f, 7.62f, 13.38f, 6.5f, 12f, 6.5f)
                curveTo(10.62f, 6.5f, 9.5f, 7.62f, 9.5f, 9f)
                curveTo(9.5f, 10.38f, 10.62f, 11.5f, 12f, 11.5f)
                curveTo(13.38f, 11.5f, 14.5f, 10.38f, 14.5f, 9f)
                close()
            }
        }.build()
    }
}
