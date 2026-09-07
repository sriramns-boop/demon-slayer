package com.example.model

import androidx.compose.ui.graphics.Color

enum class ItemRarity(val label: String, val color: Color, val borderGlow: Color) {
    COMMON("Common", Color(0xFF9E9E9E), Color(0xFF616161)),
    UNCOMMON("Uncommon", Color(0xFF4CAF50), Color(0xFF2E7D32)),
    RARE("Rare", Color(0xFF2196F3), Color(0xFF1565C0)),
    EPIC("Epic", Color(0xFFAB47BC), Color(0xFF7B1FA2)),
    LEGENDARY("Legendary", Color(0xFFFFB300), Color(0xFFFF8F00)),
    MYTHIC("Mythic", Color(0xFFFF1744), Color(0xFFD50000))
}

enum class ItemCategory(val label: String) {
    WEAPONS("Weapons"),
    ARMOR("Armor"),
    ITEMS("Items"),
    MATERIALS("Materials"),
    QUEST("Quest"),
    RELICS("Relics")
}

enum class EquipmentSlot(val label: String) {
    HEAD("Headgear"),
    BODY("Haori / Body"),
    SWORD("Nichirin Blade"),
    ACCESSORY_1("Amulet"),
    ACCESSORY_2("Relic Charm")
}

data class EquipmentItem(
    val id: String,
    val name: String,
    val slot: EquipmentSlot,
    val category: ItemCategory,
    val rarity: ItemRarity,
    val attack: Int = 0,
    val defense: Int = 0,
    val speed: Int = 0,
    val critRate: Float = 0f,
    val upgradeLevel: Int = 0,
    val passiveDesc: String = "",
    val description: String = "",
    val sellPrice: Int = 100,
    val isEquipped: Boolean = false
)

enum class BreathingStyle(
    val title: String,
    val element: String,
    val primaryColor: Color,
    val accentColor: Color,
    val iconSymbol: String,
    val ultimateName: String
) {
    TIDE("Tide Breathing", "Water", Color(0xFF00D2FF), Color(0xFF0066FF), "🌊", "Ocean Breaker"),
    INFERNO("Inferno Breathing", "Fire", Color(0xFFFF4500), Color(0xFFFF9E00), "🔥", "Solar Blaze"),
    STORM("Storm Breathing", "Thunder", Color(0xFFFFEE00), Color(0xFFFF9900), "⚡", "Heaven Strike"),
    GALE("Gale Breathing", "Wind", Color(0xFF00F5A0), Color(0xFF00A86B), "🌪️", "Sky Reaper"),
    LUNAR("Lunar Breathing", "Moon", Color(0xFFB5179E), Color(0xFF7209B7), "🌙", "Midnight Execution"),
    BLOSSOM("Blossom Breathing", "Flower", Color(0xFFFF70A6), Color(0xFFFF4081), "🌸", "Eternal Garden"),
    DRAGON("Dragon Breathing", "Dragon", Color(0xFF48CAE4), Color(0xFF90E0EF), "🐉", "Sky Dragon Rush"),
    TITAN("Titan Breathing", "Earth", Color(0xFFD4A373), Color(0xFFBC6C25), "⛰️", "Colossus Strike"),
    SOLAR("Solar Breathing", "Sun", Color(0xFFFFD166), Color(0xFFFF073A), "☀️", "DAWNBRINGER")
}

data class SkillNode(
    val id: String,
    val name: String,
    val style: BreathingStyle,
    val formLabel: String,
    val level: Int,
    val maxLevel: Int = 5,
    val damage: Int,
    val cooldownSeconds: Float,
    val energyCost: Int,
    val upgradeCost: Int,
    val isUnlocked: Boolean,
    val description: String
)

data class Quest(
    val id: String,
    val title: String,
    val category: QuestCategory,
    val chapter: String,
    val objective: String,
    val location: String,
    val distanceMeters: Int,
    val xpReward: Int,
    val coinsReward: Int,
    val rewardItem: String,
    val isCompleted: Boolean = false,
    val isTracked: Boolean = true
)

enum class QuestCategory(val label: String) {
    MAIN("Main"),
    SIDE("Side"),
    COMPANION("Companion"),
    BOUNTY("Bounty"),
    COMPLETED("Completed")
}

data class WorldRegion(
    val id: String,
    val name: String,
    val subtitle: String,
    val dangerLevel: String,
    val normalizedX: Float, // 0f..1f
    val normalizedY: Float, // 0f..1f
    val isUnlocked: Boolean,
    val hasQuest: Boolean = false,
    val hasBoss: Boolean = false,
    val hasFastTravel: Boolean = true
)

data class BossState(
    val name: String = "CRIMSON DEMON",
    val title: String = "Harbinger of the Blood Moon",
    val currentPhase: Int = 1,
    val maxPhases: Int = 4,
    val hpPercentage: Float = 1.0f,
    val staggerPercentage: Float = 0.35f,
    val isEnraged: Boolean = false,
    val isDefeated: Boolean = false
)

data class DialogueLine(
    val speaker: String,
    val title: String,
    val text: String,
    val portraitAvatar: String,
    val choices: List<String> = emptyList()
)

data class CampaignMission(
    val number: Int,
    val title: String,
    val chapter: String,
    val objective: String,
    val mapLocation: String,
    val enemies: String,
    val boss: String,
    val xp: Int,
    val coins: Int,
    val reward: String
)
