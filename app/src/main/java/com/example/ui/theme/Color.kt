package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ========================================================================
// ATMOSPHERIC / IMMERSIVE MEDIA COLOR SYSTEM: DARK FANTASY RPG
// ========================================================================

// Abyssal & Deep Obsidian Foundations
val DemonNavyBlack = Color(0xFF050508)
val DemonNavyDark = Color(0xFF080910)
val DemonNavySurface = Color(0xFF0D0F1A)
val DemonNavyCard = Color(0xFF131726)
val DemonNavyGlass = Color(0xB3050508)

// Atmospheric Ambient Auras & Glassmorphism
val AtmosphericBg = Color(0xFF050508)
val AtmosphericCrimsonOrb = Color(0xFF990000)
val AtmosphericIndigoOrb = Color(0xFF0F172A)
val AtmosphericVoidOrb = Color(0xFF1E1B4B)
val AtmosphericGlassBlack = Color(0x99000000) // black/60
val AtmosphericBorderWhite10 = Color(0x1AFFFFFF) // white/10
val AtmosphericBorderWhite20 = Color(0x33FFFFFF) // white/20
val AtmosphericBorderGlow = Color(0x40EF4444)

// Neon & Glowing Crimson Accent Palette (Blood & Combat)
val DemonCrimson = Color(0xFFDC2626)
val DemonCrimsonDark = Color(0xFF991B1B)
val DemonCrimsonGlow = Color(0xFFEF4444)
val DemonBloodRed = Color(0xFFB91C1C)
val NeonCrimson = Color(0xFFFF1E46)
val NeonCrimsonAura = Color(0x66FF1E46)

// Neon Soul Cyan / Aether Breathing (Spirit & Stamina)
val DemonAetherCyan = Color(0xFF22D3EE)
val DemonAetherBlue = Color(0xFF06B6D4)
val DemonAetherDeep = Color(0xFF0284C7)
val NeonCyan = Color(0xFF00F0FF)
val NeonCyanAura = Color(0x6600F0FF)

// Glowing Amber / Divine Sun Gold (Solar & Critical Hits)
val DemonGold = Color(0xFFFFD166)
val DemonGoldDark = Color(0xFFD97706)
val DemonAmber = Color(0xFFF59E0B)
val NeonGold = Color(0xFFFFD700)
val NeonSunfire = Color(0xFFFDE047)

// Emerald Vitality (HP & Safe Zones)
val AtmosphericEmerald = Color(0xFF10B981)
val AtmosphericEmeraldLight = Color(0xFF34D399)
val NeonEmerald = Color(0xFF10F49C)

// Void Purple & Astral Arcana
val DemonMoonPurple = Color(0xFFA855F7)
val NeonViolet = Color(0xFFC084FC)
val VoidAbyss = Color(0xFF3B0764)

// Elemental Styles
val DemonThunderYellow = Color(0xFFFACC15)
val DemonWindGreen = Color(0xFF10B981)
val DemonBlossomPink = Color(0xFFF472B6)
val DemonSolarSun = Color(0xFFFB923C)

// High-Contrast Dark Fantasy Typography Shades
val DemonTextLight = Color(0xFFF8FAFC) // Crisp moonlight white (primary)
val DemonMistGray = Color(0xFF94A3B8)  // Secondary info silver
val DemonTextMuted = Color(0xFF64748B) // Subtitle / inactive slate
val DemonTextGlow = Color(0xFFFFFFFF)

// Curated Glowing Atmosphere Gradients
val DemonCrimsonGradient = Brush.horizontalGradient(
    listOf(Color(0xFF7F1D1D), Color(0xFFDC2626), Color(0xFFFF1E46))
)

val DemonCyanGradient = Brush.horizontalGradient(
    listOf(Color(0xFF0C4A6E), Color(0xFF06B6D4), Color(0xFF00F0FF))
)

val DemonGoldGradient = Brush.horizontalGradient(
    listOf(Color(0xFF78350F), Color(0xFFF59E0B), Color(0xFFFFD166))
)

val DemonVoidGradient = Brush.horizontalGradient(
    listOf(Color(0xFF1E1B4B), Color(0xFF7C3AED), Color(0xFFC084FC))
)
