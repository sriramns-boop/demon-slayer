package com.example.ui.screens

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.GameRepository
import com.example.state.GameScreen
import com.example.state.GameViewModel
import com.example.ui.components.OrnateGlassPanel
import com.example.ui.theme.*

@Composable
fun DialogueScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val dialogueLines = GameRepository.sampleDialogue
    val line = dialogueLines[viewModel.dialogueIndex % dialogueLines.size]

    Box(
        modifier = modifier
            .testTag("dialogue_screen")
            .fillMaxSize()
            .background(DemonNavyBlack)
    ) {
        // Fullscreen Cinematic Story Scene
        Image(
            painter = painterResource(id = R.drawable.anime_game_bg),
            contentDescription = "Scene",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Top Cinema Bars
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(40.dp)
                .background(Color.Black)
        )

        // Top Dialogue Controls: Auto, Skip, Speed
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 16.dp, top = 48.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DialoguePillButton(text = "AUTO", onClick = {})
            DialoguePillButton(
                text = "SKIP",
                onClick = { viewModel.navigateTo(GameScreen.GAMEPLAY_HUD) }
            )
        }

        // Speaker 3D/Anime Character Portrait (Hero or Boss)
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 24.dp, bottom = 170.dp)
                .size(130.dp, 180.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.demon_hunter_hero),
                contentDescription = "Speaker",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .border(2.dp, DemonAetherCyan, RoundedCornerShape(12.dp))
            )
        }

        // Bottom Dialogue Box
        OrnateGlassPanel(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(180.dp)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            borderColor = DemonGold
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Speaker Name Tag
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = line.speaker,
                            color = DemonCrimsonGlow,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 2.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "• ${line.title}",
                            color = DemonGold,
                            fontSize = 10.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Spoken Text
                    Text(
                        text = "\"${line.text}\"",
                        color = Color.White,
                        fontSize = 13.sp,
                        lineHeight = 17.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Interactive Dialogue Choices / Advance Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Narrative Choice Buttons
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        line.choices.forEach { choice ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(DemonNavyCard)
                                    .border(1.dp, DemonAetherCyan, RoundedCornerShape(6.dp))
                                    .clickable {
                                        viewModel.dialogueIndex = (viewModel.dialogueIndex + 1) % dialogueLines.size
                                    }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = choice,
                                    color = DemonTextLight,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Next button
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(DemonCrimsonDark)
                            .border(1.dp, DemonGold, CircleShape)
                            .clickable {
                                if (viewModel.dialogueIndex < dialogueLines.size - 1) {
                                    viewModel.dialogueIndex++
                                } else {
                                    viewModel.navigateTo(GameScreen.GAMEPLAY_HUD)
                                }
                            }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "CONTINUE ▶",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DialoguePillButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(DemonNavyGlass)
            .border(1.dp, DemonAetherCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = text, color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}
