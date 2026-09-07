package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.GameRepository
import com.example.model.QuestCategory
import com.example.state.GameScreen
import com.example.state.GameViewModel
import com.example.ui.components.JapaneseBrushHeader
import com.example.ui.components.OrnateGlassPanel
import com.example.ui.theme.*

@Composable
fun QuestLogScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf("ACTIVE") }

    Box(
        modifier = modifier
            .testTag("quest_log_screen")
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
                JapaneseBrushHeader(title = "QUEST ARCHIVES", subtitle = "90-Mission Demon Hunter Chronicles")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Navigation Tabs
            val tabs = listOf("ACTIVE", "MAIN", "SIDE", "BOUNTY", "90-MISSION CAMPAIGN")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) DemonCrimsonDark else DemonNavySurface)
                            .border(1.dp, if (isSelected) DemonGold else DemonAetherCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .clickable { selectedTab = tab }
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

            if (selectedTab == "90-MISSION CAMPAIGN") {
                // Full 90 Campaign Missions Explorer
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(GameRepository.campaignMissions) { mission ->
                        OrnateGlassPanel(
                            modifier = Modifier.fillMaxWidth(),
                            borderColor = if (mission.number == 90) DemonGold else DemonCrimsonGlow.copy(alpha = 0.4f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "MISSION ${mission.number}",
                                            color = if (mission.number == 90) DemonGold else DemonCrimsonGlow,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = mission.title,
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = mission.chapter,
                                        color = DemonMistGray,
                                        fontSize = 9.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = mission.objective,
                                    color = DemonTextLight,
                                    fontSize = 11.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "📍 ${mission.mapLocation} • Boss: ${mission.boss}",
                                        color = DemonAetherCyan,
                                        fontSize = 9.sp
                                    )
                                    Text(
                                        text = "+${mission.xp} XP • +${mission.coins}🪙 • ${mission.reward}",
                                        color = DemonGold,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Active / Categorized Quests
                val filteredQuests = when (selectedTab) {
                    "MAIN" -> viewModel.quests.filter { it.category == QuestCategory.MAIN }
                    "SIDE" -> viewModel.quests.filter { it.category == QuestCategory.SIDE }
                    "BOUNTY" -> viewModel.quests.filter { it.category == QuestCategory.BOUNTY }
                    else -> viewModel.quests.filter { !it.isCompleted }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredQuests) { quest ->
                        val isTracked = viewModel.trackedQuest.id == quest.id
                        OrnateGlassPanel(
                            modifier = Modifier.fillMaxWidth(),
                            borderColor = if (isTracked) DemonGold else DemonCrimsonGlow.copy(alpha = 0.4f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "📜", fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = quest.title,
                                                color = Color.White,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = quest.chapter,
                                                color = DemonGold,
                                                fontSize = 9.sp
                                            )
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (isTracked) DemonGoldDark else DemonNavyCard)
                                            .border(1.dp, DemonGold, RoundedCornerShape(6.dp))
                                            .clickable {
                                                viewModel.trackedQuest = quest
                                                viewModel.addFloatingText("TRACKING: ${quest.title}", 0f, 0f, isHeal = true)
                                            }
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = if (isTracked) "TRACKING" else "TRACK QUEST",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = quest.objective,
                                    color = DemonTextLight,
                                    fontSize = 11.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("📍 ${quest.location} (${quest.distanceMeters}m)", color = DemonAetherCyan, fontSize = 10.sp)
                                    Text("🎁 +${quest.xpReward} XP • +${quest.coinsReward}🪙 • ${quest.rewardItem}", color = DemonGold, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
