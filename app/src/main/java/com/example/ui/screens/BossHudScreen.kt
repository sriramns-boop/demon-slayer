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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ai.*
import com.example.state.GameScreen
import com.example.state.GameViewModel
import com.example.ui.components.*
import com.example.ui.effects.PerfectParryEffectOverlay
import com.example.ui.theme.*

@Composable
fun BossHudScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag("boss_hud_screen")
            .fillMaxSize()
            .background(DemonNavyBlack)
    ) {
        // Boss Battle Arena Background (Eerie blood moon night sky and burning shrine)
        Image(
            painter = painterResource(id = R.drawable.anime_game_bg),
            contentDescription = "Boss Arena",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Boss Phase Visual Theme
        val phaseTheme = getPhaseVisualTheme(viewModel.bossState.currentPhase)

        // Demonic / Elemental atmosphere overlay mapped to active boss phase
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        listOf(phaseTheme.primaryColor.copy(alpha = 0.35f), Color(0xDD07080E))
                    )
                )
        )

        AtmosphericParticleOverlay(modifier = Modifier.fillMaxSize(), particleColor = phaseTheme.accentGlow)

        // 3D Perspective Arena Visual Simulation (Horizontal Frame 4K Realistic Depth)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2f
            val centerY = size.height * 0.54f
            val horizonY = centerY - 55f

            // 1. 3D PERSPECTIVE ARENA GROUND (Foreshortened elliptical rings in Z-space)
            for (i in 1..5) {
                val ringZ = i * 44f
                val ringRadiusX = ringZ * 3.2f
                val ringRadiusY = ringZ * 0.65f // Foreshortened in perspective
                drawOval(
                    brush = Brush.radialGradient(
                        listOf(
                            Color.Transparent,
                            phaseTheme.primaryColor.copy(alpha = (0.30f - i * 0.045f).coerceAtLeast(0.04f)),
                            Color.Transparent
                        ),
                        center = Offset(centerX, horizonY + ringZ),
                        radius = ringRadiusX
                    ),
                    topLeft = Offset(centerX - ringRadiusX, horizonY + ringZ - ringRadiusY),
                    size = androidx.compose.ui.geometry.Size(ringRadiusX * 2f, ringRadiusY * 2f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.4f)
                )
            }

            // 2. 3D RADIAL PERSPECTIVE GRID LINES (Vanishing point towards horizon)
            for (angleIdx in -5..5) {
                val spread = angleIdx * 110f
                drawLine(
                    brush = Brush.verticalGradient(
                        listOf(Color.Transparent, phaseTheme.primaryColor.copy(alpha = 0.22f), Color.Transparent),
                        startY = horizonY,
                        endY = size.height
                    ),
                    start = Offset(centerX + angleIdx * 12f, horizonY),
                    end = Offset(centerX + spread * 2.4f, size.height),
                    strokeWidth = 1.2f
                )
            }

            // 3. REALISTIC AMBIENT OCCLUSION & VOLUMETRIC FLOOR LIGHTING
            drawOval(
                brush = Brush.radialGradient(
                    listOf(
                        phaseTheme.primaryColor.copy(alpha = 0.40f),
                        Color(0x99000000),
                        Color.Transparent
                    ),
                    center = Offset(centerX, centerY + 20f),
                    radius = 260f
                ),
                topLeft = Offset(centerX - 240f, centerY - 40f),
                size = androidx.compose.ui.geometry.Size(480f, 160f)
            )

            // 4. 3D BOSS ENTITY (Directional Shadow, Volumetric Core, Specular 4K Rim Sheen)
            val bossShadowWidth = 200f
            val bossShadowHeight = 44f
            drawOval(
                brush = Brush.radialGradient(
                    listOf(Color(0xCC000000), Color.Transparent),
                    center = Offset(centerX, centerY + 28f),
                    radius = bossShadowWidth / 2f
                ),
                topLeft = Offset(centerX - bossShadowWidth / 2f, centerY + 12f),
                size = androidx.compose.ui.geometry.Size(bossShadowWidth, bossShadowHeight)
            )

            // Boss Outer Plasma Aura & Elemental Flames
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(phaseTheme.primaryColor.copy(alpha = 0.70f), phaseTheme.accentGlow.copy(alpha = 0.25f), Color.Transparent),
                    center = Offset(centerX, centerY - 25f),
                    radius = 110f
                ),
                radius = 110f,
                center = Offset(centerX, centerY - 25f)
            )

            // Boss 3D Metallic Volumetric Core
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(
                        Color.White,
                        phaseTheme.secondaryColor,
                        Color(0xFF1E102A),
                        Color(0xFF07080E)
                    ),
                    center = Offset(centerX - 12f, centerY - 38f),
                    radius = 56f
                ),
                radius = 56f,
                center = Offset(centerX, centerY - 25f)
            )

            // Boss 3D Specular Rim Light (HDR 4K Metallic Highlight)
            drawCircle(
                brush = Brush.sweepGradient(
                    listOf(
                        Color.Transparent,
                        Color.White.copy(alpha = 0.8f),
                        phaseTheme.accentGlow,
                        Color.Transparent
                    ),
                    center = Offset(centerX, centerY - 25f)
                ),
                radius = 56f,
                center = Offset(centerX, centerY - 25f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5f)
            )

            // Glowing Demonic Horns & Flaming Eyes
            drawCircle(color = phaseTheme.accentGlow, radius = 7f, center = Offset(centerX - 18f, centerY - 32f))
            drawCircle(color = Color.White, radius = 3f, center = Offset(centerX - 18f, centerY - 32f))
            drawCircle(color = phaseTheme.accentGlow, radius = 7f, center = Offset(centerX + 18f, centerY - 32f))
            drawCircle(color = Color.White, radius = 3f, center = Offset(centerX + 18f, centerY - 32f))

            // 5. 3D PLAYER AVATAR IN PERSPECTIVE (Nichirin Blade Direction, Shadow & Aether Aura)
            val px = centerX + viewModel.playerPosX * 180f
            val py = centerY + 110f + viewModel.playerPosY * 50f

            // Player 3D Shadow
            drawOval(
                color = Color(0x99000000),
                topLeft = Offset(px - 26f, py + 10f),
                size = androidx.compose.ui.geometry.Size(52f, 20f)
            )

            // Player Nichirin Energy Aura
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(DemonAetherCyan.copy(alpha = 0.5f), Color.Transparent),
                    center = Offset(px, py),
                    radius = 32f
                ),
                radius = 32f,
                center = Offset(px, py)
            )

            // Player Core Entity
            drawCircle(
                brush = Brush.radialGradient(
                    listOf(Color.White, DemonAetherCyan, DemonNavyCard),
                    center = Offset(px - 4f, py - 6f),
                    radius = 20f
                ),
                radius = 20f,
                center = Offset(px, py)
            )

            // Directional Nichirin Katana Sheen
            drawLine(
                brush = Brush.linearGradient(
                    listOf(Color.White, DemonGold, Color.Transparent)
                ),
                start = Offset(px, py),
                end = Offset(px + 22f, py - 24f),
                strokeWidth = 3f,
                cap = androidx.compose.ui.graphics.StrokeCap.Round
            )
        }

        // Animated sword slashes
        AnimatedSwordSlashCanvas(
            trigger = viewModel.slashEffectTrigger,
            strokeColor = viewModel.currentBreathingStyle.primaryColor
        )

        // Screen-Flash & Stylized Impact Particle Effect on Perfect Parry
        PerfectParryEffectOverlay(
            trigger = viewModel.perfectParryTrigger,
            impactXRatio = viewModel.perfectParryImpactX,
            impactYRatio = viewModel.perfectParryImpactY
        )

        // Damage numbers
        viewModel.floatingTexts.forEach { ft ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = ((0.5f + ft.x * 0.4f) * 320).coerceIn(50f, 270f).dp,
                        top = ((0.45f + ft.y * 0.3f) * 450).coerceIn(80f, 380f).dp
                    )
            ) {
                Text(
                    text = ft.text,
                    color = if (ft.isCrit) DemonGold else DemonCrimsonGlow,
                    fontSize = if (ft.isCrit) 18.sp else 13.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Active Skill Announcement
        AnimatedVisibility(
            visible = viewModel.activeSkillEffect != null,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xEE000000))
                    .border(2.dp, DemonGold, RoundedCornerShape(12.dp))
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

        // TOP: Boss HUD with stylized name label, segmented health bar & phase indicators + BossAI FSM Overlay
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BossHUD(
                bossState = viewModel.bossState,
                phaseAnnouncement = viewModel.bossPhaseAnnounce,
                onSelectPhase = { phase -> viewModel.transitionBossToPhase(phase) },
                onAdvancePhase = { viewModel.advanceBossPhase() },
                onToggleEnrage = { viewModel.toggleBossEnrage() },
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(4.dp))

            BossAIControlOverlay(
                bossAI = viewModel.bossAI,
                onDifficultyChanged = { diff -> viewModel.setBossDifficulty(diff) },
                onManualAttack = { viewModel.forceBossAttack() },
                onManualStagger = { viewModel.forceBossStagger() },
                onManualPhase = { viewModel.advanceBossPhase() },
                onTriggerParry = { viewModel.performParry() },
                modifier = Modifier.padding(horizontal = 14.dp)
            )
        }

        // TOP LEFT: Player compact status
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
                .padding(start = 12.dp, top = 88.dp)
        )

        // TOP RIGHT: Return to normal arena toggle
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 88.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DemonNavyGlass)
                .border(1.dp, DemonAetherCyan, RoundedCornerShape(12.dp))
                .clickable { viewModel.navigateTo(GameScreen.GAMEPLAY_HUD) }
                .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text("← ARENA HUD", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }

        // BOTTOM LEFT: Joystick
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

        // BOTTOM RIGHT: Combat Controls
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
