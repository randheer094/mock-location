package dev.randheer094.dev.location.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class MockColors(
    val bg: Color,
    val bgElev: Color,
    val bgElev2: Color,
    val card: Color,
    val border: Color,
    val borderStrong: Color,
    val text: Color,
    val textDim: Color,
    val textMute: Color,
    val accent: Color,
    val accentInk: Color,
    val accentSoft: Color,
    val accentGhost: Color,
    val live: Color,
    val liveGlow: Color,
    val mapBg: Color,
    val mapLine: Color,
    val mapLineStrong: Color,
    val danger: Color,
    val chipBg: Color,
)

fun darkMockColors(): MockColors {
    val accent = Color(0xFF6AB8FF)
    val live = Color(0xFF5CB8FF)
    return MockColors(
        bg = Color(0xFF0A0E1A),
        bgElev = Color(0xFF121829),
        bgElev2 = Color(0xFF1A2236),
        card = Color(0xFF141B2C),
        border = Color.White.copy(alpha = 0.06f),
        borderStrong = Color.White.copy(alpha = 0.12f),
        text = Color(0xFFEDF1F7),
        textDim = Color(0xFF9AA5BD),
        textMute = Color(0xFF5E6A83),
        accent = accent,
        accentInk = Color(0xFF04131A),
        accentSoft = accent.copy(alpha = 0.14f),
        accentGhost = accent.copy(alpha = 0.24f),
        live = live,
        liveGlow = live.copy(alpha = 0.35f),
        mapBg = Color(0xFF0B1322),
        mapLine = Color(0xFF8CB4DC).copy(alpha = 0.08f),
        mapLineStrong = Color(0xFF8CB4DC).copy(alpha = 0.14f),
        danger = Color(0xFFFF7A85),
        chipBg = Color.White.copy(alpha = 0.04f),
    )
}

fun lightMockColors(): MockColors {
    val accent = Color(0xFF1B7CC9)
    val live = Color(0xFF0E77CC)
    return MockColors(
        bg = Color(0xFFF3F1EC),
        bgElev = Color.White,
        bgElev2 = Color(0xFFF8F6F1),
        card = Color.White,
        border = Color(0xFF0B1020).copy(alpha = 0.07f),
        borderStrong = Color(0xFF0B1020).copy(alpha = 0.13f),
        text = Color(0xFF0B1020),
        textDim = Color(0xFF55607A),
        textMute = Color(0xFF8A93A8),
        accent = accent,
        accentInk = Color.White,
        accentSoft = accent.copy(alpha = 0.10f),
        accentGhost = accent.copy(alpha = 0.18f),
        live = live,
        liveGlow = live.copy(alpha = 0.28f),
        mapBg = Color(0xFFEAE7DE),
        mapLine = Color(0xFF0B1020).copy(alpha = 0.05f),
        mapLineStrong = Color(0xFF0B1020).copy(alpha = 0.10f),
        danger = Color(0xFFC64454),
        chipBg = Color(0xFF0B1020).copy(alpha = 0.04f),
    )
}

val LocalMockColors = staticCompositionLocalOf { lightMockColors() }
