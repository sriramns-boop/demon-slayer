package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Shield
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
 * Phase styling data tailored specifically for dark fantasy anime boss encounters.
 */
data class DarkFantasyPhaseStyle(
    val phaseNumber: Int,
    val romanNumeral: String,
    val phaseName: String,
    val titleSubtitle: String,
    val sigilIcon: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val glowColor: Color,
    val barGradient: List<Color>,
    val talismanBorder: Color,
    val auraBg: Color
)

val DarkFantasyPhases = listOf(
    DarkFantasyPhaseStyle(
        phaseNumber = 1,
        romanNumeral = "I",
        phaseName = "BLOOD MONARCH",
        titleSubtitle = "Harbinger of the Crimson Eclipse",
        sigilIcon = "🩸",
        primaryColor = Color(0xFFDC2626),
        secondaryColor = Color(0xFF7F1D1D),
        glowColor = Color(0xFFFF1E46),
        barGradient = listOf(Color(0xFF7F1D1D), Color(0xFFDC2626), Color(0xFFFF1E46)),
        talismanBorder = Color(0xFFEF4444),
        auraBg = Color(0x33DC2626)
    ),
    DarkFantasyPhaseStyle(
        phaseNumber = 2,
        romanNumeral = "II",
        phaseName = "DEMON TITAN",
        titleSubtitle = "Volcanic Calamity & Earth Breaker",
        sigilIcon = "🔥",
        primaryColor = Color(0xFFEA580C),
        secondaryColor = Color(0xFF7C2D12),
        glowColor = Color(0xFFF59E0B),
        barGradient = listOf(Color(0xFF7C2D12), Color(0xFFEA580C), Color(0xFFFBBF24)),
        talismanBorder = Color(0xFFF59E0B),
        auraBg = Color(0x33EA580C)
    ),
    DarkFantasyPhaseStyle(
        phaseNumber = 3,
        romanNumeral = "III",
        phaseName = "IMMORTAL SHADOW",
        titleSubtitle = "Void Sovereign of Eternal Night",
        sigilIcon = "🌑",
        primaryColor = Color(0xFF8B5CF6),
        secondaryColor = Color(0xFF312E81),
        glowColor = Color(0xFF00F0FF),
        barGradient = listOf(Color(0xFF312E81), Color(0xFF7C3AED), Color(0xFF00F0FF)),
        talismanBorder = Color(0xFF00F0FF),
        auraBg = Color(0x337C3AED)
    ),
    DarkFantasyPhaseStyle(
        phaseNumber = 4,
        romanNumeral = "IV",
        phaseName = "FINAL DAWN",
        titleSubtitle = "Apocalyptic True Form - Dawnbringer Awakening",
        sigilIcon = "☀️",
        primaryColor = Color(0xFFF59E0B),
        secondaryColor = Color(0xFF78350F),
        glowColor = Color(0xFFFEF08A),
        barGradient = listOf(Color(0xFF78350F), Color(0xFFF59E0B), Color(0xFFFEF08A), Color(0xFFFFFFFF)),
        talismanBorder = Color(0xFFFEF08A),
        auraBg = Color(0x4DF59E0B)
    )
)

fun getDarkFantasyPhaseStyle(phase: Int): DarkFantasyPhaseStyle {
    return DarkFantasyPhases.getOrElse(phase - 1) { DarkFantasyPhases[0] }
}

/**
 * BossHUD composable that displays a prominent, stylized name label and a segmented
 * health bar at the top of the screen, including a visual indicator for different boss
 * phases with an immersive dark fantasy aesthetic.
 */
@Composable
fun BossHUD(
    bossState: BossState = BossState(),
    phaseAnnouncement: String? = null,
    onSelectPhase: ((Int) -> Unit)? = null,
    onAdvancePhase: (() -> Unit)? = null,
    onToggleEnrage: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    BossHUD(
        bossName = bossState.name,
        bossTitle = bossState.title,
        currentPhase = bossState.currentPhase,
        maxPhases = bossState.maxPhases,
        hpPercentage = bossState.hpPercentage,
        staggerPercentage = bossState.staggerPercentage,
        isEnraged = bossState.isEnraged,
        phaseAnnouncement = phaseAnnouncement,
        onSelectPhase = onSelectPhase,
        onAdvancePhase = onAdvancePhase,
        onToggleEnrage = onToggleEnrage,
        modifier = modifier
    )
}

/**
 * Explicit-parameter variant of BossHUD for modular UI flexibility.
 */
@Composable
fun BossHUD(
    bossName: String,
    bossTitle: String,
    currentPhase: Int,
    maxPhases: Int = 4,
    hpPercentage: Float = 1.0f,
    staggerPercentage: Float = 0.5f,
    isEnraged: Boolean = false,
    phaseAnnouncement: String? = null,
    onSelectPhase: ((Int) -> Unit)? = null,
    onAdvancePhase: (() -> Unit)? = null,
    onToggleEnrage: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val activePhaseStyle = remember(currentPhase) {
        getDarkFantasyPhaseStyle(currentPhase)
    }

    // Dynamic animated HP state with trailing ghost damage bar
    val animatedHp by animateFloatAsState(
        targetValue = hpPercentage.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
        label = "bossHpAnim"
    )
    val ghostHp by animateFloatAsState(
        targetValue = hpPercentage.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 800, delayMillis = 150, easing = LinearOutSlowInEasing),
        label = "ghostBossHpAnim"
    )

    // Dark fantasy aura pulses
    val infiniteTransition = rememberInfiniteTransition(label = "bossHudTransitions")
    val auraGlowPulse by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auraGlowPulse"
    )
    val enrageVibration by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "enrageVibration"
    )

    var showPhaseOps by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .testTag("boss_hud")
            .fillMaxWidth()
            .widthIn(max = 420.dp)
            .padding(horizontal = 14.dp),
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
                    .clip(CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xCC000000),
                                activePhaseStyle.secondaryColor.copy(alpha = 0.95f),
                                Color(0xCC000000)
                            )
                        )
                    )
                    .border(
                        width = 1.5.dp,
                        color = activePhaseStyle.glowColor.copy(alpha = auraGlowPulse),
                        shape = CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "⚔", color = activePhaseStyle.glowColor, fontSize = 12.sp)
                    Text(
                        text = "⚠️ ${phaseAnnouncement?.uppercase()} ⚠️",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                    Text(text = "⚔", color = activePhaseStyle.glowColor, fontSize = 12.sp)
                }
            }
        }

        // ==================== THREAT HEADER & ENRAGE BADGE ====================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dark Fantasy Threat Level Sigil
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(activePhaseStyle.glowColor)
                )
                Text(
                    text = "✦ APEX DREADBORN ✦ CALAMITY CLASS ✦",
                    color = activePhaseStyle.primaryColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    fontStyle = FontStyle.Italic
                )
            }

            // Enraged Indicator Badge
            if (isEnraged) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(NeonCrimson.copy(alpha = 0.35f * enrageVibration))
                        .border(1.dp, NeonCrimson, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Enraged",
                            tint = NeonGold,
                            modifier = Modifier.size(11.dp)
                        )
                        Text(
                            text = "ENRAGED",
                            color = NeonCrimson,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // ==================== PROMINENT STYLIZED BOSS NAME LABEL ====================
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "⚔",
                color = activePhaseStyle.glowColor.copy(alpha = 0.8f),
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 6.dp)
            )

            Text(
                text = bossName.uppercase(),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .testTag("boss_hud_name")
                    .shadow(elevation = 10.dp, spotColor = activePhaseStyle.glowColor)
            )

            Text(
                text = "⚔",
                color = activePhaseStyle.glowColor.copy(alpha = 0.8f),
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 6.dp)
            )
        }

        // Phase Title Subtitle (Dark Fantasy Lore Descriptor)
        Text(
            text = "⟦ PHASE ${activePhaseStyle.romanNumeral} : ${activePhaseStyle.phaseName} — ${activePhaseStyle.titleSubtitle.uppercase()} ⟧",
            color = activePhaseStyle.glowColor,
            fontSize = 8.5.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        // ==================== VISUAL PHASE INDICATORS (DARK FANTASY TALISMANS) ====================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DarkFantasyPhases.take(maxPhases).forEach { phaseStyle ->
                val isCurrent = phaseStyle.phaseNumber == currentPhase
                val isCompleted = phaseStyle.phaseNumber < currentPhase

                Box(
                    modifier = Modifier
                        .testTag("boss_hud_phase_indicator_${phaseStyle.phaseNumber}")
                        .weight(1f)
                        .clip(CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp))
                        .background(
                            when {
                                isCurrent -> phaseStyle.auraBg
                                isCompleted -> Color(0x33141824)
                                else -> Color(0x1A050508)
                            }
                        )
                        .border(
                            width = if (isCurrent) 1.5.dp else 1.dp,
                            color = when {
                                isCurrent -> phaseStyle.talismanBorder.copy(alpha = auraGlowPulse)
                                isCompleted -> Color(0x4D94A3B8)
                                else -> Color(0x20FFFFFF)
                            },
                            shape = CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp)
                        )
                        .clickable { onSelectPhase?.invoke(phaseStyle.phaseNumber) }
                        .padding(vertical = 4.dp, horizontal = 2.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (isCurrent) {
                            Text(
                                text = phaseStyle.sigilIcon,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(end = 3.dp)
                            )
                        }

                        Text(
                            text = when {
                                isCompleted -> "✓ ${phaseStyle.romanNumeral}"
                                isCurrent -> "PHASE ${phaseStyle.romanNumeral}"
                                else -> "PHASE ${phaseStyle.romanNumeral}"
                            },
                            color = when {
                                isCurrent -> Color.White
                                isCompleted -> Color(0x99FFFFFF)
                                else -> Color(0x40FFFFFF)
                            },
                            fontSize = if (isCurrent) 9.sp else 8.sp,
                            fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ==================== SEGMENTED HEALTH BAR ====================
        // Visually segmented health bar with phase partition segments
        Box(
            modifier = Modifier
                .testTag("boss_hud_segmented_health_bar")
                .fillMaxWidth()
                .height(13.dp)
                .clip(CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp))
                .background(Color(0xCC050508))
                .border(
                    width = 1.2.dp,
                    color = activePhaseStyle.talismanBorder.copy(alpha = 0.75f),
                    shape = CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp)
                )
        ) {
            // Trailing ghost damage bar
            Box(
                modifier = Modifier
                    .fillMaxWidth(ghostHp)
                    .fillMaxHeight()
                    .clip(CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp))
                    .background(Color(0x80FFFFFF))
            )

            // Active phase-gradient health bar
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedHp)
                    .fillMaxHeight()
                    .clip(CutCornerShape(topStart = 4.dp, bottomEnd = 4.dp))
                    .background(Brush.horizontalGradient(activePhaseStyle.barGradient))
            )

            // Segment Dividers creating true segmented bar appearance
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(maxPhases - 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(2.dp)
                            .background(Color(0x99000000))
                    )
                }
            }

            // Health numerical readout with dark fantasy contrast
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${(hpPercentage * 10000).toInt()} / 10,000 HP  •  ${(hpPercentage * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // ==================== STAGGER / POSTURE METER & CONTROLS ====================
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "Stagger Shield",
                tint = NeonGold,
                modifier = Modifier.size(10.dp)
            )

            Text(
                text = "STAGGER",
                color = NeonGold,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )

            Box(
                modifier = Modifier
                    .testTag("boss_hud_stagger_meter")
                    .weight(1f)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color(0x80000000))
                    .border(0.5.dp, Color(0x33FFFFFF), CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(staggerPercentage.coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(DemonGoldDark, NeonGold, NeonSunfire)
                            )
                        )
                )
            }

            Text(
                text = "${(staggerPercentage * 100).toInt()}%",
                color = NeonGold,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )

            // Quick Phase Ops Toggle
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(3.dp))
                    .background(Color(0x26FFFFFF))
                    .border(0.5.dp, Color(0x40FFFFFF), RoundedCornerShape(3.dp))
                    .clickable { showPhaseOps = !showPhaseOps }
                    .padding(horizontal = 5.dp, vertical = 1.dp)
            ) {
                Text(
                    text = if (showPhaseOps) "HIDE OPS" else "PHASE OPS",
                    color = Color.White,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // ==================== COLLAPSIBLE DARK FANTASY PHASE CONTROL OPERATIONS ====================
        AnimatedVisibility(
            visible = showPhaseOps,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth()
                    .clip(CutCornerShape(topStart = 6.dp, bottomEnd = 6.dp))
                    .background(Color(0xF0080910))
                    .border(
                        width = 1.dp,
                        color = activePhaseStyle.primaryColor.copy(alpha = 0.5f),
                        shape = CutCornerShape(topStart = 6.dp, bottomEnd = 6.dp)
                    )
                    .padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⚔ DARK FANTASY COMBAT PHASE SIMULATOR ⚔",
                    color = Color(0xCCFFFFFF),
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Next Phase trigger
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(activePhaseStyle.primaryColor.copy(alpha = 0.35f))
                            .border(1.dp, activePhaseStyle.glowColor, RoundedCornerShape(4.dp))
                            .clickable { onAdvancePhase?.invoke() }
                            .padding(vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FastForward,
                                contentDescription = "Advance Phase",
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

                    // Enrage Toggle
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (isEnraged) NeonCrimson.copy(alpha = 0.4f)
                                else Color(0x26FFFFFF)
                            )
                            .border(
                                1.dp,
                                if (isEnraged) NeonCrimson else Color(0x40FFFFFF),
                                RoundedCornerShape(4.dp)
                            )
                            .clickable { onToggleEnrage?.invoke() }
                            .padding(vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isEnraged) "DISARM ENRAGE" else "TRIGGER ENRAGE",
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
