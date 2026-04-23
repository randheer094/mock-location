package dev.randheer094.dev.location.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Dark brand palette
private val DarkBg = Color(0xFF0A0E1A)
private val DarkBgElev = Color(0xFF121829)
private val DarkCard = Color(0xFF141B2C)
private val DarkText = Color(0xFFEDF1F7)
private val DarkTextDim = Color(0xFF9AA5BD)
private val DarkAccent = Color(0xFF6AB8FF)
private val DarkAccentInk = Color(0xFF04131A)
private val DarkDanger = Color(0xFFFF7A85)
private val DarkBorderStrong = Color.White.copy(alpha = 0.12f)
private val DarkSurfaceVariant = Color(0xFF1A2236)
private val DarkOnSurfaceVariant = Color(0xFF9AA5BD)

// Light brand palette
private val LightBg = Color(0xFFF3F1EC)
private val LightBgElev = Color.White
private val LightCard = Color.White
private val LightText = Color(0xFF0B1020)
private val LightTextDim = Color(0xFF55607A)
private val LightAccent = Color(0xFF1B7CC9)
private val LightAccentInk = Color.White
private val LightDanger = Color(0xFFC64454)
private val LightBorderStrong = Color(0xFF0B1020).copy(alpha = 0.13f)
private val LightSurfaceVariant = Color(0xFFF8F6F1)
private val LightOnSurfaceVariant = Color(0xFF55607A)

internal val DarkColorScheme = darkColorScheme(
    primary = DarkAccent,
    onPrimary = DarkAccentInk,
    primaryContainer = Color(0xFF1A3550),
    onPrimaryContainer = DarkAccent,
    secondary = DarkTextDim,
    onSecondary = DarkBg,
    secondaryContainer = DarkCard,
    onSecondaryContainer = DarkText,
    tertiary = Color(0xFF5CB8FF),
    onTertiary = DarkAccentInk,
    tertiaryContainer = Color(0xFF0B1E30),
    onTertiaryContainer = Color(0xFF5CB8FF),
    error = DarkDanger,
    onError = DarkAccentInk,
    errorContainer = Color(0xFF3D1418),
    onErrorContainer = DarkDanger,
    background = DarkBg,
    onBackground = DarkText,
    surface = DarkBg,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkBorderStrong,
    outlineVariant = Color.White.copy(alpha = 0.06f),
    surfaceContainerLowest = DarkBg,
    surfaceContainerLow = DarkBgElev,
    surfaceContainer = DarkCard,
    surfaceContainerHigh = DarkSurfaceVariant,
    surfaceContainerHighest = Color(0xFF1A2236),
)

internal val LightColorScheme = lightColorScheme(
    primary = LightAccent,
    onPrimary = LightAccentInk,
    primaryContainer = Color(0xFFD0E8F8),
    onPrimaryContainer = Color(0xFF0A3A5E),
    secondary = LightTextDim,
    onSecondary = LightBgElev,
    secondaryContainer = LightSurfaceVariant,
    onSecondaryContainer = LightText,
    tertiary = Color(0xFF0E77CC),
    onTertiary = LightAccentInk,
    tertiaryContainer = Color(0xFFCCE5F8),
    onTertiaryContainer = Color(0xFF083D66),
    error = LightDanger,
    onError = LightAccentInk,
    errorContainer = Color(0xFFF8D7DA),
    onErrorContainer = Color(0xFF6B1824),
    background = LightBg,
    onBackground = LightText,
    surface = LightBg,
    onSurface = LightText,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightBorderStrong,
    outlineVariant = Color(0xFF0B1020).copy(alpha = 0.07f),
    surfaceContainerLowest = LightBg,
    surfaceContainerLow = LightBgElev,
    surfaceContainer = LightCard,
    surfaceContainerHigh = LightSurfaceVariant,
    surfaceContainerHighest = Color(0xFFF8F6F1),
)
