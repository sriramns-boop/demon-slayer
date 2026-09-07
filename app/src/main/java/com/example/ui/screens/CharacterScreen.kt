package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.EquipmentSlot
import com.example.state.GameScreen
import com.example.state.GameViewModel
import com.example.ui.components.JapaneseBrushHeader
import com.example.ui.components.OrnateGlassPanel
import com.example.ui.theme.*

@Composable
fun CharacterScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag("character_screen")
            .fillMaxSize()
            .background(DemonNavyBlack)
    ) {
        Image(
            painter = painterResource(id = R.drawable.anime_game_bg),
            contentDescription = "Bg",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(Color(0xEE07080E)))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
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

                Spacer(modifier = Modifier.width(14.dp))
                JapaneseBrushHeader(title = "CHARACTER PROFILE", subtitle = "Shadow Hunter Status")
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main 2-Column Layout: Left Hero Display | Right Detailed Attribute Inspector
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Left: Hero Avatar Preview & Rank
                OrnateGlassPanel(
                    modifier = Modifier
                        .weight(0.48f)
                        .fillMaxHeight(),
                    borderColor = DemonAetherCyan
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.demon_hunter_hero),
                            contentDescription = "Hero Model",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Bottom Hero Label
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color(0xDD07080E))
                                    )
                                )
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "KAI • LV. 25",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = "RANK: SHADOW HUNTER II",
                                color = DemonGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Current Breath: ${viewModel.currentBreathingStyle.title}",
                                color = viewModel.currentBreathingStyle.primaryColor,
                                fontSize = 9.sp
                            )
                        }
                    }
                }

                // Right: Attributes & Equipment Slots
                OrnateGlassPanel(
                    modifier = Modifier
                        .weight(0.52f)
                        .fillMaxHeight(),
                    borderColor = DemonGold
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "BATTLE ATTRIBUTES",
                                color = DemonGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            )

                            // Attributes Table
                            AttributeRow(label = "Health Points (HP)", value = "${viewModel.currentHp.toInt()} / ${viewModel.maxHp.toInt()}", color = DemonCrimsonGlow)
                            AttributeRow(label = "Stamina", value = "${viewModel.currentStamina.toInt()} / ${viewModel.maxStamina.toInt()}", color = DemonAetherCyan)
                            AttributeRow(label = "Physical Attack", value = "450 (+85)", color = DemonCrimson)
                            AttributeRow(label = "Physical Defense", value = "380 (+45)", color = DemonAetherBlue)
                            AttributeRow(label = "Movement Speed", value = "145", color = DemonWindGreen)
                            AttributeRow(label = "Critical Strike Rate", value = "28.5%", color = DemonGold)
                            AttributeRow(label = "Breathing Power", value = "820", color = viewModel.currentBreathingStyle.primaryColor)
                        }

                        // Equipped Gear Summary
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "ACTIVE GEAR",
                                color = DemonGold,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )

                            EquipmentSlot.values().forEach { slot ->
                                val item = viewModel.equipment.firstOrNull { it.slot == slot && it.isEquipped }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(slot.label, color = DemonMistGray, fontSize = 8.sp)
                                    Text(
                                        text = item?.name ?: "None",
                                        color = item?.rarity?.color ?: Color.Gray,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Open Inventory Shortcut
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(DemonNavyCard)
                                .border(1.dp, DemonAetherCyan, RoundedCornerShape(8.dp))
                                .clickable { viewModel.navigateTo(GameScreen.INVENTORY) }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("MANAGE INVENTORY & GEAR", color = DemonAetherCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AttributeRow(
    label: String,
    value: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0x33101324))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = DemonTextLight, fontSize = 10.sp)
        Text(text = value, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}
