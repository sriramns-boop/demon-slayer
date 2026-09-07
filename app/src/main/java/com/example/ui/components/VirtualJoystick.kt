package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.roundToInt
import kotlin.math.sqrt

@Composable
fun VirtualJoystick(
    modifier: Modifier = Modifier,
    scale: Float = 1.0f,
    opacity: Float = 0.85f,
    isSprinting: Boolean,
    onSprintToggle: (Boolean) -> Unit,
    autoRun: Boolean,
    onAutoRunToggle: (Boolean) -> Unit,
    onMove: (Float, Float) -> Unit
) {
    val outerRadiusPx = 130f * scale
    var thumbOffset by remember { mutableStateOf(Offset.Zero) }

    Column(
        modifier = modifier.testTag("virtual_joystick_container"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Upper utility toggles: Auto-run & Sprint
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Auto Run button
            Box(
                modifier = Modifier
                    .testTag("auto_run_button")
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (autoRun) DemonCrimson else DemonNavyGlass.copy(alpha = opacity))
                    .border(1.dp, if (autoRun) DemonGold else DemonAetherCyan.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .clickable { onAutoRunToggle(!autoRun) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = "Auto Run",
                        tint = if (autoRun) Color.White else DemonMistGray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (autoRun) "AUTO RUN: ON" else "AUTO RUN",
                        color = if (autoRun) Color.White else DemonTextMuted,
                        fontSize = 9.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Sprint Toggle
            Box(
                modifier = Modifier
                    .testTag("sprint_button")
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSprinting) DemonAetherBlue else DemonNavyGlass.copy(alpha = opacity))
                    .border(1.dp, if (isSprinting) DemonAetherCyan else DemonAetherCyan.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .clickable { onSprintToggle(!isSprinting) }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DirectionsRun,
                        contentDescription = "Sprint",
                        tint = if (isSprinting) Color.White else DemonMistGray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isSprinting) "SPRINT: ON" else "SPRINT",
                        color = if (isSprinting) Color.White else DemonTextMuted,
                        fontSize = 9.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        // Circular Joystick Base (Atmospheric / Immersive Media Design)
        val outerSizeDp = (100 * scale).dp
        Box(
            modifier = Modifier
                .size(outerSizeDp)
                .clip(CircleShape)
                .background(Color(0x14FFFFFF))
                .border(
                    1.5.dp,
                    AtmosphericBorderWhite10,
                    CircleShape
                )
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            thumbOffset = Offset.Zero
                            onMove(0f, 0f)
                        },
                        onDragCancel = {
                            thumbOffset = Offset.Zero
                            onMove(0f, 0f)
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            val newOffset = thumbOffset + dragAmount
                            val dist = sqrt(newOffset.x * newOffset.x + newOffset.y * newOffset.y)
                            val maxR = outerRadiusPx * 0.42f
                            thumbOffset = if (dist > maxR) {
                                Offset((newOffset.x / dist) * maxR, (newOffset.y / dist) * maxR)
                            } else {
                                newOffset
                            }
                            val normX = (thumbOffset.x / maxR).coerceIn(-1f, 1f)
                            val normY = (thumbOffset.y / maxR).coerceIn(-1f, 1f)
                            onMove(normX, normY)
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // Subtle ring
            Box(
                modifier = Modifier
                    .size((outerSizeDp * 0.55f))
                    .clip(CircleShape)
                    .border(0.5.dp, Color(0x14FFFFFF), CircleShape)
            )

            // Center Knob Thumb (Immersive Media glass puck)
            val thumbSizeDp = (38 * scale).dp
            Box(
                modifier = Modifier
                    .offset { IntOffset(thumbOffset.x.roundToInt(), thumbOffset.y.roundToInt()) }
                    .size(thumbSizeDp)
                    .clip(CircleShape)
                    .background(Color(0x33FFFFFF))
                    .border(1.dp, Color(0x66FFFFFF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(DemonAetherCyan.copy(alpha = 0.8f))
                )
            }
        }
    }
}
