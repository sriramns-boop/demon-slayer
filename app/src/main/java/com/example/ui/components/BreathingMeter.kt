package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BreathingStyle
import com.example.ui.theme.*

@Composable
fun BreathingMeter(
    currentStyle: BreathingStyle,
    onSelectStyle: (BreathingStyle) -> Unit,
    gauge: Float, // 0..1f
    isFull: Boolean,
    onTriggerConcentration: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gaugePulse")
    val glowColor by infiniteTransition.animateColor(
        initialValue = currentStyle.primaryColor,
        targetValue = DemonGold,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Column(
        modifier = modifier
            .testTag("breathing_system_meter")
            .widthIn(max = 280.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Elemental Style Quick Carousel
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BreathingStyle.values().take(4).forEach { style ->
                val isSelected = style == currentStyle
                Box(
                    modifier = Modifier
                        .padding(horizontal = 2.dp)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) style.primaryColor.copy(alpha = 0.35f)
                            else Color(0x33000000)
                        )
                        .border(
                            1.dp,
                            if (isSelected) style.primaryColor else Color(0x26FFFFFF),
                            CircleShape
                        )
                        .clickable { onSelectStyle(style) }
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = style.element.uppercase(),
                        color = if (isSelected) Color.White else DemonMistGray,
                        fontSize = 7.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Atmospheric Breathing Gauge Row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "BREATHING",
                color = DemonAetherCyan,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp
            )

            // Neon Capsule Bar
            Box(
                modifier = Modifier
                    .width(110.dp)
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF070B14))
                    .border(1.dp, DemonAetherCyan.copy(alpha = 0.4f), CircleShape)
                    .padding(1.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(gauge.coerceIn(0.05f, 1f))
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF0891B2), // cyan-600
                                    Color(0xFF22D3EE), // cyan-400
                                    if (isFull) Color(0xFF67E8F9) else Color(0xFF06B6D4)
                                )
                            )
                        )
                )
            }

            Text(
                text = "${(gauge * 100).toInt()}%",
                color = Color.White,
                fontSize = 8.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Ultimate Ready Banner or Unleash button
        if (isFull) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0x3322D3EE))
                    .border(1.dp, DemonAetherCyan, CircleShape)
                    .clickable { onTriggerConcentration() }
                    .padding(horizontal = 10.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "ULTIMATE READY",
                    color = DemonAetherCyan,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
            }
        } else {
            Text(
                text = currentStyle.title.uppercase(),
                color = DemonMistGray,
                fontSize = 7.sp,
                letterSpacing = 1.sp
            )
        }
    }
}
