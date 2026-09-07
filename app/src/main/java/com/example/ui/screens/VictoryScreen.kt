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
fun VictoryScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag("victory_screen")
            .fillMaxSize()
            .background(DemonNavyBlack)
    ) {
        Image(
            painter = painterResource(id = R.drawable.anime_game_bg),
            contentDescription = "Victory Bg",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xDD07080E), Color(0xAA07080E), Color(0xF207080E))
                    )
                )
        )

        AtmosphericParticleOverlay(modifier = Modifier.fillMaxSize(), particleColor = DemonGold)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Victory Crest & Title
            Text("❖ 鬼 討 伐 ❖", color = DemonGold, fontSize = 16.sp, letterSpacing = 6.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "VICTORY",
                color = DemonGold,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 6.sp
            )

            Text(
                text = "CRIMSON DEMON HAS BEEN SLAIN",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Loot and XP Rewards Box
            OrnateGlassPanel(
                modifier = Modifier
                    .widthIn(max = 380.dp)
                    .fillMaxWidth(),
                borderColor = DemonGold
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("MISSION REWARDS", color = DemonGold, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("⭐ Experience Points", color = Color.White, fontSize = 12.sp)
                        Text("+2,500 XP", color = DemonGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("🪙 Hunter Coins", color = Color.White, fontSize = 12.sp)
                        Text("+1,200 🪙", color = DemonGold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("LOOT ACQUIRED", color = DemonAetherCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                    LootItemRow(name = "Nightfall Katana", rarity = "Epic Blade", icon = "🗡️")
                    LootItemRow(name = "Demon Fang ×4", rarity = "Crafting Material", icon = "🦷")
                    LootItemRow(name = "Herbal Vitality Elixir ×2", rarity = "Recovery Item", icon = "🧪")
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Continue Button
            Box(
                modifier = Modifier
                    .testTag("victory_continue_button")
                    .clip(RoundedCornerShape(24.dp))
                    .background(DemonCrimsonDark)
                    .border(2.dp, DemonGold, RoundedCornerShape(24.dp))
                    .clickable { viewModel.navigateTo(GameScreen.MAIN_MENU) }
                    .padding(horizontal = 36.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "SHEATHE BLADE & CONTINUE ❯",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

@Composable
fun LootItemRow(name: String, rarity: String, icon: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(DemonNavyCard)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(name, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Text(rarity, color = DemonGold, fontSize = 9.sp)
    }
}
