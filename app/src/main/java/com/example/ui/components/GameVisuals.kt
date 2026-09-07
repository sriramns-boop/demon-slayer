package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class Particle(
    val x: Float,
    val y: Float,
    val speed: Float,
    val size: Float,
    val alpha: Float,
    val color: Color
)

@Composable
fun AtmosphericParticleOverlay(
    modifier: Modifier = Modifier,
    particleColor: Color = DemonCrimsonGlow
) {
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    val particles = remember {
        List(40) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                speed = Random.nextFloat() * 0.15f + 0.05f,
                size = Random.nextFloat() * 4f + 2f,
                alpha = Random.nextFloat() * 0.5f + 0.2f,
                color = if (it % 3 == 0) DemonAetherCyan else if (it % 2 == 0) DemonCrimsonGlow else DemonGold
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        particles.forEach { p ->
            val currY = ((p.y * h + (time * p.speed * 60f)) % h)
            val currX = (p.x * w + sin(time * 0.01f + p.x * 10f) * 20f) % w
            drawCircle(
                color = p.color.copy(alpha = p.alpha),
                radius = p.size,
                center = Offset(currX, currY)
            )
        }
    }
}

@Composable
fun BloodMoonCanvas(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "moonGlow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension * 0.42f

        // Outer Aura
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    DemonCrimson.copy(alpha = 0.5f * glowAlpha),
                    DemonCrimsonDark.copy(alpha = 0.2f * glowAlpha),
                    Color.Transparent
                ),
                center = center,
                radius = radius * 1.8f
            ),
            radius = radius * 1.8f,
            center = center
        )

        // Moon disc
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFF758F),
                    DemonCrimson,
                    Color(0xFF590D22)
                ),
                center = center.copy(x = center.x - radius * 0.2f, y = center.y - radius * 0.2f),
                radius = radius
            ),
            radius = radius,
            center = center
        )

        // Subtle crater shadows
        drawCircle(
            color = Color(0x33000000),
            radius = radius * 0.25f,
            center = Offset(center.x - radius * 0.3f, center.y - radius * 0.2f)
        )
        drawCircle(
            color = Color(0x22000000),
            radius = radius * 0.35f,
            center = Offset(center.x + radius * 0.2f, center.y + radius * 0.3f)
        )
    }
}

@Composable
fun AnimatedSwordSlashCanvas(
    modifier: Modifier = Modifier,
    trigger: Int,
    strokeColor: Color = DemonAetherCyan
) {
    if (trigger <= 0) return

    val slashProgress = remember(trigger) { Animatable(0f) }

    androidx.compose.runtime.LaunchedEffect(trigger) {
        slashProgress.snapTo(0f)
        slashProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(240, easing = FastOutLinearInEasing)
        )
    }

    if (slashProgress.value < 1f) {
        Canvas(modifier = modifier.fillMaxSize()) {
            val p = slashProgress.value
            val start = Offset(size.width * (0.2f + p * 0.2f), size.height * (0.7f - p * 0.1f))
            val end = Offset(size.width * (0.5f + p * 0.4f), size.height * (0.3f - p * 0.2f))
            val control = Offset(size.width * 0.6f, size.height * 0.6f)

            val path = Path().apply {
                moveTo(start.x, start.y)
                quadraticTo(control.x, control.y, end.x, end.y)
            }

            drawPath(
                path = path,
                brush = Brush.linearGradient(
                    listOf(
                        Color.Transparent,
                        strokeColor.copy(alpha = 0.9f * (1f - p * 0.5f)),
                        Color.White,
                        strokeColor.copy(alpha = 0.6f),
                        Color.Transparent
                    )
                ),
                style = Stroke(width = 12f * (1f - p * 0.7f))
            )
        }
    }
}

@Composable
fun AtmosphericImmersiveBackdrop(
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. Base dark canvas #050508
        drawRect(color = AtmosphericBg)

        // 2. Top-Right Crimson Aura Orb (blur ~ 100px)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    AtmosphericCrimsonOrb.copy(alpha = 0.45f),
                    AtmosphericCrimsonOrb.copy(alpha = 0.25f),
                    Color.Transparent
                ),
                center = Offset(w * 0.95f, -h * 0.05f),
                radius = w * 0.65f
            ),
            radius = w * 0.65f,
            center = Offset(w * 0.95f, -h * 0.05f)
        )

        // 3. Bottom-Left Deep Midnight Indigo Aura Orb (blur ~ 120px)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    AtmosphericIndigoOrb.copy(alpha = 0.55f),
                    AtmosphericIndigoOrb.copy(alpha = 0.25f),
                    Color.Transparent
                ),
                center = Offset(-w * 0.05f, h * 1.05f),
                radius = w * 0.75f
            ),
            radius = w * 0.75f,
            center = Offset(-w * 0.05f, h * 1.05f)
        )

        // 4. Center Radial Vignette
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.Transparent,
                    AtmosphericBg.copy(alpha = 0.35f),
                    AtmosphericBg.copy(alpha = 0.85f)
                ),
                center = Offset(w / 2f, h / 2f),
                radius = kotlin.math.max(w, h) * 0.75f
            )
        )
    }
}

@Composable
fun AtmosphericImmersiveFooter(
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0x99000000))
            .border(1.dp, AtmosphericBorderWhite10)
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.foundation.layout.Column {
            androidx.compose.material3.Text(
                text = "SHADOWBLADE V2.4.0",
                color = Color(0x80FFFFFF),
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            androidx.compose.material3.Text(
                text = "ELYNDRA REALMS",
                color = Color(0x50FFFFFF),
                fontSize = 7.sp,
                letterSpacing = 0.5.sp
            )
        }

        androidx.compose.foundation.layout.Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)
        ) {
            androidx.compose.foundation.layout.Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(AtmosphericEmerald)
                )
                androidx.compose.material3.Text(
                    text = "CONNECTED",
                    color = AtmosphericEmerald,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                    .background(Color(0x1AFFFFFF))
                    .border(1.dp, AtmosphericBorderWhite10, androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
                    .clickable { onMenuClick() }
                    .padding(horizontal = 8.dp, vertical = 2.5.dp)
            ) {
                androidx.compose.material3.Text(
                    text = "MENU",
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
