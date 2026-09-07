package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * High-contrast dark fantasy RPG typography hierarchy.
 * Tailored for cinematic titles, dramatic boss announcements, sharp combat labels,
 * and legible lore/dialogue against deep obsidian surfaces.
 */
val Typography = Typography(
    // Cinematic Game Titles & Final Boss Display
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 36.sp,
        lineHeight = 42.sp,
        letterSpacing = 4.sp,
        color = DemonTextLight
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Black,
        fontStyle = FontStyle.Italic,
        fontSize = 28.sp,
        lineHeight = 34.sp,
        letterSpacing = 3.sp,
        color = DemonTextLight
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 2.5.sp,
        color = DemonTextLight
    ),

    // Screen Headings & Major Quest Titles
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Black,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 2.sp,
        color = DemonTextLight
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        lineHeight = 23.sp,
        letterSpacing = 1.5.sp,
        color = DemonTextLight
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 21.sp,
        letterSpacing = 1.sp,
        color = DemonTextLight
    ),

    // Section Titles, Item Cards & Equipment Ranks
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 1.sp,
        color = DemonTextLight
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.5.sp,
        color = DemonTextLight
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.5.sp,
        color = DemonMistGray
    ),

    // Narrative Dialogue, NPC Lore & Item Descriptions
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.25.sp,
        color = DemonTextLight
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 17.sp,
        letterSpacing = 0.25.sp,
        color = DemonMistGray
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.4.sp,
        color = DemonTextMuted
    ),

    // HUD Action Badges, Skill Hotkeys & Cooldown Timers
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Black,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 1.5.sp,
        color = DemonTextLight
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 9.sp,
        lineHeight = 13.sp,
        letterSpacing = 1.sp,
        color = DemonTextLight
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 8.sp,
        lineHeight = 11.sp,
        letterSpacing = 0.5.sp,
        color = DemonMistGray
    )
)
