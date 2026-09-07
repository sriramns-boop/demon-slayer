package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.state.GameScreen
import com.example.ui.components.AnimeMenuButton
import com.example.ui.components.AtmosphericImmersiveFooter
import com.example.ui.components.AtmosphericParticleOverlay
import com.example.ui.components.OrnateGlassPanel
import com.example.ui.theme.*

@Composable
fun MainMenuScreen(
    onNavigate: (GameScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag("main_menu_screen")
            .fillMaxSize()
            .background(DemonNavyBlack)
    ) {
        // Hero art backdrop (Demon hunter hero Kai with glowing katana)
        Image(
            painter = painterResource(id = R.drawable.demon_hunter_hero),
            contentDescription = "Hero Standby",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark gradient vignette to ensure high legibility of UI menu
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xF507080E),
                            Color(0xCC07080E),
                            Color(0x8807080E)
                        )
                    )
                )
        )

        AtmosphericParticleOverlay(modifier = Modifier.fillMaxSize())

        // Top Header
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 20.dp, top = 28.dp, end = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("❖", color = DemonCrimson, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "DEMON SLAYER",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                }
                Text(
                    text = "BLADE OF THE NIGHT",
                    color = DemonGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            }

            // Quick Showcase Switcher Button
            Box(
                modifier = Modifier
                    .testTag("showcase_sheet_button")
                    .clip(RoundedCornerShape(20.dp))
                    .background(DemonCrimsonDark)
                    .border(1.2.dp, DemonGold, RoundedCornerShape(20.dp))
                    .clickable { onNavigate(GameScreen.UI_SHOWCASE) }
                    .padding(horizontal = 14.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "✦ UI SHOWCASE ✦",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }
        }

        // Left Navigation Menu Panel
        OrnateGlassPanel(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp, top = 75.dp, bottom = 20.dp)
                .widthIn(max = 280.dp)
                .fillMaxHeight(0.88f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Featured Action: Enter Battle HUD
                AnimeMenuButton(
                    text = "BATTLE HUD (PLAY)",
                    subText = "Live 3D Combat Simulator",
                    icon = "⚔️",
                    isPrimary = true,
                    testTag = "btn_continue",
                    onClick = { onNavigate(GameScreen.GAMEPLAY_HUD) }
                )

                // Featured Action: Boss Battle
                AnimeMenuButton(
                    text = "BOSS: CRIMSON DEMON",
                    subText = "Multi-Phase Epic Encounter",
                    icon = "👹",
                    isPrimary = true,
                    testTag = "btn_boss",
                    onClick = { onNavigate(GameScreen.BOSS_HUD) }
                )

                AnimeMenuButton(
                    text = "INVENTORY",
                    subText = "Weapons, Armor & Relics",
                    icon = "🎒",
                    testTag = "btn_inventory",
                    onClick = { onNavigate(GameScreen.INVENTORY) }
                )

                AnimeMenuButton(
                    text = "SKILL TREE",
                    subText = "8 Breathing Disciplines",
                    icon = "🌀",
                    testTag = "btn_skills",
                    onClick = { onNavigate(GameScreen.SKILL_TREE) }
                )

                AnimeMenuButton(
                    text = "CHARACTER",
                    subText = "Kai Lv. 25 Status & Gear",
                    icon = "👤",
                    testTag = "btn_character",
                    onClick = { onNavigate(GameScreen.CHARACTER) }
                )

                AnimeMenuButton(
                    text = "WORLD MAP",
                    subText = "10 Regions of Elyndra",
                    icon = "🗺️",
                    testTag = "btn_map",
                    onClick = { onNavigate(GameScreen.WORLD_MAP) }
                )

                AnimeMenuButton(
                    text = "QUEST LOG",
                    subText = "90 Campaign Missions",
                    icon = "📜",
                    testTag = "btn_quests",
                    onClick = { onNavigate(GameScreen.QUEST_LOG) }
                )

                AnimeMenuButton(
                    text = "DIALOGUE",
                    subText = "Cinematic Story Sequence",
                    icon = "💬",
                    testTag = "btn_dialogue",
                    onClick = { onNavigate(GameScreen.DIALOGUE) }
                )

                AnimeMenuButton(
                    text = "SETTINGS",
                    subText = "Audio, Quality & Controls",
                    icon = "⚙️",
                    testTag = "btn_settings",
                    onClick = { onNavigate(GameScreen.PAUSE_MENU) }
                )
            }
        }

        // Bottom Atmospheric Footer
        AtmosphericImmersiveFooter(
            onMenuClick = { onNavigate(GameScreen.PAUSE_MENU) },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
