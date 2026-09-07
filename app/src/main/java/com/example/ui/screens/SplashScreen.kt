package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AtmosphericParticleOverlay
import com.example.ui.components.BloodMoonCanvas
import com.example.ui.theme.*

@Composable
fun SplashScreen(
    onStartGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "splash")
    val promptAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "promptAlpha"
    )

    Box(
        modifier = modifier
            .testTag("splash_screen")
            .fillMaxSize()
            .background(DemonNavyBlack)
            .clickable { onStartGame() }
    ) {
        // Landscape background art
        Image(
            painter = painterResource(id = R.drawable.anime_game_bg),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Dark gradient overlay for contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xBB07080E),
                            Color(0x7707080E),
                            Color(0xF007080E)
                        )
                    )
                )
        )

        // Blood Moon at top-center
        BloodMoonCanvas(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
                .size(160.dp)
        )

        // Floating particle overlay
        AtmosphericParticleOverlay(
            modifier = Modifier.fillMaxSize(),
            particleColor = DemonCrimsonGlow
        )

        // Center Title & Vertical Katana Motif
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "鬼 滅 の 刃",
                color = DemonCrimsonGlow,
                fontSize = 18.sp,
                letterSpacing = 6.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "DEMON SLAYER",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "BLADE OF THE NIGHT",
                color = DemonGold,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Vertical Katana Stylized Centerpiece
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(90.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                DemonAetherCyan,
                                Color.White,
                                DemonCrimson,
                                DemonGold
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "❖", color = DemonGold, fontSize = 16.sp)
        }

        // Tap to Start Prompt at bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(DemonCrimsonDark.copy(alpha = promptAlpha * 0.7f))
                    .padding(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "— TAP TO START —",
                    color = Color.White.copy(alpha = promptAlpha),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "DEMON SLAYER ARC • ORIGINAL IP CONCEPT",
                color = DemonMistGray,
                fontSize = 10.sp,
                letterSpacing = 1.5.sp
            )
        }
    }
}
