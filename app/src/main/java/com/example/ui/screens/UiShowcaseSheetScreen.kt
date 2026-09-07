package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.example.state.GameViewModel
import com.example.ui.components.JapaneseBrushHeader
import com.example.ui.components.OrnateGlassPanel
import com.example.ui.theme.*

@Composable
fun UiShowcaseSheetScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    var showcaseTab by remember { mutableStateOf("BRAND & LOGO") }

    Box(
        modifier = modifier
            .testTag("ui_showcase_sheet_screen")
            .fillMaxSize()
            .background(DemonNavyBlack)
    ) {
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
                    JapaneseBrushHeader(
                        title = "UI DESIGN SYSTEM & SHOWCASE",
                        subtitle = "Demon Slayer: Blade of the Night — Master Presentation"
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(DemonCrimsonDark)
                        .border(1.dp, DemonGold, RoundedCornerShape(12.dp))
                        .clickable { viewModel.navigateTo(GameScreen.GAMEPLAY_HUD) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("PLAY GAMEPLAY HUD ⚔️", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Showcase Category Tabs
            val showcaseTabs = listOf("BRAND & LOGO", "DESIGN SYSTEM & COLOR", "ALL 15 GAME FRAMES")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                showcaseTabs.forEach { tab ->
                    val isSelected = showcaseTab == tab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) DemonCrimsonDark else DemonNavySurface)
                            .border(1.dp, if (isSelected) DemonGold else DemonAetherCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .clickable { showcaseTab = tab }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = tab,
                            color = if (isSelected) Color.White else DemonMistGray,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Scrollable Content Area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                when (showcaseTab) {
                    "BRAND & LOGO" -> {
                        // Section 1: APP ICON & GAME LOGO SPECIFICATIONS
                        OrnateGlassPanel(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                                Text("SECTION 1: APP ICON / GAME LOGO SUITE", color = DemonGold, fontSize = 13.sp, fontWeight = FontWeight.Black)
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // 1. Icon-Only App Icon Variant
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.demon_slayer_icon),
                                            contentDescription = "App Icon",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(100.dp)
                                                .clip(RoundedCornerShape(20.dp))
                                                .border(2.dp, DemonGold, RoundedCornerShape(20.dp))
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text("App Icon (1:1)", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text("Dark navy, red moon, dual aether blade", color = DemonMistGray, fontSize = 8.sp)
                                    }

                                    // 2. Full Horizontal Logo with Japanese Kanji
                                    Column(
                                        modifier = Modifier.weight(1.5f),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(DemonNavyCard)
                                                .border(1.dp, DemonAetherCyan, RoundedCornerShape(12.dp))
                                                .padding(12.dp)
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.demon_slayer_icon),
                                                    contentDescription = "Logo",
                                                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(8.dp))
                                                )
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Text("鬼 滅 の 刃", color = DemonCrimsonGlow, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                                    Text("DEMON SLAYER", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Black)
                                                    Text("BLADE OF THE NIGHT", color = DemonGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text("Horizontal Logo Lockup", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        Text("For banners, splash headers & store graphics", color = DemonMistGray, fontSize = 8.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Logo Variations: Dark, Light, Transparent
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    LogoVariantCard(
                                        title = "Dark Theme",
                                        bg = DemonNavyBlack,
                                        textColor = Color.White,
                                        modifier = Modifier.weight(1f)
                                    )
                                    LogoVariantCard(
                                        title = "Light Theme",
                                        bg = Color(0xFFEAEBED),
                                        textColor = Color.Black,
                                        modifier = Modifier.weight(1f)
                                    )
                                    LogoVariantCard(
                                        title = "Transparent",
                                        bg = Color(0x22FFFFFF),
                                        textColor = DemonGold,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    "DESIGN SYSTEM & COLOR" -> {
                        // Section 20-22: VISUAL DESIGN LANGUAGE
                        OrnateGlassPanel(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.fillMaxWidth().padding(14.dp)) {
                                Text("DESIGN LANGUAGE & COLOR PALETTE", color = DemonGold, fontSize = 13.sp, fontWeight = FontWeight.Black)
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    ColorSwatch(name = "Midnight Black", hex = "#07080E", color = DemonNavyBlack, modifier = Modifier.weight(1f))
                                    ColorSwatch(name = "Crimson Red", hex = "#E63946", color = DemonCrimson, modifier = Modifier.weight(1f))
                                    ColorSwatch(name = "Spirit Cyan", hex = "#00F0FF", color = DemonAetherCyan, modifier = Modifier.weight(1f))
                                    ColorSwatch(name = "Imperial Gold", hex = "#FFD166", color = DemonGold, modifier = Modifier.weight(1f))
                                    ColorSwatch(name = "Sakura Pink", hex = "#FF70A6", color = DemonBlossomPink, modifier = Modifier.weight(1f))
                                    ColorSwatch(name = "Thunder Yellow", hex = "#FFEE00", color = DemonThunderYellow, modifier = Modifier.weight(1f))
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text("DESIGN PHILOSOPHY", color = DemonAetherCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "• Dark supernatural anime atmosphere with high contrast luminous energy borders.\n" +
                                           "• Frosted glassmorphism (translucent panels) ensuring deep immersion in 3D combat while maintaining complete HUD legibility.\n" +
                                           "• Traditional Japanese ornamental mon crests and brush typography.\n" +
                                           "• Dynamic color-coded elemental breathing styles (Water, Fire, Thunder, Wind, Moon, Sun).",
                                    color = DemonTextLight,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }

                    "ALL 15 GAME FRAMES" -> {
                        // Interactive Navigation Cards to all frames
                        Text("INTERACTIVE FRAME ARCHIVES (TAP TO VIEW ANY FRAME)", color = DemonGold, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)

                        val frameList = listOf(
                            Triple("1. SPLASH SCREEN", "Red Moon, vertical katana, Tap to Start", GameScreen.SPLASH),
                            Triple("2. MAIN MENU", "Hero stance, Japanese mon borders, full navigation", GameScreen.MAIN_MENU),
                            Triple("3. GAMEPLAY HUD", "Playable combat arena, virtual joystick, combo attacks", GameScreen.GAMEPLAY_HUD),
                            Triple("4. BOSS HUD", "Crimson Demon multi-phase health bar & stagger", GameScreen.BOSS_HUD),
                            Triple("5. INVENTORY FRAME", "Gear slots, item rarity grid, stats inspector", GameScreen.INVENTORY),
                            Triple("6. SKILL TREE FRAME", "Circular celestial breathing wheel, 8 elemental styles", GameScreen.SKILL_TREE),
                            Triple("7. CHARACTER PROFILE", "Kai Lv. 25 stats, equipment slots & rank", GameScreen.CHARACTER),
                            Triple("8. WORLD MAP", "10 regions of Elyndra with fast travel teleportation", GameScreen.WORLD_MAP),
                            Triple("9. QUEST LOG", "Active quest tracker + 90-mission campaign archives", GameScreen.QUEST_LOG),
                            Triple("10. DIALOGUE FRAME", "Cinematic dialogue box with character portraits & choices", GameScreen.DIALOGUE),
                            Triple("11. PAUSE MENU", "Frosted glass overlay with game settings & shortcuts", GameScreen.PAUSE_MENU),
                            Triple("12. VICTORY FRAME", "Boss defeated celebration, XP/Gold, and rare loot", GameScreen.VICTORY),
                            Triple("13. DEFEAT FRAME", "Mission failed atmospheric screen with retry buttons", GameScreen.DEFEAT)
                        )

                        frameList.forEach { (title, subtitle, targetScreen) ->
                            OrnateGlassPanel(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.navigateTo(targetScreen) }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = title, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        Text(text = subtitle, color = DemonMistGray, fontSize = 9.sp)
                                    }
                                    Text(text = "LAUNCH ❯", color = DemonCrimsonGlow, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LogoVariantCard(
    title: String,
    bg: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(1.dp, DemonAetherCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("DEMON SLAYER", color = textColor, fontSize = 9.sp, fontWeight = FontWeight.Black)
            Text(title, color = DemonMistGray, fontSize = 7.sp)
        }
    }
}

@Composable
fun ColorSwatch(name: String, hex: String, color: Color, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(color)
                .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
        )
        Spacer(modifier = Modifier.height(3.dp))
        Text(name, color = Color.White, fontSize = 7.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        Text(hex, color = DemonMistGray, fontSize = 6.sp)
    }
}
