package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.state.GameScreen
import com.example.state.GameViewModel
import com.example.ui.components.AnimeMenuButton
import com.example.ui.components.JapaneseBrushHeader
import com.example.ui.components.OrnateGlassPanel
import com.example.ui.theme.*

@Composable
fun PauseMenuScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag("pause_menu_screen")
            .fillMaxSize()
            .background(Color(0xE607080E)),
        contentAlignment = Alignment.Center
    ) {
        OrnateGlassPanel(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxHeight(0.92f)
                .padding(16.dp),
            borderColor = DemonGold
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                JapaneseBrushHeader(title = "PAUSED", subtitle = "Game Suspended")

                Spacer(modifier = Modifier.height(6.dp))

                // Action Buttons
                AnimeMenuButton(
                    text = "RESUME BATTLE",
                    icon = "▶️",
                    isPrimary = true,
                    testTag = "btn_resume",
                    onClick = { viewModel.togglePause() }
                )

                AnimeMenuButton(
                    text = "QUEST TRACKER",
                    icon = "📜",
                    testTag = "btn_pause_quests",
                    onClick = { viewModel.navigateTo(GameScreen.QUEST_LOG) }
                )

                AnimeMenuButton(
                    text = "INVENTORY",
                    icon = "🎒",
                    testTag = "btn_pause_inventory",
                    onClick = { viewModel.navigateTo(GameScreen.INVENTORY) }
                )

                AnimeMenuButton(
                    text = "SKILL TREE",
                    icon = "🌀",
                    testTag = "btn_pause_skills",
                    onClick = { viewModel.navigateTo(GameScreen.SKILL_TREE) }
                )

                AnimeMenuButton(
                    text = "WORLD MAP",
                    icon = "🗺️",
                    testTag = "btn_pause_map",
                    onClick = { viewModel.navigateTo(GameScreen.WORLD_MAP) }
                )

                AnimeMenuButton(
                    text = "CHARACTER PROFILE",
                    icon = "👤",
                    testTag = "btn_pause_character",
                    onClick = { viewModel.navigateTo(GameScreen.CHARACTER) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // In-Game Settings Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(DemonNavyCard)
                        .border(1.dp, DemonAetherCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("GAMEPLAY SETTINGS", color = DemonGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                        // Sound Toggle
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Audio & BGM", color = Color.White, fontSize = 11.sp)
                            Switch(
                                checked = viewModel.soundEnabled,
                                onCheckedChange = { viewModel.soundEnabled = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = DemonGold,
                                    checkedTrackColor = DemonCrimsonDark
                                )
                            )
                        }

                        // Joystick Scale Slider
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Joystick Size", color = Color.White, fontSize = 10.sp)
                                Text("${(viewModel.joystickScale * 100).toInt()}%", color = DemonAetherCyan, fontSize = 10.sp)
                            }
                            Slider(
                                value = viewModel.joystickScale,
                                onValueChange = { viewModel.joystickScale = it },
                                valueRange = 0.7f..1.4f,
                                colors = SliderDefaults.colors(
                                    thumbColor = DemonAetherCyan,
                                    activeTrackColor = DemonAetherCyan
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Quit to Main Menu
                AnimeMenuButton(
                    text = "QUIT TO MAIN MENU",
                    icon = "🚪",
                    testTag = "btn_quit_menu",
                    onClick = { viewModel.navigateTo(GameScreen.MAIN_MENU) }
                )
            }
        }
    }
}
