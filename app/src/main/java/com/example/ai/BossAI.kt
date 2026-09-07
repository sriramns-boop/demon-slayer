package com.example.ai

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.sqrt
import kotlin.random.Random

// ========================================================================
// FINITE STATE MACHINE (FSM) STATE HIERARCHY
// ========================================================================

/**
 * Finite State Machine (FSM) states governing Boss behaviors in combat encounters.
 */
sealed class BossBehaviorState {
    abstract val stateName: String

    /**
     * Idle State:
     * Boss circles the arena, observes player distance, maintains guard,
     * and prepares attack execution once cooldown interval expires.
     */
    data class Idle(
        val duration: Float,
        val elapsed: Float = 0f,
        val targetX: Float = 0f,
        val targetY: Float = -0.3f
    ) : BossBehaviorState() {
        override val stateName: String = "IDLE"
        val remaining: Float get() = (duration - elapsed).coerceAtLeast(0f)
        val isFinished: Boolean get() = elapsed >= duration
    }

    /**
     * Attack State:
     * Multi-step attack sequence:
     * - TELEGRAPH: Windup phase displaying hazard telegraph zone & audio/visual cue.
     * - ACTIVE: The strike frame dealing damage to player if within hitbox.
     * - RECOVERY: Cooldown follow-through where boss cannot act.
     */
    data class Attack(
        val pattern: BossAttackPattern,
        val step: AttackStep = AttackStep.TELEGRAPH,
        val stepElapsed: Float = 0f,
        val hitDelivered: Boolean = false
    ) : BossBehaviorState() {
        override val stateName: String = "ATTACK (${step.name})"
    }

    /**
     * Phase Transition State:
     * Triggered upon HP phase boundary. Boss becomes invulnerable, channels elemental
     * shockwave, transforms visual styles, roars, and transitions to the next form.
     */
    data class PhaseTransition(
        val fromPhase: Int,
        val toPhase: Int,
        val duration: Float = 2.4f,
        val elapsed: Float = 0f,
        val shockwaveTriggered: Boolean = false
    ) : BossBehaviorState() {
        override val stateName: String = "PHASE TRANSITION"
        val progress: Float get() = (elapsed / duration).coerceIn(0f, 1f)
        val isFinished: Boolean get() = elapsed >= duration
    }

    /**
     * Stagger State:
     * Triggered when boss posture/stagger bar is broken (or perfect parry counter).
     * Boss drops to knees, defenses collapse, takes 200% critical damage for duration.
     */
    data class Stagger(
        val duration: Float,
        val elapsed: Float = 0f,
        val recoveryTriggered: Boolean = false
    ) : BossBehaviorState() {
        override val stateName: String = "STAGGER"
        val remaining: Float get() = (duration - elapsed).coerceAtLeast(0f)
        val progress: Float get() = (elapsed / duration).coerceIn(0f, 1f)
        val isFinished: Boolean get() = elapsed >= duration
    }

    /**
     * Defeated State: Final cinematic collapse.
     */
    object Defeated : BossBehaviorState() {
        override val stateName: String = "DEFEATED"
    }
}

enum class AttackStep {
    TELEGRAPH, // Hazard zone warning with particle buildup
    ACTIVE,    // Strike frame dealing damage
    RECOVERY   // Post-strike vulnerable follow-through
}

// ========================================================================
// ADJUSTABLE DIFFICULTY SCALING
// ========================================================================

/**
 * Difficulty scaling adjusting boss AI aggression, damage, telegraph reaction windows,
 * stagger recovery times, and enrage thresholds.
 */
enum class BossDifficulty(
    val label: String,
    val description: String,
    val attackFrequencyMultiplier: Float, // higher = shorter idle, faster attacks
    val damageMultiplier: Float,
    val telegraphSpeedMultiplier: Float, // <1.0 = faster telegraph (harder), >1.0 = slower (easier)
    val staggerDurationSeconds: Float,
    val enrageThresholdPct: Float
) {
    EASY(
        label = "EASY",
        description = "Generous windup telegraphs with extended stagger punish windows.",
        attackFrequencyMultiplier = 0.65f,
        damageMultiplier = 0.60f,
        telegraphSpeedMultiplier = 1.40f,
        staggerDurationSeconds = 5.0f,
        enrageThresholdPct = 0.20f
    ),
    NORMAL(
        label = "NORMAL",
        description = "Balanced anime dark fantasy combat pace with standard parry cues.",
        attackFrequencyMultiplier = 1.0f,
        damageMultiplier = 1.0f,
        telegraphSpeedMultiplier = 1.0f,
        staggerDurationSeconds = 3.5f,
        enrageThresholdPct = 0.35f
    ),
    HARD(
        label = "HARD",
        description = "Aggressive combination chaining, fast telegraphs, and heavy damage.",
        attackFrequencyMultiplier = 1.35f,
        damageMultiplier = 1.45f,
        telegraphSpeedMultiplier = 0.75f,
        staggerDurationSeconds = 2.4f,
        enrageThresholdPct = 0.50f
    ),
    NIGHTMARE(
        label = "NIGHTMARE",
        description = "Lethal caliber with lightning-fast telegraphs, unblockables, and brutal frenzy.",
        attackFrequencyMultiplier = 1.80f,
        damageMultiplier = 2.20f,
        telegraphSpeedMultiplier = 0.55f,
        staggerDurationSeconds = 1.6f,
        enrageThresholdPct = 0.65f
    )
}

// ========================================================================
// MODULAR ATTACK PATTERNS BY PHASE
// ========================================================================

data class BossAttackPattern(
    val id: String,
    val name: String,
    val phaseRequired: Int,
    val baseDamage: Int,
    val telegraphDurationSeconds: Float,
    val activeDurationSeconds: Float,
    val recoveryDurationSeconds: Float,
    val isUnblockable: Boolean,
    val telegraphHint: String,
    val element: String
)

val BossAttackPool = listOf(
    // Phase 1: Blood Monarch Attacks
    BossAttackPattern(
        id = "blood_claws",
        name = "Blood Claws Combo",
        phaseRequired = 1,
        baseDamage = 280,
        telegraphDurationSeconds = 0.9f,
        activeDurationSeconds = 0.35f,
        recoveryDurationSeconds = 0.6f,
        isUnblockable = false,
        telegraphHint = "⚠️ Frontal double claw swipe. Block or Dodge forward.",
        element = "Blood"
    ),
    BossAttackPattern(
        id = "crimson_crescent",
        name = "Crimson Crescent Wave",
        phaseRequired = 1,
        baseDamage = 340,
        telegraphDurationSeconds = 1.1f,
        activeDurationSeconds = 0.4f,
        recoveryDurationSeconds = 0.7f,
        isUnblockable = false,
        telegraphHint = "⚠️ Ranged blood projectile wave. Dodge laterally.",
        element = "Blood"
    ),
    BossAttackPattern(
        id = "blood_pool_eruption",
        name = "Blood Geyser Eruption",
        phaseRequired = 1,
        baseDamage = 450,
        telegraphDurationSeconds = 1.3f,
        activeDurationSeconds = 0.5f,
        recoveryDurationSeconds = 0.9f,
        isUnblockable = true,
        telegraphHint = "⛔ UNBLOCKABLE: Demonic ground pool bursting. Jump or roll away!",
        element = "Blood"
    ),

    // Phase 2: Demon Titan Attacks
    BossAttackPattern(
        id = "titan_quake_slam",
        name = "Titan Earthbreaker Slam",
        phaseRequired = 2,
        baseDamage = 520,
        telegraphDurationSeconds = 1.0f,
        activeDurationSeconds = 0.45f,
        recoveryDurationSeconds = 0.7f,
        isUnblockable = true,
        telegraphHint = "⛔ UNBLOCKABLE: Frontal shockwave slam. Jump to evade!",
        element = "Molten"
    ),
    BossAttackPattern(
        id = "molten_meteor_barrage",
        name = "Molten Meteor Rain",
        phaseRequired = 2,
        baseDamage = 580,
        telegraphDurationSeconds = 1.4f,
        activeDurationSeconds = 0.6f,
        recoveryDurationSeconds = 0.8f,
        isUnblockable = false,
        telegraphHint = "⚠️ Falling volcanic magma embers. Sprint out of blast radius.",
        element = "Fire"
    ),

    // Phase 3: Immortal Shadow Attacks
    BossAttackPattern(
        id = "shadow_blink_slash",
        name = "Shadow Blink Execution",
        phaseRequired = 3,
        baseDamage = 640,
        telegraphDurationSeconds = 0.8f,
        activeDurationSeconds = 0.35f,
        recoveryDurationSeconds = 0.5f,
        isUnblockable = false,
        telegraphHint = "⚠️ Instant teleport strike behind player. Perfect Parry ready!",
        element = "Void"
    ),
    BossAttackPattern(
        id = "void_eclipse_whirlwind",
        name = "Void Eclipse Tempest",
        phaseRequired = 3,
        baseDamage = 720,
        telegraphDurationSeconds = 1.1f,
        activeDurationSeconds = 0.5f,
        recoveryDurationSeconds = 0.8f,
        isUnblockable = true,
        telegraphHint = "⛔ UNBLOCKABLE: 360-degree astral blade spin. Evade backward!",
        element = "Void"
    ),

    // Phase 4: Final Dawn Attacks
    BossAttackPattern(
        id = "solar_flare_burst",
        name = "Solar Calamity Nova",
        phaseRequired = 4,
        baseDamage = 850,
        telegraphDurationSeconds = 1.2f,
        activeDurationSeconds = 0.6f,
        recoveryDurationSeconds = 0.9f,
        isUnblockable = true,
        telegraphHint = "⛔ APOCALYPTIC NOVA: Channeling dawnbringer blast. Take cover!",
        element = "Solar"
    ),
    BossAttackPattern(
        id = "radiant_judgment_beam",
        name = "Radiant Judgment Beam",
        phaseRequired = 4,
        baseDamage = 920,
        telegraphDurationSeconds = 0.9f,
        activeDurationSeconds = 0.45f,
        recoveryDurationSeconds = 0.6f,
        isUnblockable = false,
        telegraphHint = "⚠️ High-velocity celestial laser beam. Perfect Dodge required!",
        element = "Solar"
    )
)

// ========================================================================
// FINITE STATE MACHINE CONTROLLER (BossAI)
// ========================================================================

/**
 * Result returned upon each AI update tick.
 */
data class BossAIUpdateResult(
    val state: BossBehaviorState,
    val stateChanged: Boolean,
    val damageDealtToPlayer: Int = 0,
    val isUnblockableAttack: Boolean = false,
    val telegraphWarningText: String? = null,
    val shockwaveTriggered: Boolean = false,
    val attackSoundEvent: String? = null
)

/**
 * Damage reaction result when player hits the boss.
 */
data class BossDamageReaction(
    val newHpPercentage: Float,
    val newStaggerPercentage: Float,
    val triggeredStagger: Boolean,
    val triggeredPhaseTransition: Boolean,
    val targetPhase: Int? = null
)

/**
 * Modular BossAI Finite State Machine managing boss combat behaviors (Idle, Attack,
 * Phase Transition, Stagger) with adjustable difficulty scaling.
 */
class BossAI(
    initialDifficulty: BossDifficulty = BossDifficulty.NORMAL,
    initialPhase: Int = 1
) {
    var difficulty: BossDifficulty = initialDifficulty
        private set

    var currentPhase: Int = initialPhase
        private set

    var currentState: BossBehaviorState = BossBehaviorState.Idle(duration = 2.0f)
        private set

    var staggerGauge: Float = 1.0f // 1.0 = fully poised, 0.0 = broken
        private set

    var isEnraged: Boolean = false
        private set

    var bossHpPct: Float = 1.0f
        private set

    var maxPhases: Int = 4

    // Event listener callbacks
    var onStateChanged: ((BossBehaviorState) -> Unit)? = null
    var onAttackTelegraph: ((BossAttackPattern, Float) -> Unit)? = null
    var onAttackHit: ((BossAttackPattern, Int, Boolean) -> Unit)? = null
    var onPhaseTransition: ((Int, Int) -> Unit)? = null
    var onStaggered: (() -> Unit)? = null
    var onStaggerRecovered: (() -> Unit)? = null

    fun setDifficulty(newDifficulty: BossDifficulty) {
        this.difficulty = newDifficulty
    }

    fun setPhase(phase: Int) {
        val clamped = phase.coerceIn(1, maxPhases)
        if (clamped != currentPhase) {
            triggerPhaseTransition(clamped)
        }
    }

    fun triggerPhaseTransition(toPhase: Int) {
        val oldState = currentState
        currentState = BossBehaviorState.PhaseTransition(
            fromPhase = currentPhase,
            toPhase = toPhase,
            duration = 2.4f
        )
        onStateChanged?.invoke(currentState)
        onPhaseTransition?.invoke(currentPhase, toPhase)
    }

    fun forceStagger() {
        staggerGauge = 0f
        val duration = difficulty.staggerDurationSeconds
        currentState = BossBehaviorState.Stagger(duration = duration)
        onStateChanged?.invoke(currentState)
        onStaggered?.invoke()
    }

    fun triggerAttack(pattern: BossAttackPattern? = null) {
        val selected = pattern ?: selectNextAttack()
        val telegDuration = selected.telegraphDurationSeconds * difficulty.telegraphSpeedMultiplier
        currentState = BossBehaviorState.Attack(
            pattern = selected,
            step = AttackStep.TELEGRAPH,
            stepElapsed = 0f
        )
        onStateChanged?.invoke(currentState)
        onAttackTelegraph?.invoke(selected, telegDuration)
    }

    /**
     * Periodic update tick of the Boss FSM.
     */
    fun update(
        deltaTimeSeconds: Float,
        playerPosX: Float = 0f,
        playerPosY: Float = 0f
    ): BossAIUpdateResult {
        var stateChanged = false
        var damageDealt = 0
        var isUnblockable = false
        var telegraphWarning: String? = null
        var shockwave = false
        var soundEvent: String? = null

        when (val state = currentState) {
            is BossBehaviorState.Idle -> {
                val newElapsed = state.elapsed + deltaTimeSeconds
                if (newElapsed >= state.duration) {
                    // Idle ended, select next attack pattern
                    val nextPattern = selectNextAttack()
                    val telegDuration = nextPattern.telegraphDurationSeconds * difficulty.telegraphSpeedMultiplier

                    currentState = BossBehaviorState.Attack(
                        pattern = nextPattern,
                        step = AttackStep.TELEGRAPH,
                        stepElapsed = 0f
                    )
                    stateChanged = true
                    telegraphWarning = nextPattern.telegraphHint
                    onStateChanged?.invoke(currentState)
                    onAttackTelegraph?.invoke(nextPattern, telegDuration)
                } else {
                    // Slowly regenerate stagger poise while idling
                    if (staggerGauge < 1.0f) {
                        staggerGauge = (staggerGauge + 0.05f * deltaTimeSeconds).coerceAtMost(1.0f)
                    }
                    currentState = state.copy(elapsed = newElapsed)
                }
            }

            is BossBehaviorState.Attack -> {
                val pattern = state.pattern
                val telegDuration = pattern.telegraphDurationSeconds * difficulty.telegraphSpeedMultiplier
                val activeDuration = pattern.activeDurationSeconds
                val recovDuration = pattern.recoveryDurationSeconds

                when (state.step) {
                    AttackStep.TELEGRAPH -> {
                        val newElapsed = state.stepElapsed + deltaTimeSeconds
                        telegraphWarning = pattern.telegraphHint
                        if (newElapsed >= telegDuration) {
                            // Transition to ACTIVE strike frame
                            currentState = state.copy(
                                step = AttackStep.ACTIVE,
                                stepElapsed = 0f,
                                hitDelivered = false
                            )
                            stateChanged = true
                            soundEvent = "BOSS_ATTACK_STRIKE"
                            onStateChanged?.invoke(currentState)
                        } else {
                            currentState = state.copy(stepElapsed = newElapsed)
                        }
                    }

                    AttackStep.ACTIVE -> {
                        val newElapsed = state.stepElapsed + deltaTimeSeconds
                        if (!state.hitDelivered) {
                            // Calculate damage with difficulty scaling
                            val baseDmg = pattern.baseDamage
                            val scaledDmg = (baseDmg * difficulty.damageMultiplier).toInt()
                            damageDealt = scaledDmg
                            isUnblockable = pattern.isUnblockable
                            onAttackHit?.invoke(pattern, scaledDmg, isUnblockable)
                        }

                        if (newElapsed >= activeDuration) {
                            // Transition to RECOVERY phase
                            currentState = state.copy(
                                step = AttackStep.RECOVERY,
                                stepElapsed = 0f,
                                hitDelivered = true
                            )
                            stateChanged = true
                            onStateChanged?.invoke(currentState)
                        } else {
                            currentState = state.copy(stepElapsed = newElapsed, hitDelivered = true)
                        }
                    }

                    AttackStep.RECOVERY -> {
                        val newElapsed = state.stepElapsed + deltaTimeSeconds
                        if (newElapsed >= recovDuration) {
                            // Recovery finished: return to IDLE with difficulty-scaled duration
                            val baseIdle = 1.8f + Random.nextFloat() * 1.0f
                            val scaledIdle = baseIdle / difficulty.attackFrequencyMultiplier
                            currentState = BossBehaviorState.Idle(duration = scaledIdle)
                            stateChanged = true
                            onStateChanged?.invoke(currentState)
                        } else {
                            currentState = state.copy(stepElapsed = newElapsed)
                        }
                    }
                }
            }

            is BossBehaviorState.PhaseTransition -> {
                val newElapsed = state.elapsed + deltaTimeSeconds
                val halfTime = state.duration * 0.5f

                if (!state.shockwaveTriggered && newElapsed >= halfTime) {
                    shockwave = true
                    currentPhase = state.toPhase
                    staggerGauge = 1.0f
                    soundEvent = "PHASE_SHOCKWAVE"
                    currentState = state.copy(elapsed = newElapsed, shockwaveTriggered = true)
                } else if (newElapsed >= state.duration) {
                    // Transition complete: start with aggressive short idle
                    currentState = BossBehaviorState.Idle(duration = 1.2f)
                    stateChanged = true
                    onStateChanged?.invoke(currentState)
                } else {
                    currentState = state.copy(elapsed = newElapsed)
                }
            }

            is BossBehaviorState.Stagger -> {
                val newElapsed = state.elapsed + deltaTimeSeconds
                if (newElapsed >= state.duration) {
                    // Recover from stagger: restore poise, brief recovery
                    staggerGauge = 1.0f
                    currentState = BossBehaviorState.Idle(duration = 1.5f)
                    stateChanged = true
                    onStateChanged?.invoke(currentState)
                    onStaggerRecovered?.invoke()
                } else {
                    currentState = state.copy(elapsed = newElapsed)
                }
            }

            is BossBehaviorState.Defeated -> {
                // Boss stays defeated
            }
        }

        return BossAIUpdateResult(
            state = currentState,
            stateChanged = stateChanged,
            damageDealtToPlayer = damageDealt,
            isUnblockableAttack = isUnblockable,
            telegraphWarningText = telegraphWarning,
            shockwaveTriggered = shockwave,
            attackSoundEvent = soundEvent
        )
    }

    /**
     * Process player damage against boss posture & HP.
     */
    fun onPlayerDamageReceived(
        damage: Int,
        staggerDamage: Float = 0.08f
    ): BossDamageReaction {
        if (currentState is BossBehaviorState.Defeated || currentState is BossBehaviorState.PhaseTransition) {
            return BossDamageReaction(
                newHpPercentage = bossHpPct,
                newStaggerPercentage = staggerGauge,
                triggeredStagger = false,
                triggeredPhaseTransition = false
            )
        }

        // Apply stagger reduction
        var triggeredStagger = false
        staggerGauge = (staggerGauge - staggerDamage).coerceAtLeast(0f)
        if (staggerGauge <= 0f && currentState !is BossBehaviorState.Stagger) {
            forceStagger()
            triggeredStagger = true
        }

        // Apply HP reduction
        val totalHpVal = bossHpPct * 10000f
        val dmgMultiplier = if (currentState is BossBehaviorState.Stagger) 2.0f else 1.0f
        val actualDmg = damage * dmgMultiplier
        val newHpVal = (totalHpVal - actualDmg).coerceAtLeast(0f)
        bossHpPct = newHpVal / 10000f

        var triggeredPhase = false
        var targetPhase: Int? = null

        // Check phase advancement condition
        if (bossHpPct <= 0f) {
            if (currentPhase < maxPhases) {
                targetPhase = currentPhase + 1
                bossHpPct = 1.0f
                staggerGauge = 1.0f
                triggerPhaseTransition(targetPhase)
                triggeredPhase = true
            } else {
                currentState = BossBehaviorState.Defeated
                onStateChanged?.invoke(currentState)
            }
        } else if (bossHpPct <= difficulty.enrageThresholdPct && !isEnraged) {
            isEnraged = true
        }

        return BossDamageReaction(
            newHpPercentage = bossHpPct,
            newStaggerPercentage = staggerGauge,
            triggeredStagger = triggeredStagger,
            triggeredPhaseTransition = triggeredPhase,
            targetPhase = targetPhase
        )
    }

    private fun selectNextAttack(): BossAttackPattern {
        val eligible = BossAttackPool.filter { it.phaseRequired <= currentPhase }
        return if (eligible.isNotEmpty()) {
            eligible.random()
        } else {
            BossAttackPool.first()
        }
    }

    fun reset(initialPhase: Int = 1) {
        currentPhase = initialPhase.coerceIn(1, maxPhases)
        bossHpPct = 1.0f
        staggerGauge = 1.0f
        isEnraged = false
        currentState = BossBehaviorState.Idle(duration = 2.0f)
        onStateChanged?.invoke(currentState)
    }
}

// ========================================================================
// MODULAR COMPOSABLE UI: BossAIControlOverlay
// ========================================================================

/**
 * Visual inspection and difficulty tuning control panel for the BossAI finite state machine.
 */
@Composable
fun BossAIControlOverlay(
    bossAI: BossAI,
    onDifficultyChanged: (BossDifficulty) -> Unit,
    onManualAttack: () -> Unit,
    onManualStagger: () -> Unit,
    onManualPhase: () -> Unit,
    onTriggerParry: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val stateColor = when (bossAI.currentState) {
        is BossBehaviorState.Idle -> DemonAetherCyan
        is BossBehaviorState.Attack -> NeonCrimson
        is BossBehaviorState.PhaseTransition -> DemonMoonPurple
        is BossBehaviorState.Stagger -> DemonGold
        is BossBehaviorState.Defeated -> Color.Gray
    }

    Column(
        modifier = modifier
            .testTag("boss_ai_overlay")
            .fillMaxWidth()
            .clip(CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp))
            .background(Color(0xEB07080E))
            .border(1.dp, stateColor.copy(alpha = 0.6f), CutCornerShape(topStart = 8.dp, bottomEnd = 8.dp))
            .padding(8.dp)
    ) {
        // Header Row: Active FSM State & Difficulty Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(stateColor)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AI FSM: ${bossAI.currentState.stateName}",
                    color = stateColor,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }

            // Expand/collapse controls
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0x33FFFFFF))
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (expanded) "HIDE AI OPS" else "AI SETTINGS",
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Active Attack Telegraph Alert Bar (if currently in telegraph)
        val attackState = bossAI.currentState as? BossBehaviorState.Attack
        if (attackState != null && attackState.step == AttackStep.TELEGRAPH) {
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color(0x66FF1E46))
                    .border(1.dp, NeonCrimson, RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = attackState.pattern.telegraphHint,
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }

        // Collapsible Tuning Controls
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(top = 6.dp)) {
                // Difficulty Selector
                Text(
                    text = "DIFFICULTY SCALING (ADJUSTS AGGRESSION, DMG & STAGGER):",
                    color = DemonMistGray,
                    fontSize = 7.5.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    BossDifficulty.values().forEach { diff ->
                        val isSelected = diff == bossAI.difficulty
                        val diffColor = when (diff) {
                            BossDifficulty.EASY -> AtmosphericEmerald
                            BossDifficulty.NORMAL -> DemonAetherCyan
                            BossDifficulty.HARD -> DemonAmber
                            BossDifficulty.NIGHTMARE -> NeonCrimson
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (isSelected) diffColor.copy(alpha = 0.35f) else Color(0x1AFFFFFF))
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.5.dp,
                                    color = if (isSelected) diffColor else Color(0x33FFFFFF),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .clickable {
                                    bossAI.setDifficulty(diff)
                                    onDifficultyChanged(diff)
                                }
                                .padding(vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = diff.label,
                                color = if (isSelected) Color.White else DemonMistGray,
                                fontSize = 8.sp,
                                fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Manual FSM Trigger Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0x33DC2626))
                            .border(1.dp, NeonCrimson, RoundedCornerShape(4.dp))
                            .clickable { onManualAttack() }
                            .padding(vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("FORCE ATTACK", color = Color.White, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0x33D97706))
                            .border(1.dp, DemonGold, RoundedCornerShape(4.dp))
                            .clickable { onManualStagger() }
                            .padding(vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("FORCE STAGGER", color = Color.White, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0x33F59E0B))
                            .border(1.dp, Color(0xFFFFD700), RoundedCornerShape(4.dp))
                            .clickable { onTriggerParry() }
                            .padding(vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⚡ TEST PARRY", color = Color.White, fontSize = 7.5.sp, fontWeight = FontWeight.Black)
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0x337C3AED))
                            .border(1.dp, DemonMoonPurple, RoundedCornerShape(4.dp))
                            .clickable { onManualPhase() }
                            .padding(vertical = 3.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("NEXT PHASE", color = Color.White, fontSize = 7.5.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
