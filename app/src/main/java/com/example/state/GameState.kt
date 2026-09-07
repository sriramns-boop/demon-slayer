package com.example.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.*
import com.example.data.GameRepository
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

enum class GameScreen {
    SPLASH,
    MAIN_MENU,
    GAMEPLAY_HUD,
    BOSS_HUD,
    INVENTORY,
    SKILL_TREE,
    CHARACTER,
    WORLD_MAP,
    QUEST_LOG,
    DIALOGUE,
    PAUSE_MENU,
    VICTORY,
    DEFEAT,
    UI_SHOWCASE
}

data class FloatingCombatText(
    val id: Long = System.currentTimeMillis() + Random.nextLong(1000),
    val text: String,
    val x: Float,
    val y: Float,
    val isCrit: Boolean = false,
    val isHeal: Boolean = false
)

data class EnemyInstance(
    val id: String,
    val name: String,
    var x: Float,
    var y: Float,
    var hp: Float,
    val maxHp: Float = 600f,
    var isHit: Boolean = false
)

class GameViewModel : ViewModel() {

    var currentScreen by mutableStateOf(GameScreen.SPLASH)
    var previousScreen by mutableStateOf(GameScreen.MAIN_MENU)

    // Player stats
    var playerName by mutableStateOf("KAI")
    var playerLevel by mutableIntStateOf(25)
    var maxHp by mutableFloatStateOf(1250f)
    var currentHp by mutableFloatStateOf(1250f)
    var maxStamina by mutableFloatStateOf(800f)
    var currentStamina by mutableFloatStateOf(800f)
    var xpProgress by mutableFloatStateOf(0.65f)
    var coins by mutableIntStateOf(12450)

    // Position in combat arena (normalized -1f..1f)
    var playerPosX by mutableFloatStateOf(0f)
    var playerPosY by mutableFloatStateOf(0f)
    var playerDirectionAngle by mutableFloatStateOf(0f)
    var isMoving by mutableStateOf(false)
    var isSprinting by mutableStateOf(false)
    var autoRun by mutableStateOf(false)

    // Breathing System
    var currentBreathingStyle by mutableStateOf(BreathingStyle.TIDE)
    var breathingGauge by mutableFloatStateOf(0.85f) // 0..1f
    var isBreathingFull by mutableStateOf(false)
    var isConcentrationActive by mutableStateOf(false)

    // Combat State
    var comboCount by mutableIntStateOf(0)
    var isAttacking by mutableStateOf(false)
    var isBlocking by mutableStateOf(false)
    var isDodging by mutableStateOf(false)
    var screenShakeAmount by mutableFloatStateOf(0f)
    var slashEffectTrigger by mutableIntStateOf(0)
    var activeSkillEffect by mutableStateOf<String?>(null)

    // Perfect Parry & Cinematic Deflection State
    var perfectParryTrigger by mutableLongStateOf(0L)
    var perfectParryImpactX by mutableFloatStateOf(0.5f)
    var perfectParryImpactY by mutableFloatStateOf(0.52f)
    var lastBlockPressTime by mutableLongStateOf(0L)
    var isHitStopActive by mutableStateOf(false)

    // Skill cooldown timers (0f = ready)
    var skill1Cooldown by mutableFloatStateOf(0f)
    var skill2Cooldown by mutableFloatStateOf(0f)
    var skill3Cooldown by mutableFloatStateOf(0f)

    // Boss State & BossAI Finite State Machine
    val bossAI = BossAI(initialDifficulty = BossDifficulty.NORMAL, initialPhase = 1)
    var bossState by mutableStateOf(BossState())
    var bossPhaseAnnounce by mutableStateOf<String?>(null)

    // Floating text items
    val floatingTexts = mutableStateListOf<FloatingCombatText>()

    // Combat arena enemies
    val enemies = mutableStateListOf(
        EnemyInstance("e1", "Night Crawler", 0.4f, -0.3f, 550f),
        EnemyInstance("e2", "Blood Imp", -0.5f, 0.2f, 420f),
        EnemyInstance("e3", "Forest Stalker", 0.2f, 0.6f, 600f)
    )

    // Quest & Inventory state
    var quests = mutableStateListOf<Quest>().apply { addAll(GameRepository.defaultQuests) }
    var trackedQuest by mutableStateOf(quests.firstOrNull { it.isTracked } ?: quests.first())
    var isQuestTrackerExpanded by mutableStateOf(true)

    var equipment = mutableStateListOf<EquipmentItem>().apply { addAll(GameRepository.defaultEquipment) }
    var selectedItem by mutableStateOf(equipment.first())
    var inventoryTab by mutableStateOf(ItemCategory.WEAPONS)

    var skills = mutableStateListOf<SkillNode>().apply { addAll(GameRepository.defaultSkills) }
    var selectedSkillNode by mutableStateOf(skills.first())

    var worldRegions = mutableStateListOf<WorldRegion>().apply { addAll(GameRepository.defaultRegions) }
    var selectedRegion by mutableStateOf(worldRegions.first())

    // Dialogue State
    var dialogueIndex by mutableIntStateOf(0)
    var dialogueSpeed by mutableStateOf("Normal")

    // UI Customization
    var joystickOnLeft by mutableStateOf(true)
    var joystickScale by mutableFloatStateOf(1.0f)
    var joystickOpacity by mutableFloatStateOf(0.85f)
    var graphicsQuality by mutableStateOf("High")
    var soundEnabled by mutableStateOf(true)

    init {
        startNaturalRegenLoop()
    }

    private fun startNaturalRegenLoop() {
        viewModelScope.launch {
            while (true) {
                delay(300)
                // Stamina regeneration
                if (!isSprinting && currentStamina < maxStamina) {
                    currentStamina = (currentStamina + 15f).coerceAtMost(maxStamina)
                }
                // Breathing gauge slow auto generation
                if (breathingGauge < 1f) {
                    breathingGauge = (breathingGauge + 0.01f).coerceAtMost(1f)
                    isBreathingFull = breathingGauge >= 0.999f
                }
                // Skill cooldowns decay
                if (skill1Cooldown > 0f) skill1Cooldown = (skill1Cooldown - 0.3f).coerceAtLeast(0f)
                if (skill2Cooldown > 0f) skill2Cooldown = (skill2Cooldown - 0.3f).coerceAtLeast(0f)
                if (skill3Cooldown > 0f) skill3Cooldown = (skill3Cooldown - 0.3f).coerceAtLeast(0f)

                // Decrement screen shake
                if (screenShakeAmount > 0f) {
                    screenShakeAmount = (screenShakeAmount - 2f).coerceAtLeast(0f)
                }

                // Periodic BossAI Finite State Machine update
                if (currentScreen == GameScreen.BOSS_HUD && !bossState.isDefeated) {
                    val aiResult = bossAI.update(0.3f, playerPosX, playerPosY)
                    if (aiResult.damageDealtToPlayer > 0) {
                        val now = System.currentTimeMillis()
                        val parryWindowActive = (now - lastBlockPressTime) <= 500L
                        if (isBlocking && parryWindowActive && !aiResult.isUnblockableAttack) {
                            // Automatically execute Perfect Parry on precise timing deflection!
                            triggerPerfectParry()
                        } else {
                            val finalDmg = if (isBlocking) (aiResult.damageDealtToPlayer * 0.35f).toInt() else aiResult.damageDealtToPlayer
                            currentHp = (currentHp - finalDmg).coerceAtLeast(0f)
                            screenShakeAmount = if (aiResult.isUnblockableAttack) 18f else 10f
                            addFloatingText("-$finalDmg HP", playerPosX, playerPosY, isCrit = aiResult.isUnblockableAttack)
                        }
                    }
                    if (aiResult.shockwaveTriggered) {
                        screenShakeAmount = 24f
                    }
                    // Keep bossState synchronized with bossAI
                    bossState = bossState.copy(
                        hpPercentage = bossAI.bossHpPct,
                        staggerPercentage = bossAI.staggerGauge,
                        currentPhase = bossAI.currentPhase,
                        isEnraged = bossAI.isEnraged,
                        isDefeated = bossAI.currentState is BossBehaviorState.Defeated
                    )
                }
            }
        }
    }

    fun navigateTo(screen: GameScreen) {
        if (currentScreen != GameScreen.PAUSE_MENU) {
            previousScreen = currentScreen
        }
        currentScreen = screen
    }

    fun togglePause() {
        if (currentScreen == GameScreen.PAUSE_MENU) {
            currentScreen = previousScreen
        } else {
            previousScreen = currentScreen
            currentScreen = GameScreen.PAUSE_MENU
        }
    }

    fun onJoystickMove(deltaX: Float, deltaY: Float) {
        if (deltaX == 0f && deltaY == 0f && !autoRun) {
            isMoving = false
            return
        }
        isMoving = true
        val speedMultiplier = if (isSprinting && currentStamina > 20) {
            currentStamina = (currentStamina - 2f).coerceAtLeast(0f)
            0.045f
        } else {
            0.025f
        }

        playerPosX = (playerPosX + deltaX * speedMultiplier).coerceIn(-0.95f, 0.95f)
        playerPosY = (playerPosY + deltaY * speedMultiplier).coerceIn(-0.95f, 0.95f)
        playerDirectionAngle = Math.toDegrees(Math.atan2(deltaY.toDouble(), deltaX.toDouble())).toFloat()

        // Move quest distance dynamically
        val remainingDist = (trackedQuest.distanceMeters - 1).coerceAtLeast(10)
        trackedQuest = trackedQuest.copy(distanceMeters = remainingDist)
    }

    fun performAttack() {
        isAttacking = true
        comboCount = (comboCount % 4) + 1
        slashEffectTrigger++
        screenShakeAmount = 6f

        // Charge breathing gauge with hits
        breathingGauge = (breathingGauge + 0.08f).coerceAtMost(1f)
        isBreathingFull = breathingGauge >= 0.999f

        val baseDamage = 180 + (comboCount * 45)
        val isCrit = Random.nextFloat() < 0.35f
        val finalDamage = if (isCrit) (baseDamage * 1.8f).toInt() else baseDamage

        // Hit closest enemy or boss
        if (currentScreen == GameScreen.BOSS_HUD) {
            damageBoss(finalDamage, isCrit)
        } else {
            damageNearestEnemy(finalDamage, isCrit)
        }

        viewModelScope.launch {
            delay(280)
            isAttacking = false
        }
    }

    fun performHeavyAttack() {
        if (currentStamina < 150) {
            addFloatingText("NO STAMINA!", 0f, 0f, isCrit = true)
            return
        }
        currentStamina = (currentStamina - 150).coerceAtLeast(0f)
        isAttacking = true
        comboCount = 4
        slashEffectTrigger += 2
        screenShakeAmount = 14f

        breathingGauge = (breathingGauge + 0.15f).coerceAtMost(1f)
        isBreathingFull = breathingGauge >= 0.999f

        val damage = 620 + Random.nextInt(120)
        if (currentScreen == GameScreen.BOSS_HUD) {
            damageBoss(damage, isCrit = true)
        } else {
            damageNearestEnemy(damage, isCrit = true)
        }

        viewModelScope.launch {
            delay(400)
            isAttacking = false
        }
    }

    fun performDodge() {
        if (currentStamina < 80) return
        currentStamina = (currentStamina - 80).coerceAtLeast(0f)
        isDodging = true
        val forwardRad = Math.toRadians(playerDirectionAngle.toDouble())
        playerPosX = (playerPosX + cos(forwardRad).toFloat() * 0.25f).coerceIn(-0.95f, 0.95f)
        playerPosY = (playerPosY + sin(forwardRad).toFloat() * 0.25f).coerceIn(-0.95f, 0.95f)

        addFloatingText("DODGE!", 0f, 0.1f)
        viewModelScope.launch {
            delay(250)
            isDodging = false
        }
    }

    fun performBlock(active: Boolean) {
        isBlocking = active
        if (active) {
            val now = System.currentTimeMillis()
            lastBlockPressTime = now
            // If the boss is currently in an attack window, trigger Perfect Parry!
            val bState = bossAI.currentState
            if (bState is BossBehaviorState.Attack && !bState.pattern.isUnblockable) {
                val isParryWindow = (bState.step == AttackStep.ACTIVE) ||
                    (bState.step == AttackStep.TELEGRAPH && bState.stepElapsed >= bState.pattern.telegraphDurationSeconds * bossAI.difficulty.telegraphSpeedMultiplier * 0.70f)
                if (isParryWindow) {
                    triggerPerfectParry()
                    return
                }
            }
            addFloatingText("GUARD", 0f, 0f)
        }
    }

    /**
     * Executes a dedicated Perfect Parry:
     * - Triggers screen-flash and kinetic impact particle effects
     * - Completely negates incoming damage
     * - Deals counter damage and breaks boss posture
     * - Restores stamina and breathing gauge
     */
    fun performParry() {
        triggerPerfectParry()
    }

    fun triggerPerfectParry() {
        val now = System.currentTimeMillis()
        perfectParryTrigger = now
        perfectParryImpactX = (0.5f + playerPosX * 0.25f).coerceIn(0.2f, 0.8f)
        perfectParryImpactY = (0.52f + playerPosY * 0.15f).coerceIn(0.25f, 0.75f)

        // Visceral feedback
        screenShakeAmount = 28f
        breathingGauge = (breathingGauge + 0.35f).coerceAtMost(1f)
        isBreathingFull = breathingGauge >= 0.999f
        currentStamina = (currentStamina + 40f).coerceAtMost(maxStamina)

        // Damage boss posture & deliver counter strike
        damageBoss(850, isCrit = true)
        bossAI.onPlayerDamageReceived(damage = 850, staggerDamage = 0.45f)

        // Brief cinematic hit-stop
        isHitStopActive = true
        viewModelScope.launch {
            delay(70)
            isHitStopActive = false
        }

        addFloatingText("⚡ PERFECT DEFLECT!", 0f, -0.2f, isCrit = true)
    }

    fun performSkill(skillIndex: Int) {
        when (skillIndex) {
            1 -> {
                if (skill1Cooldown > 0f) return
                skill1Cooldown = 4.0f
                activeSkillEffect = "${currentBreathingStyle.title}: Form 1 - Flowing Edge"
                screenShakeAmount = 10f
                breathingGauge = (breathingGauge + 0.1f).coerceAtMost(1f)
                applyAoeDamage(480, "SKILL 1 CRIT")
            }
            2 -> {
                if (skill2Cooldown > 0f) return
                skill2Cooldown = 7.5f
                activeSkillEffect = "${currentBreathingStyle.title}: Form 2 - Whirlpool Arc"
                screenShakeAmount = 16f
                applyAoeDamage(720, "ELEMENTAL CRIT")
            }
            3 -> {
                if (skill3Cooldown > 0f) return
                skill3Cooldown = 12.0f
                activeSkillEffect = "${currentBreathingStyle.title}: Advanced Form - Thunder Dragon"
                screenShakeAmount = 22f
                applyAoeDamage(1100, "STAGGER BREAK!")
            }
        }
        viewModelScope.launch {
            delay(1200)
            activeSkillEffect = null
        }
    }

    fun triggerUltimate() {
        if (breathingGauge < 0.95f) {
            addFloatingText("BREATHING NOT FULL!", 0f, 0f, isCrit = true)
            return
        }
        breathingGauge = 0f
        isBreathingFull = false
        screenShakeAmount = 30f
        activeSkillEffect = "✨ ${currentBreathingStyle.ultimateName} ✨"

        viewModelScope.launch {
            for (i in 1..4) {
                delay(180)
                screenShakeAmount = 18f
                applyAoeDamage(850 + i * 350, "DAWN DESTROYER!")
            }
            delay(1000)
            activeSkillEffect = null
        }
    }

    private fun damageNearestEnemy(amount: Int, isCrit: Boolean) {
        val target = enemies.firstOrNull { it.hp > 0 }
        if (target != null) {
            target.hp = (target.hp - amount).coerceAtLeast(0f)
            target.isHit = true
            addFloatingText("-$amount", target.x, target.y - 0.1f, isCrit)
            if (target.hp <= 0) {
                addFloatingText("+450 XP", target.x, target.y, isHeal = true)
                coins += 180
                xpProgress = (xpProgress + 0.08f).let { if (it >= 1f) { playerLevel++; 0.1f } else it }
            }
            viewModelScope.launch {
                delay(200)
                target.isHit = false
            }
        }
    }

    private fun damageBoss(amount: Int, isCrit: Boolean) {
        if (bossState.isDefeated) return
        addFloatingText("-$amount", 0f, -0.4f, isCrit)

        val reaction = bossAI.onPlayerDamageReceived(amount, staggerDamage = 0.08f)
        bossState = bossState.copy(
            hpPercentage = reaction.newHpPercentage,
            staggerPercentage = reaction.newStaggerPercentage,
            isEnraged = bossAI.isEnraged,
            isDefeated = bossAI.currentState is BossBehaviorState.Defeated
        )

        if (reaction.triggeredStagger) {
            addFloatingText("⚡ POSTURE BROKEN! (200% DMG)", 0f, -0.3f, isCrit = true)
            screenShakeAmount = 14f
        }

        if (reaction.triggeredPhaseTransition && reaction.targetPhase != null) {
            val phaseNames = listOf("Blood Monarch", "Demon Titan", "Immortal Shadow", "Final Dawn")
            val pName = phaseNames.getOrElse(reaction.targetPhase - 1) { "Awakened Form" }
            bossPhaseAnnounce = "PHASE ${reaction.targetPhase}: $pName"
            screenShakeAmount = 25f
            viewModelScope.launch {
                delay(2200)
                bossPhaseAnnounce = null
            }
        }

        if (bossAI.currentState is BossBehaviorState.Defeated) {
            viewModelScope.launch {
                delay(1200)
                navigateTo(GameScreen.VICTORY)
            }
        }
    }

    fun setBossDifficulty(difficulty: BossDifficulty) {
        bossAI.setDifficulty(difficulty)
        addFloatingText("AI DIFFICULTY: ${difficulty.label}", 0f, -0.2f)
    }

    fun forceBossAttack() {
        bossAI.triggerAttack()
    }

    fun forceBossStagger() {
        bossAI.forceStagger()
        addFloatingText("⚡ FORCED STAGGER!", 0f, -0.25f, isCrit = true)
        bossState = bossState.copy(staggerPercentage = 0f)
    }

    fun transitionBossToPhase(targetPhase: Int) {
        val clamped = targetPhase.coerceIn(1, 4)
        val phaseNames = listOf("Blood Monarch", "Demon Titan", "Immortal Shadow", "Final Dawn")
        val phaseName = phaseNames[clamped - 1]
        bossPhaseAnnounce = "PHASE $clamped: $phaseName"
        screenShakeAmount = 20f
        bossAI.setPhase(clamped)
        bossState = bossState.copy(
            currentPhase = clamped,
            title = phaseName,
            hpPercentage = 1.0f,
            staggerPercentage = 0.65f,
            isEnraged = clamped >= 3,
            isDefeated = false
        )
        viewModelScope.launch {
            delay(2200)
            bossPhaseAnnounce = null
        }
    }

    fun advanceBossPhase() {
        val next = if (bossState.currentPhase < bossState.maxPhases) bossState.currentPhase + 1 else 1
        transitionBossToPhase(next)
    }

    fun toggleBossEnrage() {
        bossState = bossState.copy(isEnraged = !bossState.isEnraged)
    }

    fun resetBoss() {
        bossState = BossState(
            name = "CRIMSON KING",
            title = "Blood Monarch",
            currentPhase = 1,
            maxPhases = 4,
            hpPercentage = 1.0f,
            staggerPercentage = 0.8f,
            isEnraged = false,
            isDefeated = false
        )
        bossPhaseAnnounce = "BOSS ENCOUNTER INITIATED"
        viewModelScope.launch {
            delay(1800)
            bossPhaseAnnounce = null
        }
    }

    private fun applyAoeDamage(amount: Int, label: String) {
        addFloatingText(label, 0f, 0f, isCrit = true)
        if (currentScreen == GameScreen.BOSS_HUD) {
            damageBoss(amount, isCrit = true)
        } else {
            enemies.forEach { enemy ->
                if (enemy.hp > 0) {
                    enemy.hp = (enemy.hp - amount).coerceAtLeast(0f)
                    addFloatingText("-$amount", enemy.x, enemy.y, isCrit = true)
                }
            }
        }
    }

    fun addFloatingText(text: String, x: Float, y: Float, isCrit: Boolean = false, isHeal: Boolean = false) {
        val item = FloatingCombatText(text = text, x = x, y = y, isCrit = isCrit, isHeal = isHeal)
        floatingTexts.add(item)
        viewModelScope.launch {
            delay(900)
            floatingTexts.remove(item)
        }
    }

    fun equipItem(item: EquipmentItem) {
        val updated = equipment.map {
            if (it.slot == item.slot) {
                it.copy(isEquipped = it.id == item.id)
            } else it
        }
        equipment.clear()
        equipment.addAll(updated)
        selectedItem = item.copy(isEquipped = true)
        addFloatingText("EQUIPPED: ${item.name}", 0f, -0.2f, isHeal = true)
    }

    fun upgradeItem(item: EquipmentItem) {
        if (coins < 500) {
            addFloatingText("NEED 500 COINS!", 0f, 0f, isCrit = true)
            return
        }
        coins -= 500
        val updated = equipment.map {
            if (it.id == item.id) {
                it.copy(
                    upgradeLevel = it.upgradeLevel + 1,
                    attack = it.attack + 25,
                    defense = it.defense + 15
                )
            } else it
        }
        equipment.clear()
        equipment.addAll(updated)
        selectedItem = updated.first { it.id == item.id }
        addFloatingText("+1 UPGRADED!", 0f, -0.2f, isHeal = true)
    }

    fun unlockOrUpgradeSkill(skill: SkillNode) {
        if (coins < skill.upgradeCost) {
            addFloatingText("NOT ENOUGH COINS!", 0f, 0f, isCrit = true)
            return
        }
        coins -= skill.upgradeCost
        val updated = skills.map {
            if (it.id == skill.id) {
                it.copy(
                    isUnlocked = true,
                    level = (it.level + 1).coerceAtMost(it.maxLevel),
                    damage = it.damage + 60
                )
            } else it
        }
        skills.clear()
        skills.addAll(updated)
        selectedSkillNode = updated.first { it.id == skill.id }
        addFloatingText("SKILL ENHANCED!", 0f, -0.2f, isHeal = true)
    }

    fun fastTravelTo(region: WorldRegion) {
        selectedRegion = region
        addFloatingText("TRAVELING TO ${region.name}...", 0f, 0f, isHeal = true)
        viewModelScope.launch {
            delay(600)
            navigateTo(GameScreen.GAMEPLAY_HUD)
        }
    }

    fun restartMission() {
        currentHp = maxHp
        currentStamina = maxStamina
        breathingGauge = 0.5f
        bossState = BossState()
        enemies.forEach { it.hp = it.maxHp }
        navigateTo(GameScreen.GAMEPLAY_HUD)
    }
}
