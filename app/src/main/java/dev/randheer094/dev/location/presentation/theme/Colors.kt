package dev.randheer094.dev.location.presentation.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Light theme colors
private val PrimaryLight = Color(0xFF6750A4)
private val OnPrimaryLight = Color(0xFFFFFFFF)
private val PrimaryContainerLight = Color(0xFFEADDFF)
private val OnPrimaryContainerLight = Color(0xFF21005D)
private val SecondaryLight = Color(0xFF625B71)
private val OnSecondaryLight = Color(0xFFFFFFFF)
private val SecondaryContainerLight = Color(0xFFE8DEF8)
private val OnSecondaryContainerLight = Color(0xFF1D192B)
private val TertiaryLight = Color(0xFF7D5260)
private val OnTertiaryLight = Color(0xFFFFFFFF)
private val TertiaryContainerLight = Color(0xFFFFD8E4)
private val OnTertiaryContainerLight = Color(0xFF31111D)
private val ErrorLight = Color(0xFFB3261E)
private val OnErrorLight = Color(0xFFFFFFFF)
private val ErrorContainerLight = Color(0xFFF9DEDC)
private val OnErrorContainerLight = Color(0xFF410E0B)
private val BackgroundLight = Color(0xFFFEF7FF)
private val OnBackgroundLight = Color(0xFF1D1B20)
private val SurfaceLight = Color(0xFFFEF7FF)
private val OnSurfaceLight = Color(0xFF1D1B20)
private val SurfaceVariantLight = Color(0xFFE7E0EC)
private val OnSurfaceVariantLight = Color(0xFF49454F)
private val OutlineLight = Color(0xFF79747E)
private val OutlineVariantLight = Color(0xFFCAC4D0)
private val SurfaceContainerLowestLight = Color(0xFFFFFFFF)
private val SurfaceContainerLowLight = Color(0xFFF7F2FA)
private val SurfaceContainerLightColor = Color(0xFFF3EDF7)
private val SurfaceContainerHighLight = Color(0xFFECE6F0)
private val SurfaceContainerHighestLight = Color(0xFFE6E0E9)

// Dark theme colors
private val PrimaryDark = Color(0xFFD0BCFF)
private val OnPrimaryDark = Color(0xFF381E72)
private val PrimaryContainerDark = Color(0xFF4F378B)
private val OnPrimaryContainerDark = Color(0xFFEADDFF)
private val SecondaryDark = Color(0xFFCCC2DC)
private val OnSecondaryDark = Color(0xFF332D41)
private val SecondaryContainerDark = Color(0xFF4A4458)
private val OnSecondaryContainerDark = Color(0xFFE8DEF8)
private val TertiaryDark = Color(0xFFEFB8C8)
private val OnTertiaryDark = Color(0xFF492532)
private val TertiaryContainerDark = Color(0xFF633B48)
private val OnTertiaryContainerDark = Color(0xFFFFD8E4)
private val ErrorDark = Color(0xFFF2B8B5)
private val OnErrorDark = Color(0xFF601410)
private val ErrorContainerDark = Color(0xFF8C1D18)
private val OnErrorContainerDark = Color(0xFFF9DEDC)
private val BackgroundDark = Color(0xFF141218)
private val OnBackgroundDark = Color(0xFFE6E0E9)
private val SurfaceDark = Color(0xFF141218)
private val OnSurfaceDark = Color(0xFFE6E0E9)
private val SurfaceVariantDark = Color(0xFF49454F)
private val OnSurfaceVariantDark = Color(0xFFCAC4D0)
private val OutlineDark = Color(0xFF938F99)
private val OutlineVariantDark = Color(0xFF49454F)
private val SurfaceContainerLowestDark = Color(0xFF0F0D13)
private val SurfaceContainerLowDark = Color(0xFF1D1B20)
private val SurfaceContainerDarkColor = Color(0xFF211F26)
private val SurfaceContainerHighDark = Color(0xFF2B2930)
private val SurfaceContainerHighestDark = Color(0xFF36343B)

internal val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryContainerDark,
    onPrimaryContainer = OnPrimaryContainerDark,
    secondary = SecondaryDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = OnSecondaryContainerDark,
    tertiary = TertiaryDark,
    onTertiary = OnTertiaryDark,
    tertiaryContainer = TertiaryContainerDark,
    onTertiaryContainer = OnTertiaryContainerDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    surfaceContainerLowest = SurfaceContainerLowestDark,
    surfaceContainerLow = SurfaceContainerLowDark,
    surfaceContainer = SurfaceContainerDarkColor,
    surfaceContainerHigh = SurfaceContainerHighDark,
    surfaceContainerHighest = SurfaceContainerHighestDark,
)

internal val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryContainerLight,
    onPrimaryContainer = OnPrimaryContainerLight,
    secondary = SecondaryLight,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryContainerLight,
    onSecondaryContainer = OnSecondaryContainerLight,
    tertiary = TertiaryLight,
    onTertiary = OnTertiaryLight,
    tertiaryContainer = TertiaryContainerLight,
    onTertiaryContainer = OnTertiaryContainerLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    surfaceContainerLowest = SurfaceContainerLowestLight,
    surfaceContainerLow = SurfaceContainerLowLight,
    surfaceContainer = SurfaceContainerLightColor,
    surfaceContainerHigh = SurfaceContainerHighLight,
    surfaceContainerHighest = SurfaceContainerHighestLight,
)
