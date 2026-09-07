package com.example.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun OrnateGlassPanel(
    modifier: Modifier = Modifier,
    borderColor: Color = AtmosphericBorderWhite10,
    cornerSize: Dp = 16.dp,
    backgroundColor: Color = AtmosphericGlassBlack,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerSize))
            .background(backgroundColor)
            .border(
                BorderStroke(
                    1.dp,
                    Brush.linearGradient(
                        listOf(
                            borderColor,
                            Color(0x33FFFFFF),
                            borderColor.copy(alpha = 0.5f),
                            Color(0x0DFFFFFF)
                        )
                    )
                ),
                RoundedCornerShape(cornerSize)
            )
            .padding(1.dp)
    ) {
        content()
    }
}

@Composable
fun AnimeMenuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subText: String? = null,
    icon: String? = null,
    isPrimary: Boolean = false,
    testTag: String = "menu_button"
) {
    val borderColor = if (isPrimary) DemonCrimson.copy(alpha = 0.8f) else AtmosphericBorderWhite10
    val bgBrush = if (isPrimary) {
        Brush.horizontalGradient(
            listOf(
                Color(0xCC7F1D1D),
                Color(0xAA1E1B4B),
                Color(0x88050508)
            )
        )
    } else {
        Brush.horizontalGradient(
            listOf(
                Color(0x660F172A),
                Color(0x44050508)
            )
        )
    }

    Box(
        modifier = modifier
            .testTag(testTag)
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(bgBrush)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (icon != null) {
                    Text(
                        text = icon,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
                Column {
                    Text(
                        text = text,
                        color = if (isPrimary) DemonGold else Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    if (subText != null) {
                        Text(
                            text = subText,
                            color = DemonMistGray,
                            fontSize = 10.sp
                        )
                    }
                }
            }
            Text(
                text = "❯",
                color = if (isPrimary) DemonCrimsonGlow else DemonAetherCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun JapaneseBrushHeader(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "❖", color = DemonCrimson, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title.uppercase(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "❖", color = DemonCrimson, fontSize = 14.sp)
        }
        if (subtitle != null) {
            Text(
                text = subtitle,
                color = DemonGold,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp
            )
        }
    }
}
