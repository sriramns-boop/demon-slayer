package com.example

import com.example.model.BossState
import com.example.ui.components.getPhaseVisualTheme
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests verifying Boss Phase Visual Themes and state transitions.
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `test boss phase 1 theme configuration`() {
        val theme = getPhaseVisualTheme(1)
        assertEquals(1, theme.phaseNumber)
        assertEquals("BLOOD MONARCH", theme.phaseName)
        assertEquals("🩸", theme.icon)
        assertTrue(theme.healthGradient.isNotEmpty())
    }

    @Test
    fun `test boss phase 2 theme configuration`() {
        val theme = getPhaseVisualTheme(2)
        assertEquals(2, theme.phaseNumber)
        assertEquals("DEMON TITAN", theme.phaseName)
        assertEquals("🔥", theme.icon)
    }

    @Test
    fun `test boss phase 3 theme configuration`() {
        val theme = getPhaseVisualTheme(3)
        assertEquals(3, theme.phaseNumber)
        assertEquals("IMMORTAL SHADOW", theme.phaseName)
        assertEquals("🌑", theme.icon)
    }

    @Test
    fun `test boss phase 4 theme configuration`() {
        val theme = getPhaseVisualTheme(4)
        assertEquals(4, theme.phaseNumber)
        assertEquals("FINAL DAWN", theme.phaseName)
        assertEquals("☀️", theme.icon)
    }

    @Test
    fun `test boss state defaults`() {
        val boss = BossState()
        assertEquals(1, boss.currentPhase)
        assertEquals(4, boss.maxPhases)
        assertEquals(1.0f, boss.hpPercentage, 0.001f)
        assertFalse(boss.isDefeated)
    }

    @Test
    fun `test dark fantasy phase styles`() {
        val p1 = com.example.ui.components.getDarkFantasyPhaseStyle(1)
        assertEquals("I", p1.romanNumeral)
        assertEquals("BLOOD MONARCH", p1.phaseName)
        assertEquals("🩸", p1.sigilIcon)

        val p2 = com.example.ui.components.getDarkFantasyPhaseStyle(2)
        assertEquals("II", p2.romanNumeral)
        assertEquals("DEMON TITAN", p2.phaseName)
        assertEquals("🔥", p2.sigilIcon)

        val p3 = com.example.ui.components.getDarkFantasyPhaseStyle(3)
        assertEquals("III", p3.romanNumeral)
        assertEquals("IMMORTAL SHADOW", p3.phaseName)
        assertEquals("🌑", p3.sigilIcon)

        val p4 = com.example.ui.components.getDarkFantasyPhaseStyle(4)
        assertEquals("IV", p4.romanNumeral)
        assertEquals("FINAL DAWN", p4.phaseName)
        assertEquals("☀️", p4.sigilIcon)
    }

    @Test
    fun `test BossAI initialization and idle state`() {
        val ai = com.example.ai.BossAI(
            initialDifficulty = com.example.ai.BossDifficulty.NORMAL,
            initialPhase = 1
        )
        assertTrue(ai.currentState is com.example.ai.BossBehaviorState.Idle)
        assertEquals(1, ai.currentPhase)
        assertEquals(1.0f, ai.staggerGauge, 0.001f)
    }

    @Test
    fun `test BossAI difficulty scaling properties`() {
        val easy = com.example.ai.BossDifficulty.EASY
        val hard = com.example.ai.BossDifficulty.HARD
        val nightmare = com.example.ai.BossDifficulty.NIGHTMARE

        assertTrue("Easy has lower damage multiplier than Hard", easy.damageMultiplier < hard.damageMultiplier)
        assertTrue("Nightmare has highest damage multiplier", nightmare.damageMultiplier > hard.damageMultiplier)
        assertTrue("Easy has longer telegraph reaction window", easy.telegraphSpeedMultiplier > hard.telegraphSpeedMultiplier)
        assertTrue("Easy stagger window is longer than Nightmare", easy.staggerDurationSeconds > nightmare.staggerDurationSeconds)
    }

    @Test
    fun `test BossAI transitions to attack from idle`() {
        val ai = com.example.ai.BossAI(
            initialDifficulty = com.example.ai.BossDifficulty.NORMAL,
            initialPhase = 1
        )
        // Force idle timer expiration
        val result = ai.update(deltaTimeSeconds = 10.0f)
        assertTrue(result.stateChanged)
        assertTrue(ai.currentState is com.example.ai.BossBehaviorState.Attack)
        val attackState = ai.currentState as com.example.ai.BossBehaviorState.Attack
        assertEquals(com.example.ai.AttackStep.TELEGRAPH, attackState.step)
    }

    @Test
    fun `test BossAI stagger trigger on posture depletion`() {
        val ai = com.example.ai.BossAI(
            initialDifficulty = com.example.ai.BossDifficulty.NORMAL,
            initialPhase = 1
        )
        val reaction = ai.onPlayerDamageReceived(damage = 500, staggerDamage = 1.5f)
        assertTrue(reaction.triggeredStagger)
        assertTrue(ai.currentState is com.example.ai.BossBehaviorState.Stagger)
    }

    @Test
    fun `test BossAI phase transition behavior`() {
        val ai = com.example.ai.BossAI(
            initialDifficulty = com.example.ai.BossDifficulty.NORMAL,
            initialPhase = 1
        )
        ai.triggerPhaseTransition(2)
        assertTrue(ai.currentState is com.example.ai.BossBehaviorState.PhaseTransition)
        val transState = ai.currentState as com.example.ai.BossBehaviorState.PhaseTransition
        assertEquals(1, transState.fromPhase)
        assertEquals(2, transState.toPhase)
    }

    @Test
    fun `test perfect parry execution and state updates`() {
        val vm = com.example.state.GameViewModel()
        val initialBreathing = vm.breathingGauge
        val initialStamina = vm.currentStamina

        assertEquals(0L, vm.perfectParryTrigger)

        vm.performParry()

        assertTrue("Perfect parry trigger timestamp must be set", vm.perfectParryTrigger > 0L)
        assertTrue("Screen shake must trigger for impact feedback", vm.screenShakeAmount > 0f)
        assertTrue("Breathing gauge must be boosted by deflection", vm.breathingGauge >= initialBreathing)
        assertTrue("Stamina must be restored by parrying", vm.currentStamina >= initialStamina)
    }
}


