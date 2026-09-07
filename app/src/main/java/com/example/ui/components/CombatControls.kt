package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BreathingStyle
import com.example.ui.theme.*

@Composable
fun CombatControls(
    comboCount: Int,
    isAttacking: Boolean,
    breathingStyle: BreathingStyle,
    breathingGauge: Float,
    isBreathingFull: Boolean,
    skill1Cd: Float,
    skill2Cd: Float,
    skill3Cd: Float,
    onAttack: () -> Unit,
    onHeavyAttack: () -> Unit,
    onDodge: () -> Unit,
    onBlockStart: () -> Unit,
    onBlockEnd: () -> Unit,
    onSkill1: () -> Unit,
    onSkill2: () -> Unit,
    onSkill3: () -> Unit,
    onUltimate: () -> Unit,
    onParry: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Ultimate pulsing scale animation
    val infiniteTransition = rememberInfiniteTransition(label = "ultPulse")
    val ultScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isBreathingFull) 1.15f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ultScale"
    )

    Box(
        modifier = modifier
            .testTag("combat_controls_panel")
            .size(240.dp, 200.dp)
    ) {
        // Skill 1 (Top Left)
        SkillActionButton(
            label = "SKILL 1",
            icon = "🌊",
            cooldown = skill1Cd,
            color = DemonAetherCyan,
            onClick = onSkill1,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 10.dp)
        )

        // Skill 2 (Top Center)
        SkillActionButton(
            label = "SKILL 2",
            icon = "🔥",
            cooldown = skill2Cd,
            color = DemonCrimson,
            onClick = onSkill2,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 0.dp)
        )

        // Skill 3 (Top Right)
        SkillActionButton(
            label = "SKILL 3",
            icon = "⚡",
            cooldown = skill3Cd,
            color = DemonThunderYellow,
            onClick = onSkill3,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 12.dp, top = 10.dp)
        )

        // Ultimate Button (Left side of attack cluster)
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 0.dp)
                .scale(ultScale)
                .size(54.dp)
                .clip(CircleShape)
                .background(
                    if (isBreathingFull) {
                        Brush.radialGradient(listOf(DemonGold, DemonCrimson, DemonNavyBlack))
                    } else {
                        Brush.radialGradient(listOf(DemonNavyCard, DemonNavyBlack))
                    }
                )
                .border(
                    2.dp,
                    if (isBreathingFull) DemonGold else DemonAetherCyan.copy(alpha = 0.4f),
                    CircleShape
                )
                .clickable { onUltimate() }
                .testTag("ultimate_button"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "☀️",
                    fontSize = 18.sp
                )
                Text(
                    text = if (isBreathingFull) "BURST" else "${(breathingGauge * 100).toInt()}%",
                    color = if (isBreathingFull) Color.White else DemonMistGray,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        // Heavy Attack (Upper Right of main attack)
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp, bottom = 20.dp)
                .size(46.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(DemonCrimsonDark, DemonNavyDark)))
                .border(1.5.dp, DemonCrimsonGlow, CircleShape)
                .clickable { onHeavyAttack() }
                .testTag("heavy_attack_button"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("💥", fontSize = 14.sp)
                Text("HEAVY", color = DemonGold, fontSize = 7.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Dedicated Perfect Parry / Deflect Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 54.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(Color(0xFF332000), Color(0xDD0D1117))))
                .border(1.5.dp, DemonGold, CircleShape)
                .clickable { onParry() }
                .testTag("parry_button"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("⚡", fontSize = 13.sp)
                Text("PARRY", color = DemonGold, fontSize = 7.sp, fontWeight = FontWeight.Black)
            }
        }

        // Block / Guard Button (Lower Left)
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 32.dp, bottom = 4.dp)
                .size(44.dp)
                .clip(CircleShape)
                .background(DemonNavyGlass)
                .border(1.dp, DemonAetherCyan, CircleShape)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            onBlockStart()
                            tryAwaitRelease()
                            onBlockEnd()
                        }
                    )
                }
                .testTag("block_button"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("🛡️", fontSize = 14.sp)
                Text("GUARD", color = DemonAetherCyan, fontSize = 7.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Dodge Button (Bottom Center)
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 0.dp, end = 8.dp)
                .size(46.dp)
                .clip(CircleShape)
                .background(DemonNavyGlass)
                .border(1.dp, DemonWindGreen, CircleShape)
                .clickable { onDodge() }
                .testTag("dodge_button"),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("💨", fontSize = 14.sp)
                Text("DODGE", color = DemonWindGreen, fontSize = 7.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Primary Attack Button (Large focal point with bold ATK text and red aura)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 4.dp, bottom = 4.dp)
                .size(76.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(
                            if (isAttacking) Color(0xFFEF4444) else Color(0xFFDC2626),
                            Color(0xFF991B1B),
                            Color(0xFF450A0A)
                        )
                    )
                )
                .border(3.dp, Color(0x4DFFFFFF), CircleShape)
                .clickable { onAttack() }
                .testTag("primary_attack_button"),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color(0x26FFFFFF))
                    .border(1.dp, Color(0x4DFFFFFF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (comboCount > 0) "x$comboCount" else "ATK",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }
    }
}

@Composable
fun SkillActionButton(
    label: String,
    icon: String,
    cooldown: Float,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isReady = cooldown <= 0f

    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(if (isReady) DemonNavyGlass else Color(0x99181B30))
            .border(1.dp, if (isReady) color else DemonMistGray.copy(alpha = 0.4f), CircleShape)
            .clickable(enabled = isReady) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isReady) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(icon, fontSize = 15.sp)
                Text(label, color = color, fontSize = 6.sp, fontWeight = FontWeight.Bold)
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color(0xBB000000)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = String.format("%.1f", cooldown),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}
