package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
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
import com.example.ui.components.JapaneseBrushHeader
import com.example.ui.components.OrnateGlassPanel
import com.example.ui.theme.*

@Composable
fun SkillTreeScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag("skill_tree_screen")
            .fillMaxSize()
            .background(DemonNavyBlack)
    ) {
        Image(
            painter = painterResource(id = R.drawable.anime_game_bg),
            contentDescription = "Bg",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(Color(0xF007080E)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .testTag("back_button")
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DemonNavyCard)
                            .border(1.dp, DemonAetherCyan, CircleShape)
                            .clickable { viewModel.navigateTo(GameScreen.MAIN_MENU) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))
                    JapaneseBrushHeader(title = "SKILL TREE", subtitle = "Celestial Breathing Wheel")
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(DemonNavyCard)
                        .border(1.dp, DemonGold, RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🪙", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${viewModel.coins}",
                        color = DemonGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Style Selector Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BreathingStyle.values().forEach { style ->
                    val isSelected = viewModel.currentBreathingStyle == style
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) style.primaryColor.copy(alpha = 0.35f) else DemonNavySurface)
                            .border(1.dp, if (isSelected) style.primaryColor else Color(0x33FFFFFF), RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.currentBreathingStyle = style
                                val firstInStyle = viewModel.skills.firstOrNull { it.style == style }
                                if (firstInStyle != null) viewModel.selectedSkillNode = firstInStyle
                            }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(style.iconSymbol, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = style.title.uppercase(),
                                color = if (isSelected) Color.White else DemonMistGray,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Area: Left Wheel Mandala & Right Node Inspector
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Left: Circular Mandala Skill Nodes
                OrnateGlassPanel(
                    modifier = Modifier
                        .weight(0.62f)
                        .fillMaxHeight(),
                    borderColor = viewModel.currentBreathingStyle.primaryColor
                ) {
                    val currentSkills = viewModel.skills.filter { it.style == viewModel.currentBreathingStyle }

                    // Decorative Mandala Canvas
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val radius = size.minDimension * 0.40f

                        // Mandala background circles
                        drawCircle(color = viewModel.currentBreathingStyle.primaryColor.copy(alpha = 0.15f), radius = radius * 0.4f, center = center, style = Stroke(1.5f))
                        drawCircle(color = viewModel.currentBreathingStyle.primaryColor.copy(alpha = 0.25f), radius = radius * 0.8f, center = center, style = Stroke(1.5f))

                        // Connecting Lines
                        currentSkills.forEachIndexed { index, _ ->
                            val angle = (index.toDouble() / currentSkills.size.toDouble()) * 2 * Math.PI - Math.PI / 2
                            val nx = center.x + (radius * 0.65f * kotlin.math.cos(angle)).toFloat()
                            val ny = center.y + (radius * 0.65f * kotlin.math.sin(angle)).toFloat()
                            drawLine(
                                color = viewModel.currentBreathingStyle.accentColor.copy(alpha = 0.4f),
                                start = center,
                                end = Offset(nx, ny),
                                strokeWidth = 2f
                            )
                        }
                    }

                    // Interactive Skill Nodes in a Column / Grid
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        currentSkills.forEach { skill ->
                            val isSelected = skill.id == viewModel.selectedSkillNode.id
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) viewModel.currentBreathingStyle.primaryColor.copy(alpha = 0.3f) else DemonNavyCard)
                                    .border(
                                        1.5.dp,
                                        if (isSelected) DemonGold else if (skill.isUnlocked) viewModel.currentBreathingStyle.primaryColor else Color(0x33FFFFFF),
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { viewModel.selectedSkillNode = skill }
                                    .padding(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = if (skill.isUnlocked) "✨" else "🔒",
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = skill.name,
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = skill.formLabel,
                                                color = viewModel.currentBreathingStyle.primaryColor,
                                                fontSize = 9.sp
                                            )
                                        }
                                    }

                                    Text(
                                        text = if (skill.isUnlocked) "Lv.${skill.level}/${skill.maxLevel}" else "LOCKED",
                                        color = if (skill.isUnlocked) DemonGold else DemonMistGray,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Right: Selected Skill Inspector
                val selected = viewModel.selectedSkillNode
                OrnateGlassPanel(
                    modifier = Modifier
                        .weight(0.38f)
                        .fillMaxHeight(),
                    borderColor = DemonGold
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = selected.name,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "${selected.style.title} • ${selected.formLabel}",
                                color = selected.style.primaryColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = selected.description,
                                color = DemonTextLight,
                                fontSize = 10.sp,
                                lineHeight = 13.sp
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Stat values
                            Text("💥 Base Damage: +${selected.damage}", color = DemonCrimsonGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("⏱️ Cooldown: ${selected.cooldownSeconds}s", color = DemonAetherCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("⚡ Energy Cost: ${selected.energyCost}", color = DemonThunderYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        // Upgrade Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(DemonCrimsonDark)
                                .border(1.2.dp, DemonGold, RoundedCornerShape(8.dp))
                                .clickable { viewModel.unlockOrUpgradeSkill(selected) }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (selected.isUnlocked) "UPGRADE (${selected.upgradeCost}🪙)" else "UNLOCK (${selected.upgradeCost}🪙)",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
