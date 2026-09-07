package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.*

@Composable
fun CharacterStatusPanel(
    name: String = "KAI",
    level: Int = 25,
    currentHp: Float,
    maxHp: Float,
    currentStamina: Float,
    maxStamina: Float,
    xpProgress: Float,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedHp by animateFloatAsState(targetValue = (currentHp / maxHp).coerceIn(0f, 1f), label = "hp")
    val animatedStamina by animateFloatAsState(targetValue = (currentStamina / maxStamina).coerceIn(0f, 1f), label = "stamina")

    Row(
        modifier = modifier
            .testTag("character_status_panel")
            .clip(RoundedCornerShape(28.dp))
            .background(AtmosphericGlassBlack)
            .border(1.dp, AtmosphericBorderWhite10, RoundedCornerShape(28.dp))
            .padding(start = 6.dp, top = 5.dp, end = 10.dp, bottom = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Character Avatar with Neon Cyan ring and LV Badge
        Box(
            modifier = Modifier.size(46.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.demon_hunter_hero),
                contentDescription = "Hero Portrait",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(2.dp, DemonAetherCyan, CircleShape)
            )

            // Level badge at bottom-right
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(DemonAetherCyan)
                    .border(1.dp, Color.Black, CircleShape)
                    .padding(horizontal = 4.dp, vertical = 1.dp)
            ) {
                Text(
                    text = "LV.$level",
                    color = Color.Black,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Name and Atmospheric Bars Column
        Column(
            modifier = Modifier.widthIn(min = 110.dp, max = 135.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    color = DemonAetherCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "${currentHp.toInt()} HP",
                    color = AtmosphericEmeraldLight,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Health Bar (Emerald gradient from-emerald-500 to-emerald-400)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF080F16))
                    .border(0.5.dp, Color(0x1AFFFFFF), CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedHp)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF10B981), Color(0xFF34D399))
                            )
                        )
                )
            }

            // Stamina Bar (Cyan to Blue gradient from-cyan-500 to-blue-500)
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF080F16))
                    .border(0.5.dp, Color(0x1AFFFFFF), CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedStamina)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF06B6D4), Color(0xFF3B82F6))
                            )
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Pause Button
        Box(
            modifier = Modifier
                .testTag("pause_button")
                .size(28.dp)
                .clip(CircleShape)
                .background(Color(0x33FFFFFF))
                .border(0.8.dp, AtmosphericBorderWhite20, CircleShape)
                .clickable { onPauseClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Pause,
                contentDescription = "Pause",
                tint = Color.White,
                modifier = Modifier.size(14.dp)
            )
        }
    }
}
