package com.example.ui.effects

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.cos
import kotlin.math.sin

// ========================================================================
// ZERO-ALLOCATION PRE-COMPUTED PARTICLE CONSTANTS (OPTIMIZED FOR MOBILE)
// ========================================================================

private const val PARTICLE_COUNT = 32

// Pre-computed normalized directions, speeds, and spark lengths for 32 particles
// to prevent any object allocations during high-frequency frame drawing.
private val PARTICLE_ANGLES = FloatArray(PARTICLE_COUNT) { i ->
    val baseAngle = (i.toFloat() / PARTICLE_COUNT) * (2f * Math.PI.toFloat())
    val jitter = ((((i * 7 + 13) % 29) - 14) / 50f)
    baseAngle + jitter
}

private val PARTICLE_SPEEDS = FloatArray(PARTICLE_COUNT) { i ->
    val randOffset = ((i * 17 + 5) % 31) / 30f // deterministic 0.0 .. 1.0
    180f + randOffset * 280f
}

private val PARTICLE_LENGTHS = FloatArray(PARTICLE_COUNT) { i ->
    val randOffset = ((i * 23 + 11) % 19) / 18f
    14f + randOffset * 22f
}

private val PARTICLE_COLORS = arrayOf(
    Color(0xFFFFFFFF), // Radiant white core
    Color(0xFFFFDF60), // Solar gold
    Color(0xFFFFC107), // Demon gold
    Color(0xFF67E8F9), // Aether cyan
    Color(0xFF00F5FF), // Electric cyan
    Color(0xFFFF3366), // Crimson spark
    Color(0xFFFFFFFF), // Pure white
    Color(0xFFFFE082)  // Amber flare
)

/**
 * High-performance screen-flash and stylized anime impact particle overlay
 * triggered upon executing a successful 'Perfect Parry'.
 *
 * Implemented via direct DrawScope primitives on a single Canvas layer,
 * avoiding object allocations per frame for maximum 60/120fps performance on mobile.
 */
@Composable
fun PerfectParryEffectOverlay(
    trigger: Long,
    modifier: Modifier = Modifier,
    impactXRatio: Float = 0.5f,
    impactYRatio: Float = 0.52f,
    onEffectFinished: () -> Unit = {}
) {
    if (trigger <= 0L) return

    val animProgress = remember(trigger) { Animatable(0f) }

    LaunchedEffect(trigger) {
        animProgress.snapTo(0f)
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 480, easing = LinearEasing)
        )
        onEffectFinished()
    }

    val progress = animProgress.value
    if (progress >= 1f) return

    Box(
        modifier = modifier
            .testTag("perfect_parry_overlay")
            .fillMaxSize()
    ) {
        // ====================================================================
        // MOBILE-OPTIMIZED SINGLE CANVAS: FLASH & IMPACT PARTICLES
        // ====================================================================
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = size.width * impactXRatio.coerceIn(0.1f, 0.9f)
            val cy = size.height * impactYRatio.coerceIn(0.1f, 0.9f)
            val centerOffset = Offset(cx, cy)

            // 1. BLINDING SCREEN-FLASH (Rapid exponential decay in first 200ms)
            renderScreenFlash(progress, centerOffset)

            // 2. KINETIC SHOCKWAVE EXPANSION RINGS
            renderShockwaveRings(progress, centerOffset)

            // 3. ANIME STARBURST & CROSS FLARES (Peak clash deflection burst)
            renderStarburstCrossFlares(progress, centerOffset)

            // 4. HIGH-VELOCITY KINETIC SPARK STREAKS (32 zero-allocation particles)
            renderKineticSparkStreaks(progress, centerOffset)

            // 5. NICHIRIN BLADE DEFLECTION CRESCENT ARC
            renderDeflectionBladeArc(progress, centerOffset)
        }

        // ====================================================================
        // CINEMATIC IMPACT BANNER (SCALING WITH GLOWING ACCENTS)
        // ====================================================================
        val bannerScale = when {
            progress < 0.15f -> 0.7f + (progress / 0.15f) * 0.45f // snap in with impact overshoot
            progress < 0.25f -> 1.15f - ((progress - 0.15f) / 0.10f) * 0.15f // settle to 1.0
            else -> 1.0f + (progress - 0.25f) * 0.1f // subtle drift
        }
        val bannerAlpha = when {
            progress < 0.08f -> progress / 0.08f
            progress > 0.65f -> (1f - ((progress - 0.65f) / 0.35f)).coerceIn(0f, 1f)
            else -> 1f
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .scale(bannerScale)
                .alpha(bannerAlpha)
                .padding(horizontal = 24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp))
                    .background(Color(0xDD000000))
                    .border(
                        width = 1.5.dp,
                        brush = Brush.horizontalGradient(
                            listOf(Color.Transparent, DemonGold, Color.White, DemonGold, Color.Transparent)
                        ),
                        shape = CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp)
                    )
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⚡",
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "PERFECT PARRY",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 3.sp,
                        fontStyle = FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "⚡",
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "DEFLECTION 100% • POSTURE SHATTERED",
                    color = DemonGold,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.5.sp
                )
            }
        }
    }
}

// ========================================================================
// DRAW SCOPE RENDERING EXTENSIONS
// ========================================================================

/**
 * Blinding screen-flash layer:
 * Starts at pure luminous white with radial bloom, decaying swiftly over 220ms.
 */
private fun DrawScope.renderScreenFlash(progress: Float, center: Offset) {
    if (progress >= 0.45f) return

    val flashDecay = (1f - (progress / 0.45f)).let { it * it * it } // Cubic falloff
    val flashColor = Color.White.copy(alpha = (flashDecay * 0.92f).coerceIn(0f, 1f))

    // Fullscreen blinding flash
    drawRect(color = flashColor)

    // Intense radial bloom focused on the clash coordinate
    val bloomRadius = size.maxDimension * (0.35f + progress * 0.5f)
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color.White.copy(alpha = (flashDecay * 0.95f).coerceIn(0f, 1f)),
                DemonGold.copy(alpha = (flashDecay * 0.60f).coerceIn(0f, 1f)),
                Color(0xFF00F5FF).copy(alpha = (flashDecay * 0.25f).coerceIn(0f, 1f)),
                Color.Transparent
            ),
            center = center,
            radius = bloomRadius
        ),
        radius = bloomRadius,
        center = center
    )

    // Chromatic golden vignette on screen edges during peak flash
    val edgeAlpha = (flashDecay * 0.75f).coerceIn(0f, 1f)
    drawRect(
        brush = Brush.radialGradient(
            colors = listOf(Color.Transparent, DemonGold.copy(alpha = edgeAlpha)),
            center = Offset(size.width / 2f, size.height / 2f),
            radius = size.maxDimension * 0.7f
        )
    )
}

/**
 * Multiple concentric shockwave distortion rings expanding outward with fading strokes.
 */
private fun DrawScope.renderShockwaveRings(progress: Float, center: Offset) {
    // Wave 1: Primary Solar Gold Shockwave Ring
    val r1 = 15f + progress * (size.maxDimension * 0.42f)
    val stroke1 = (8f * (1f - progress)).coerceAtLeast(0.8f)
    val alpha1 = ((1f - progress) * 1.3f).coerceIn(0f, 1f)

    drawCircle(
        color = Color(0xFFFFE082).copy(alpha = alpha1),
        radius = r1,
        center = center,
        style = Stroke(width = stroke1)
    )

    // Wave 2: Fast Ethereal Cyan Shockwave Ring (slightly offset speed)
    val r2 = 10f + (progress * 1.15f).coerceIn(0f, 1f) * (size.maxDimension * 0.35f)
    val stroke2 = (5f * (1f - progress)).coerceAtLeast(0.6f)
    val alpha2 = ((1f - progress) * 0.9f).coerceIn(0f, 1f)

    drawCircle(
        color = Color(0xFF67E8F9).copy(alpha = alpha2),
        radius = r2,
        center = center,
        style = Stroke(width = stroke2)
    )

    // Wave 3: Ultra-fast inner white shockwave (dies early at 30% progress)
    if (progress < 0.30f) {
        val p3 = progress / 0.30f
        val r3 = 5f + p3 * 120f
        val alpha3 = (1f - p3).coerceIn(0f, 1f)
        drawCircle(
            color = Color.White.copy(alpha = alpha3),
            radius = r3,
            center = center,
            style = Stroke(width = 3f * (1f - p3))
        )
    }
}

/**
 * Stylized anime starburst diamond flares and sharp crossed slashes at the clash point.
 */
private fun DrawScope.renderStarburstCrossFlares(progress: Float, center: Offset) {
    if (progress >= 0.40f) return

    val burstProgress = progress / 0.40f
    val decay = (1f - burstProgress).let { it * it }
    val flareAlpha = decay.coerceIn(0f, 1f)

    // Horizontal sharp diamond beam
    val hWidth = (size.width * 0.45f) * (0.4f + burstProgress * 0.6f)
    val hHeight = (10f * decay).coerceAtLeast(1f)

    drawOval(
        brush = Brush.horizontalGradient(
            listOf(Color.Transparent, Color.White.copy(alpha = flareAlpha), DemonGold.copy(alpha = flareAlpha * 0.8f), Color.Transparent),
            startX = center.x - hWidth,
            endX = center.x + hWidth
        ),
        topLeft = Offset(center.x - hWidth, center.y - hHeight / 2f),
        size = Size(hWidth * 2f, hHeight)
    )

    // Vertical sharp diamond beam
    val vHeight = (size.height * 0.40f) * (0.4f + burstProgress * 0.6f)
    val vWidth = (8f * decay).coerceAtLeast(1f)

    drawOval(
        brush = Brush.verticalGradient(
            listOf(Color.Transparent, Color.White.copy(alpha = flareAlpha), DemonGold.copy(alpha = flareAlpha * 0.8f), Color.Transparent),
            startY = center.y - vHeight,
            endY = center.y + vHeight
        ),
        topLeft = Offset(center.x - vWidth / 2f, center.y - vHeight),
        size = Size(vWidth, vHeight * 2f)
    )

    // 45-degree diagonal crossed deflection sparks
    val diagLength = 70f * (0.5f + burstProgress * 0.5f)
    val dOffset1 = Offset(cos(0.785f) * diagLength, sin(0.785f) * diagLength)
    val dOffset2 = Offset(cos(2.356f) * diagLength, sin(2.356f) * diagLength)

    drawLine(
        color = Color.White.copy(alpha = flareAlpha),
        start = center - dOffset1,
        end = center + dOffset1,
        strokeWidth = 3.5f * decay,
        cap = StrokeCap.Round
    )

    drawLine(
        color = Color(0xFF67E8F9).copy(alpha = flareAlpha),
        start = center - dOffset2,
        end = center + dOffset2,
        strokeWidth = 3f * decay,
        cap = StrokeCap.Round
    )
}

/**
 * 32 high-velocity kinetic spark particles flying outward with randomized angles,
 * deceleration, and trail lengths without runtime allocations.
 */
private fun DrawScope.renderKineticSparkStreaks(progress: Float, center: Offset) {
    // Decelerating kinetic physics curve: sqrt/power progression
    val distanceProgress = java.lang.Math.pow(progress.toDouble(), 0.62).toFloat()
    val fadeProgress = (1f - progress).let { it * it }

    for (i in 0 until PARTICLE_COUNT) {
        val angle = PARTICLE_ANGLES[i]
        val maxSpeed = PARTICLE_SPEEDS[i]
        val sparkLen = PARTICLE_LENGTHS[i] * (1f - progress * 0.6f)
        val color = PARTICLE_COLORS[i % PARTICLE_COLORS.size].copy(alpha = (fadeProgress * 0.95f).coerceIn(0f, 1f))

        val currentDist = maxSpeed * distanceProgress
        val tailDist = (currentDist - sparkLen).coerceAtLeast(0f)

        val cosA = cos(angle)
        val sinA = sin(angle)

        val headX = center.x + cosA * currentDist
        val headY = center.y + sinA * currentDist

        val tailX = center.x + cosA * tailDist
        val tailY = center.y + sinA * tailDist

        // Draw spark trajectory line with tapering stroke
        drawLine(
            color = color,
            start = Offset(tailX, tailY),
            end = Offset(headX, headY),
            strokeWidth = (3.2f * fadeProgress).coerceAtLeast(0.7f),
            cap = StrokeCap.Round
        )

        // Draw luminous spark head point
        drawCircle(
            color = Color.White.copy(alpha = fadeProgress),
            radius = (2.2f * fadeProgress).coerceAtLeast(0.5f),
            center = Offset(headX, headY)
        )
    }
}

/**
 * Stylized crescent blade deflection arc simulating Kai's Nichirin blade swipe meeting the attack.
 */
private fun DrawScope.renderDeflectionBladeArc(progress: Float, center: Offset) {
    if (progress >= 0.35f) return

    val arcProgress = progress / 0.35f
    val arcAlpha = (1f - arcProgress).coerceIn(0f, 1f)
    val arcRadius = 45f + arcProgress * 55f

    drawArc(
        brush = Brush.sweepGradient(
            listOf(
                Color.Transparent,
                Color(0xFF00F5FF).copy(alpha = arcAlpha * 0.5f),
                Color.White.copy(alpha = arcAlpha),
                DemonGold.copy(alpha = arcAlpha * 0.8f),
                Color.Transparent
            ),
            center = center
        ),
        startAngle = 180f + arcProgress * 60f,
        sweepAngle = 110f,
        useCenter = false,
        topLeft = Offset(center.x - arcRadius, center.y - arcRadius),
        size = Size(arcRadius * 2f, arcRadius * 2f),
        style = Stroke(width = (4f * (1f - arcProgress)).coerceAtLeast(1f), cap = StrokeCap.Round)
    )
}
