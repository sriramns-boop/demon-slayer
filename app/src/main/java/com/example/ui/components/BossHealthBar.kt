package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BossState
import com.example.ui.theme.*

/**
 * Visual styling theme configuration for each combat phase of the boss encounter.
 */
data class BossPhaseVisualTheme(
    val phaseNumber: Int,
    val phaseName: String,
    val subtitle: String,
    val icon: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val accentGlow: Color,
    val healthGradient: List<Color>,
    val badgeBgColor: Color,
    val badgeBorderColor: Color,
    val auraColor: Color,
    val themeDescription: String
)

val Phase1BloodMonarch = BossPhaseVisualTheme(
    phaseNumber = 1,
    phaseName = "BLOOD MONARCH",
    subtitle = "Harbinger of the Crimson Eclipse",
    icon = "🩸",
    primaryColor = Color(0xFFDC2626),
    secondaryColor = Color(0xFF991B1B),
    accentGlow = Color(0xFFEF4444),
    healthGradient = listOf(Color(0xFF7F1D1D), Color(0xFFDC2626), Color(0xFFF43F5E)),
    badgeBgColor = Color(0x4DDC2626),
    badgeBorderColor = Color(0xFFEF4444),
    auraColor = Color(0x66DC2626),
    themeDescription = "Blood Crystal Auras & Crimson Slashes"
)

val Phase2DemonTitan = BossPhaseVisualTheme(
    phaseNumber = 2,
    phaseName = "DEMON TITAN",
    subtitle = "Volcanic Calamity & Earth Breaker",
    icon = "🔥",
    primaryColor = Color(0xFFEA580C),
    secondaryColor = Color(0xFFC2410C),
    accentGlow = Color(0xFFF59E0B),
    healthGradient = listOf(Color(0xFF7C2D12), Color(0xFFEA580C), Color(0xFFFBBF24)),
    badgeBgColor = Color(0x4DEA580C),
    badgeBorderColor = Color(0xFFF59E0B),
    auraColor = Color(0x66EA580C),
    themeDescription = "Molten Obsidian Armor & Magma Quakes"
)

val Phase3ImmortalShadow = BossPhaseVisualTheme(
    phaseNumber = 3,
    phaseName = "IMMORTAL SHADOW",
    subtitle = "Void Sovereign of Eternal Night",
    icon = "🌑",
    primaryColor = Color(0xFF8B5CF6),
    secondaryColor = Color(0xFF4C1D95),
    accentGlow = Color(0xFF06B6D4),
    healthGradient = listOf(Color(0xFF312E81), Color(0xFF7C3AED), Color(0xFF06B6D4)),
    badgeBgColor = Color(0x4D7C3AED),
    badgeBorderColor = Color(0xFF06B6D4),
    auraColor = Color(0x667C3AED),
    themeDescription = "Void Phantoms & Spectral Eclipse Mist"
)

val Phase4FinalDawn = BossPhaseVisualTheme(
    phaseNumber = 4,
    phaseName = "FINAL DAWN",
    subtitle = "Apocalyptic True Form - Dawnbringer Awakening",
    icon = "☀️",
    primaryColor = Color(0xFFF59E0B),
    secondaryColor = Color(0xFFB45309),
    accentGlow = Color(0xFFFEF08A),
    healthGradient = listOf(Color(0xFFB45309), Color(0xFFF59E0B), Color(0xFFFDE047), Color(0xFFFFFFFF)),
    badgeBgColor = Color(0x4DF59E0B),
    badgeBorderColor = Color(0xFFFEF08A),
    auraColor = Color(0x80F59E0B),
    themeDescription = "Blinding Solar Flares & Celestial Wrath"
)

fun getPhaseVisualTheme(phase: Int): BossPhaseVisualTheme {
    return when (phase) {
        1 -> Phase1BloodMonarch
        2 -> Phase2DemonTitan
        3 -> Phase3ImmortalShadow
        4 -> Phase4FinalDawn
        else -> Phase1BloodMonarch
    }
}

/**
 * Specialized Boss HUD component that displays a prominent boss name and health bar at the top,
 * including an interactive phase indicator system that dynamically transforms its visual style,
 * color palette, crest icons, and particle auras when the boss transitions between combat phases.
 */
@Composable
fun BossHealthBar(
    bossState: BossState,
    phaseAnnouncement: String? = null,
    onSelectPhase: ((Int) -> Unit)? = null,
    onAdvancePhase: (() -> Unit)? = null,
    onToggleEnrage: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val activeTheme = remember(bossState.currentPhase) {
        getPhaseVisualTheme(bossState.currentPhase)
    }

    // Health bar animation with trailing ghost damage effect
    val animatedHp by animateFloatAsState(
        targetValue = bossState.hpPercentage.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "bossHp"
    )
    val ghostHp by animateFloatAsState(
        targetValue = bossState.hpPercentage.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800, delayMillis = 200, easing = LinearOutSlowInEasing),
        label = "ghostBossHp"
    )

    // Pulsing aura animation for enrage and phase glow
    val infiniteTransition = rememberInfiniteTransition(label = "bossPhaseAura")
    val auraPulse by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auraPulse"
    )
    val enrageFlash by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "enrageFlash"
    )

    var showControls by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .testTag("boss_health_bar_container")
            .widthIn(max = 380.dp)
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ==================== PHASE ANNOUNCEMENT BANNER ====================
        AnimatedVisibility(
            visible = phaseAnnouncement != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xCC000000),
                                activeTheme.secondaryColor.copy(alpha = 0.9f),
                                Color(0xCC000000)
                            )
                        )
                    )
                    .border(1.5.dp, activeTheme.accentGlow, RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = activeTheme.icon, fontSize = 14.sp)
                    Text(
                        text = "⚠️ ${phaseAnnouncement?.uppercase()} ⚠️",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                    Text(text = activeTheme.icon, fontSize = 14.sp)
                }
            }
        }

        // ==================== BOSS NAME & THREAT HEADER ====================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Threat Level & Category
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(activeTheme.primaryColor)
                )
                Text(
                    text = "APEX DEMON • THREAT SS",
                    color = activeTheme.primaryColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.5.sp,
                    fontStyle = FontStyle.Italic
                )
            }

            // Enraged Indicator Badge
            if (bossState.isEnraged) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(DemonCrimson.copy(alpha = 0.35f * enrageFlash))
                        .border(1.dp, DemonCrimsonGlow, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 1.5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ElectricBolt,
                            contentDescription = "Enraged",
                            tint = DemonGold,
                            modifier = Modifier.size(10.dp)
                        )
                        Text(
                            text = "ENRAGED",
                            color = DemonCrimsonGlow,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Prominent Boss Name (stylized per active phase)
        Text(
            text = bossState.name.uppercase(),
            color = Color.White,
            fontSize = 21.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 4.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier
                .testTag("boss_name_title")
                .shadow(elevation = 8.dp, spotColor = activeTheme.primaryColor)
        )

        // Boss Phase Subtitle & Elemental Profile
        Text(
            text = "${activeTheme.phaseName} — ${activeTheme.subtitle.uppercase()}",
            color = activeTheme.accentGlow,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        // ==================== PHASE INDICATORS (DYNAMIC VISUAL STYLES) ====================
        // Segmented visual indicators that transform when transitioning between combat phases
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            (1..bossState.maxPhases).forEach { phaseNumber ->
                val isCurrentPhase = phaseNumber == bossState.currentPhase
                val isCompletedPhase = phaseNumber < bossState.currentPhase
                val phaseTheme = getPhaseVisualTheme(phaseNumber)

                val phaseRoman = when (phaseNumber) {
                    1 -> "I"
                    2 -> "II"
                    3 -> "III"
                    4 -> "IV"
                    else -> "$phaseNumber"
                }

                Box(
                    modifier = Modifier
                        .testTag("boss_phase_indicator_$phaseNumber")
                        .weight(1f)
                        .padding(horizontal = 3.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when {
                                isCurrentPhase -> phaseTheme.badgeBgColor
                                isCompletedPhase -> Color(0x33101018)
                                else -> Color(0x1A090A14)
                            }
                        )
                        .border(
                            width = if (isCurrentPhase) 1.5.dp else 1.dp,
                            color = when {
                                isCurrentPhase -> phaseTheme.badgeBorderColor.copy(alpha = auraPulse)
                                isCompletedPhase -> Color(0x4DFFFFFF)
                                else -> Color(0x20FFFFFF)
                            },
                            shape = RoundedCornerShape(6.dp)
                        )
                        .clickable { onSelectPhase?.invoke(phaseNumber) }
                        .padding(vertical = 4.dp, horizontal = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (isCurrentPhase) {
                            Text(
                                text = phaseTheme.icon,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(end = 3.dp)
                            )
                        }

                        Text(
                            text = when {
                                isCompletedPhase -> "✓ $phaseRoman"
                                isCurrentPhase -> "PHASE $phaseRoman"
                                else -> "PHASE $phaseRoman"
                            },
                            color = when {
                                isCurrentPhase -> Color.White
                                isCompletedPhase -> Color(0x80FFFFFF)
                                else -> Color(0x40FFFFFF)
                            },
                            fontSize = if (isCurrentPhase) 9.sp else 8.sp,
                            fontWeight = if (isCurrentPhase) FontWeight.Black else FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ==================== PROMINENT BOSS HEALTH BAR ====================
        // Segmented, phase-themed dual-layer health bar
        Box(
            modifier = Modifier
                .testTag("boss_health_bar")
                .fillMaxWidth()
                .height(11.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(Color(0xCC05050A))
                .border(
                    width = 1.2.dp,
                    color = activeTheme.badgeBorderColor.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(5.dp)
                )
        ) {
            // Trailing ghost damage bar (delayed yellow/white phantom that animates down)
            Box(
                modifier = Modifier
                    .fillMaxWidth(ghostHp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(5.dp))
                    .background(Color(0x80FFFFFF))
            )

            // Active health bar with phase-themed gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedHp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(5.dp))
                    .background(
                        Brush.horizontalGradient(activeTheme.healthGradient)
                    )
            )

            // Multi-phase divider notches
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(bossState.maxPhases - 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.5.dp)
                            .background(Color(0x66000000))
                    )
                }
            }

            // Health percentage overlay
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${(bossState.hpPercentage * 10000).toInt()} / 10,000 HP • ${(bossState.hpPercentage * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ==================== STAGGER / POSTURE METER ====================
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "STAGGER",
                color = DemonGold,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color(0x99000000))
                    .border(0.5.dp, Color(0x33FFFFFF), CircleShape)
                    .testTag("boss_stagger_bar")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(bossState.staggerPercentage.coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(DemonGoldDark, DemonGold, Color(0xFFFEF08A))
                            )
                        )
                )
            }

            Text(
                text = "${(bossState.staggerPercentage * 100).toInt()}%",
                color = DemonGold,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )

            // Toggle quick phase controls trigger
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0x26FFFFFF))
                    .border(0.5.dp, Color(0x40FFFFFF), RoundedCornerShape(3.dp))
                    .clickable { showControls = !showControls }
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Text(
                    text = if (showControls) "HIDE CONTROLS" else "PHASE OPS",
                    color = Color.White,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // ==================== INTERACTIVE PHASE TRANSITION CONTROLS ====================
        // Allows testing & manual triggering of phase transitions to immediately inspect visual changes
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xE60A0C16))
                    .border(1.dp, activeTheme.primaryColor.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                    .padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "COMBAT PHASE TRANSITION CONTROLS",
                    color = Color(0xCCFFFFFF),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Advance Phase button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(activeTheme.primaryColor.copy(alpha = 0.35f))
                            .border(1.dp, activeTheme.accentGlow, RoundedCornerShape(4.dp))
                            .clickable { onAdvancePhase?.invoke() }
                            .padding(vertical = 3.dp)
                            .testTag("boss_advance_phase_btn"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FastForward,
                                contentDescription = "Next Phase",
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "NEXT PHASE",
                                color = Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    // Enrage Toggle button
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (bossState.isEnraged) DemonCrimson.copy(alpha = 0.4f)
                                else Color(0x26FFFFFF)
                            )
                            .border(
                                1.dp,
                                if (bossState.isEnraged) DemonCrimsonGlow else Color(0x40FFFFFF),
                                RoundedCornerShape(4.dp)
                            )
                            .clickable { onToggleEnrage?.invoke() }
                            .padding(vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (bossState.isEnraged) "DISARM ENRAGE" else "TRIGGER ENRAGE",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
