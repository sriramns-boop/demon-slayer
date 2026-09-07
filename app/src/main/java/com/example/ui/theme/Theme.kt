package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ========================================================================
// ATMOSPHERIC / IMMERSIVE MEDIA DARK FANTASY COLOR SCHEME
// ========================================================================

val AtmosphericDarkColorScheme: ColorScheme = darkColorScheme(
    // Neon Crimson Blood / Combat Accents
    primary = NeonCrimson,
    onPrimary = Color.White,
    primaryContainer = DemonCrimsonDark,
    onPrimaryContainer = Color(0xFFFFD1D9),

    // Neon Soul Cyan / Aether Breathing (Spirit & Stamina)
    secondary = NeonCyan,
    onSecondary = Color(0xFF03161C),
    secondaryContainer = Color(0xFF082F49),
    onSecondaryContainer = Color(0xFFBAF5FF),

    // Neon Gold / Solar Spark (Critical Hits & Loot)
    tertiary = NeonGold,
    onTertiary = Color(0xFF261900),
    tertiaryContainer = Color(0xFF451A03),
    onTertiaryContainer = NeonSunfire,

    // Abyssal Darkness & Obsidian Foundations
    background = DemonNavyBlack,
    onBackground = DemonTextLight,

    // Deep Translucent Glass & Card Surfaces
    surface = DemonNavySurface,
    onSurface = DemonTextLight,
    surfaceVariant = DemonNavyCard,
    onSurfaceVariant = DemonMistGray,
    surfaceTint = NeonCrimson,

    // Inverted Surfaces
    inverseSurface = DemonTextLight,
    inverseOnSurface = DemonNavyBlack,
    inversePrimary = DemonCrimsonDark,

    // Translucent Glowing Borders & Outlines
    outline = Color(0x40EF4444), // Neon Crimson glow edge
    outlineVariant = AtmosphericBorderWhite10, // Subtle glass edge (white/10)

    // Apocalyptic Error / Warning States
    error = NeonCrimson,
    onError = Color.White,
    errorContainer = Color(0xFF450A0A),
    onErrorContainer = Color(0xFFFFD1D9)
)

/**
 * Extended Glow & Aura Palette for the Atmospheric / Immersive Media design language.
 */
data class AtmosphericGlowPalette(
    val neonCrimson: Color = NeonCrimson,
    val neonCyan: Color = NeonCyan,
    val neonGold: Color = NeonGold,
    val neonEmerald: Color = NeonEmerald,
    val neonViolet: Color = NeonViolet,
    val glassBorder: Color = AtmosphericBorderWhite10,
    val glassBorderActive: Color = AtmosphericBorderWhite20,
    val glassBackground: Color = AtmosphericGlassBlack,
    val crimsonGradient: Brush = DemonCrimsonGradient,
    val cyanGradient: Brush = DemonCyanGradient,
    val goldGradient: Brush = DemonGoldGradient,
    val voidGradient: Brush = DemonVoidGradient
)

val LocalAtmosphericGlow = staticCompositionLocalOf { AtmosphericGlowPalette() }

/**
 * Direct accessor for atmospheric glows and media gradients on [MaterialTheme].
 */
val MaterialTheme.atmosphericGlow: AtmosphericGlowPalette
    @Composable
    @ReadOnlyComposable
    get() = LocalAtmosphericGlow.current

/**
 * Root theme applying the 'Atmospheric / Immersive Media' design language.
 * Enforces deep obsidian foundations, radiant neon glowing accents,
 * high-contrast dark fantasy typography, and glassmorphic depth.
 */
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val glowPalette = AtmosphericGlowPalette()

    CompositionLocalProvider(
        LocalAtmosphericGlow provides glowPalette
    ) {
        MaterialTheme(
            colorScheme = AtmosphericDarkColorScheme,
            typography = Typography,
            content = content
        )
    }
}
