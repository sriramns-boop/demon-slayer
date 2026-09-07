package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.state.GameViewModel
import com.example.ui.components.AtmosphericParticleOverlay
import com.example.ui.components.OrnateGlassPanel
import com.example.ui.theme.*

@Composable
fun DefeatScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag("defeat_screen")
            .fillMaxSize()
            .background(DemonNavyBlack)
    ) {
        Image(
            painter = painterResource(id = R.drawable.anime_game_bg),
            contentDescription = "Defeat Bg",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Heavy dark red despair overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xF52A0005), Color(0xEE07080E))
                    )
                )
        )

        AtmosphericParticleOverlay(modifier = Modifier.fillMaxSize(), particleColor = DemonCrimson)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("敗 北", color = DemonCrimson, fontSize = 28.sp, letterSpacing = 8.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "DEFEATED",
                color = DemonCrimsonGlow,
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 6.sp
            )

            Text(
                text = "THE NIGHT HAS CONSUMED YOUR SPIRIT",
                color = DemonMistGray,
                fontSize = 12.sp,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            OrnateGlassPanel(
                modifier = Modifier
                    .widthIn(max = 340.dp)
                    .fillMaxWidth(),
                borderColor = DemonCrimson
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .testTag("retry_button")
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(DemonCrimsonDark)
                            .border(1.dp, DemonGold, RoundedCornerShape(8.dp))
                            .clickable { viewModel.restartMission() }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("RETRY MISSION ⚔️", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(DemonNavyCard)
                            .border(1.dp, DemonAetherCyan.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .clickable { viewModel.restartMission() }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("RESTART FROM CHECKPOINT 🔄", color = DemonTextLight, fontSize = 11.sp)
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x33000000))
                            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(8.dp))
                            .clickable { viewModel.navigateTo(GameScreen.MAIN_MENU) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("RETURN TO MAIN MENU ⛩️", color = DemonMistGray, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
