package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.BreathingStyle
import com.example.state.GameScreen
import com.example.state.GameViewModel
import com.example.ui.components.*
import com.example.ui.effects.PerfectParryEffectOverlay
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GameplayHudScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag("gameplay_hud_screen")
            .fillMaxSize()
            .background(DemonNavyBlack)
    ) {
        // 1. In-game 3D world backdrop (Japanese temple and mountain path at night)
        Image(
            painter = painterResource(id = R.drawable.anime_game_bg),
            contentDescription = "Arena Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark combat vignette
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        listOf(Color(0x3307080E), Color(0xBB07080E))
                    )
                )
        )

        // 2. Playable Arena Surface Simulation (Floor Grid, Enemies, and Player avatar)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val arenaRadius = size.minDimension * 0.45f

            // Arena boundary circle
            drawCircle(
                color = DemonAetherCyan.copy(alpha = 0.2f),
                radius = arenaRadius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 2f)
            )

            // Render Arena Enemies
            viewModel.enemies.forEach { enemy ->
                if (enemy.hp > 0) {
                    val ex = centerX + enemy.x * arenaRadius
                    val ey = centerY + enemy.y * arenaRadius

                    // Shadow
                    drawOval(
                        color = Color(0x66000000),
                        topLeft = Offset(ex - 18f, ey + 10f),
                        size = androidx.compose.ui.geometry.Size(36f, 16f)
                    )

                    // Enemy body / demonic glow
                    val enemyColor = if (enemy.isHit) Color.White else DemonCrimson
                    drawCircle(
                        color = enemyColor,
                        radius = 16f,
                        center = Offset(ex, ey)
                    )

                    // Enemy health bar
                    val hpPct = (enemy.hp / enemy.maxHp).coerceIn(0f, 1f)
                    drawRect(
                        color = Color(0xFF330000),
                        topLeft = Offset(ex - 22f, ey - 26f),
                        size = androidx.compose.ui.geometry.Size(44f, 5f)
                    )
                    drawRect(
                        color = DemonCrimsonGlow,
                        topLeft = Offset(ex - 22f, ey - 26f),
                        size = androidx.compose.ui.geometry.Size(44f * hpPct, 5f)
                    )
                }
            }

            // Render Player Avatar
            val px = centerX + viewModel.playerPosX * arenaRadius
            val py = centerY + viewModel.playerPosY * arenaRadius

            // Player shadow
            drawOval(
                color = Color(0x88000000),
                topLeft = Offset(px - 22f, py + 14f),
                size = androidx.compose.ui.geometry.Size(44f, 18f)
            )

            // Aether Aura when breathing or dodging
            if (viewModel.isDodging || viewModel.isBreathingFull) {
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(DemonAetherCyan.copy(alpha = 0.6f), Color.Transparent),
                        center = Offset(px, py),
                        radius = 42f
                    ),
                    radius = 42f,
                    center = Offset(px, py)
                )
            }

            // Player circle marker (Kai with glowing nichirin direction indicator)
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(Color.White, DemonAetherCyan, DemonAetherBlue),
                    center = Offset(px, py),
                    radius = 20f
                ),
                radius = 20f,
                center = Offset(px, py)
            )

            // Nichirin Katana blade pointing in movement angle
            val angleRad = Math.toRadians(viewModel.playerDirectionAngle.toDouble())
            val bladeEndX = px + cos(angleRad).toFloat() * 32f
            val bladeEndY = py + sin(angleRad).toFloat() * 32f

            drawLine(
                color = if (viewModel.isAttacking) DemonCrimsonGlow else DemonGold,
                start = Offset(px, py),
                end = Offset(bladeEndX, bladeEndY),
                strokeWidth = 5f
            )
        }

        // 3. Animated sword slash canvas overlay
        AnimatedSwordSlashCanvas(
            trigger = viewModel.slashEffectTrigger,
            strokeColor = viewModel.currentBreathingStyle.primaryColor
        )

        // 3b. Screen-Flash & Stylized Impact Particle Effect on Perfect Parry
        PerfectParryEffectOverlay(
            trigger = viewModel.perfectParryTrigger,
            impactXRatio = viewModel.perfectParryImpactX,
            impactYRatio = viewModel.perfectParryImpactY
        )

        // 4. Floating Damage Text Floaters
        viewModel.floatingTexts.forEach { ft ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = ((0.5f + ft.x * 0.4f) * 320).coerceIn(40f, 280f).dp,
                        top = ((0.5f + ft.y * 0.35f) * 450).coerceIn(80f, 400f).dp
                    )
            ) {
                Text(
                    text = ft.text,
                    color = if (ft.isHeal) DemonWindGreen else if (ft.isCrit) DemonGold else Color.White,
                    fontSize = if (ft.isCrit) 16.sp else 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }

        // 5. Active Skill announcement banner
        AnimatedVisibility(
            visible = viewModel.activeSkillEffect != null,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xDD000000))
                    .border(2.dp, viewModel.currentBreathingStyle.primaryColor, RoundedCornerShape(12.dp))
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = viewModel.activeSkillEffect ?: "",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
            }
        }

        // ==================== HUD OVERLAYS ====================

        // TOP LEFT: Character Status Panel
        CharacterStatusPanel(
            name = viewModel.playerName,
            level = viewModel.playerLevel,
            currentHp = viewModel.currentHp,
            maxHp = viewModel.maxHp,
            currentStamina = viewModel.currentStamina,
            maxStamina = viewModel.maxStamina,
            xpProgress = viewModel.xpProgress,
            onPauseClick = { viewModel.togglePause() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 12.dp, top = 26.dp)
        )

        // TOP CENTER: Quest Tracker
        QuestTrackerPanel(
            quest = viewModel.trackedQuest,
            isExpanded = viewModel.isQuestTrackerExpanded,
            onToggleExpand = { viewModel.isQuestTrackerExpanded = !viewModel.isQuestTrackerExpanded },
            onOpenQuestLog = { viewModel.navigateTo(GameScreen.QUEST_LOG) },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 26.dp)
        )

        // TOP RIGHT: Mini Map Radar & Quick Navigation
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 12.dp, top = 26.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            MiniMapRadar(
                playerX = viewModel.playerPosX,
                playerY = viewModel.playerPosY,
                enemies = viewModel.enemies,
                onExpandClick = { viewModel.navigateTo(GameScreen.WORLD_MAP) }
            )

            // Switch to Boss HUD Quick Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(DemonCrimsonDark)
                    .border(1.dp, DemonGold, RoundedCornerShape(12.dp))
                    .clickable { viewModel.navigateTo(GameScreen.BOSS_HUD) }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("👹 BOSS MODE", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }
        }

        // BOTTOM LEFT: Virtual Joystick (direction control, sprint, auto-run)
        VirtualJoystick(
            scale = viewModel.joystickScale,
            opacity = viewModel.joystickOpacity,
            isSprinting = viewModel.isSprinting,
            onSprintToggle = { viewModel.isSprinting = it },
            autoRun = viewModel.autoRun,
            onAutoRunToggle = { viewModel.autoRun = it },
            onMove = { dx, dy -> viewModel.onJoystickMove(dx, dy) },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, bottom = 16.dp)
        )

        // BOTTOM CENTER: Breathing Meter
        BreathingMeter(
            currentStyle = viewModel.currentBreathingStyle,
            onSelectStyle = { viewModel.currentBreathingStyle = it },
            gauge = viewModel.breathingGauge,
            isFull = viewModel.isBreathingFull,
            onTriggerConcentration = { viewModel.triggerUltimate() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        )

        // BOTTOM RIGHT: Combat Controls (Attack combos, Heavy, Dodge, Guard, Skills 1-3, Ultimate)
        CombatControls(
            comboCount = viewModel.comboCount,
            isAttacking = viewModel.isAttacking,
            breathingStyle = viewModel.currentBreathingStyle,
            breathingGauge = viewModel.breathingGauge,
            isBreathingFull = viewModel.isBreathingFull,
            skill1Cd = viewModel.skill1Cooldown,
            skill2Cd = viewModel.skill2Cooldown,
            skill3Cd = viewModel.skill3Cooldown,
            onAttack = { viewModel.performAttack() },
            onHeavyAttack = { viewModel.performHeavyAttack() },
            onDodge = { viewModel.performDodge() },
            onBlockStart = { viewModel.performBlock(true) },
            onBlockEnd = { viewModel.performBlock(false) },
            onSkill1 = { viewModel.performSkill(1) },
            onSkill2 = { viewModel.performSkill(2) },
            onSkill3 = { viewModel.performSkill(3) },
            onUltimate = { viewModel.triggerUltimate() },
            onParry = { viewModel.performParry() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 8.dp, bottom = 8.dp)
        )
    }
}
