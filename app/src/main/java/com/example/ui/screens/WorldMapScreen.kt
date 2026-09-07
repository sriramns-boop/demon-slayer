package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import com.example.model.WorldRegion
import com.example.state.GameScreen
import com.example.state.GameViewModel
import com.example.ui.components.JapaneseBrushHeader
import com.example.ui.components.OrnateGlassPanel
import com.example.ui.theme.*

@Composable
fun WorldMapScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag("world_map_screen")
            .fillMaxSize()
            .background(DemonNavyBlack)
    ) {
        Image(
            painter = painterResource(id = R.drawable.anime_game_bg),
            contentDescription = "Map Canvas",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(Color(0xDC07080E)))

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
                    JapaneseBrushHeader(title = "WORLD MAP", subtitle = "Lands of Elyndra")
                }

                Text(
                    text = "DISCOVERED: 7 / 10",
                    color = DemonGold,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Map Canvas & Right Side Region Inspector
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Interactive World Map Surface
                OrnateGlassPanel(
                    modifier = Modifier
                        .weight(0.68f)
                        .fillMaxHeight(),
                    borderColor = DemonAetherCyan
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Drawing map routes & territory borders
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            // Draw travel path lines between adjacent regions
                            val pts = viewModel.worldRegions.map { Offset(it.normalizedX * w, it.normalizedY * h) }
                            for (i in 0 until pts.size - 1) {
                                drawLine(
                                    color = DemonAetherCyan.copy(alpha = 0.25f),
                                    start = pts[i],
                                    end = pts[i + 1],
                                    strokeWidth = 1.5f
                                )
                            }
                        }

                        // Interactive Region Node Markers
                        viewModel.worldRegions.forEach { region ->
                            val isSelected = region.id == viewModel.selectedRegion.id

                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(
                                        start = (region.normalizedX * 220).dp,
                                        top = (region.normalizedY * 260).dp
                                    )
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) DemonCrimsonDark else if (region.isUnlocked) DemonNavyCard else Color(0x66000000))
                                    .border(
                                        1.5.dp,
                                        if (isSelected) DemonGold else if (region.isUnlocked) DemonAetherCyan else Color(0x44FFFFFF),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { viewModel.selectedRegion = region }
                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (region.hasBoss) "👹" else if (region.hasQuest) "⭐" else "⛩️",
                                        fontSize = 11.sp
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = region.name,
                                        color = if (isSelected) Color.White else if (region.isUnlocked) DemonTextLight else DemonMistGray,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Right: Selected Region Inspector & Fast Travel Button
                val region = viewModel.selectedRegion
                OrnateGlassPanel(
                    modifier = Modifier
                        .weight(0.32f)
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
                                text = region.name,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = region.subtitle,
                                color = DemonGold,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text("⚠️ Danger Level: ${region.dangerLevel}", color = DemonCrimsonGlow, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            Text("STATUS: ${if (region.isUnlocked) "Discovered" else "Locked"}", color = if (region.isUnlocked) DemonWindGreen else Color.Gray, fontSize = 10.sp)

                            if (region.hasBoss) {
                                Text("👹 BOSS TERRITORY", color = DemonCrimson, fontSize = 9.sp, fontWeight = FontWeight.ExtraBold)
                            }
                            if (region.hasQuest) {
                                Text("📜 ACTIVE QUEST AVAILABLE", color = DemonGold, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Fast Travel Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (region.isUnlocked) DemonCrimsonDark else Color.DarkGray)
                                .border(1.dp, DemonGold, RoundedCornerShape(8.dp))
                                .clickable(enabled = region.isUnlocked) {
                                    viewModel.fastTravelTo(region)
                                }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (region.isUnlocked) "⚡ FAST TRAVEL" else "LOCKED",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
