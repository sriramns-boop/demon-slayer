package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fullscreen
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.state.EnemyInstance
import com.example.ui.theme.*

@Composable
fun MiniMapRadar(
    playerX: Float,
    playerY: Float,
    enemies: List<EnemyInstance>,
    onExpandClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.testTag("minimap_radar"),
        contentAlignment = Alignment.Center
    ) {
        // Radar Circle
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(AtmosphericGlassBlack)
                .border(1.dp, AtmosphericBorderWhite20, CircleShape)
                .clickable { onExpandClick() },
            contentAlignment = Alignment.Center
        ) {
            // Radar Canvas
            Canvas(modifier = Modifier.fillMaxSize().padding(3.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.minDimension / 2f

                // Outer inner ring
                drawCircle(
                    color = DemonAetherCyan.copy(alpha = 0.2f),
                    radius = radius * 0.75f,
                    center = center,
                    style = Stroke(1f)
                )

                // Center ring
                drawCircle(
                    color = DemonAetherCyan.copy(alpha = 0.4f),
                    radius = radius * 0.35f,
                    center = center,
                    style = Stroke(1f)
                )

                // Top Red North indicator notch
                drawLine(
                    color = DemonCrimson,
                    start = Offset(center.x, 2f),
                    end = Offset(center.x, 8f),
                    strokeWidth = 3f
                )

                // Enemies (red blips)
                enemies.forEach { enemy ->
                    if (enemy.hp > 0) {
                        val ex = center.x + (enemy.x - playerX) * radius * 0.75f
                        val ey = center.y + (enemy.y - playerY) * radius * 0.75f
                        if ((ex - center.x) * (ex - center.x) + (ey - center.y) * (ey - center.y) < radius * radius) {
                            drawCircle(color = DemonCrimson, radius = 3f, center = Offset(ex, ey))
                        }
                    }
                }

                // Player position in center (Cyan Dot with glow)
                drawCircle(color = DemonAetherCyan.copy(alpha = 0.5f), radius = 5f, center = center)
                drawCircle(color = DemonAetherCyan, radius = 2.5f, center = center)
            }
        }

        // MAP label badge at bottom
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 6.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xE6050508))
                .border(0.5.dp, AtmosphericBorderWhite20, RoundedCornerShape(3.dp))
                .padding(horizontal = 4.dp, vertical = 0.5.dp)
        ) {
            Text(
                text = "MAP",
                color = Color(0xCCFFFFFF),
                fontSize = 7.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        }
    }
}
