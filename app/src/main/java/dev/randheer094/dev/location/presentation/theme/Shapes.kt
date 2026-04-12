package dev.randheer094.dev.location.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Shape scale per Material 3 guidelines. Values are slightly more generous than the default
 * scale so cards and bottom sheets feel closer to the "expressive" rounded-corner language
 * introduced in the 2025 Material 3 refresh.
 */
internal val Shapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)
