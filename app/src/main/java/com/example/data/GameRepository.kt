package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.model.*

object GameRepository {

    val defaultRegions = listOf(
        WorldRegion("ashen", "ASHEN VILLAGE", "Birthplace of the Blade", "Lv. 1-5", 0.20f, 0.70f, isUnlocked = true, hasQuest = true, hasFastTravel = true),
        WorldRegion("hq", "HUNTER HEADQUARTERS", "Citadel of the Breath Masters", "Lv. 5-10", 0.35f, 0.60f, isUnlocked = true, hasQuest = false, hasFastTravel = true),
        WorldRegion("forest", "WHISPERING FOREST", "Domain of the Silk Demons", "Lv. 10-16", 0.25f, 0.45f, isUnlocked = true, hasQuest = true, hasBoss = true, hasFastTravel = true),
        WorldRegion("city", "NIGHTFALL CITY", "The Blood Moon Metropolis", "Lv. 16-22", 0.45f, 0.50f, isUnlocked = true, hasQuest = false, hasFastTravel = true),
        WorldRegion("iron", "IRON MOUNTAIN", "The Eternal Master Forge", "Lv. 22-29", 0.60f, 0.38f, isUnlocked = true, hasBoss = true, hasFastTravel = true),
        WorldRegion("temple", "SKY TEMPLE", "Sanctuary of the Wind & Sun", "Lv. 29-36", 0.75f, 0.25f, isUnlocked = true, hasQuest = true, hasFastTravel = true),
        WorldRegion("waste", "CRIMSON WASTELAND", "Desolation of Blood Rivers", "Lv. 36-43", 0.70f, 0.65f, isUnlocked = true, hasBoss = true, hasFastTravel = true),
        WorldRegion("battle", "NIGHTFALL BATTLEFIELD", "Trench of the 8 Demon Generals", "Lv. 43-50", 0.50f, 0.80f, isUnlocked = false, hasFastTravel = false),
        WorldRegion("fortress", "ENDLESS FORTRESS", "Labyrinth of Shifting Space", "Lv. 50-57", 0.85f, 0.78f, isUnlocked = false, hasBoss = true, hasFastTravel = false),
        WorldRegion("throne", "CRIMSON KING'S THRONE", "Sanctum of Eternal Night", "Lv. 60+", 0.90f, 0.40f, isUnlocked = false, hasBoss = true, hasFastTravel = false)
    )

    val defaultEquipment: List<EquipmentItem> = listOf(
        EquipmentItem(
            id = "w1",
            name = "NIGHTFALL KATANA",
            slot = EquipmentSlot.SWORD,
            category = ItemCategory.WEAPONS,
            rarity = ItemRarity.EPIC,
            attack = 420,
            defense = 0,
            speed = 8,
            critRate = 15f,
            upgradeLevel = 7,
            passiveDesc = "Night's Embrace: +25% Critical Damage under moonlight",
            description = "Forged from moonlit scarlet iron ore in the high peaks of Iron Mountain.",
            sellPrice = 1800,
            isEquipped = true
        ),
        EquipmentItem(
            id = "w2",
            name = "DAWNBLADE OF THE SUN",
            slot = EquipmentSlot.SWORD,
            category = ItemCategory.WEAPONS,
            rarity = ItemRarity.MYTHIC,
            attack = 890,
            defense = 50,
            speed = 18,
            critRate = 32f,
            upgradeLevel = 10,
            passiveDesc = "Solar Flare: Slashes ignite demons with true solar radiant fire",
            description = "The ancient heirloom of the first demon hunter, radiant as dawn.",
            sellPrice = 12000,
            isEquipped = false
        ),
        EquipmentItem(
            id = "w3",
            name = "TIDAL FLOW NICHIRIN",
            slot = EquipmentSlot.SWORD,
            category = ItemCategory.WEAPONS,
            rarity = ItemRarity.RARE,
            attack = 290,
            defense = 15,
            speed = 12,
            critRate = 10f,
            upgradeLevel = 4,
            passiveDesc = "Fluid Riposte: +15% damage to combo chain finshers",
            description = "A deep cerulean blade reflecting the current of high mountain waters.",
            sellPrice = 850,
            isEquipped = false
        ),
        EquipmentItem(
            id = "w4",
            name = "STORM FANG TACHI",
            slot = EquipmentSlot.SWORD,
            category = ItemCategory.WEAPONS,
            rarity = ItemRarity.LEGENDARY,
            attack = 640,
            defense = 0,
            speed = 25,
            critRate = 22f,
            upgradeLevel = 6,
            passiveDesc = "Thunderclap: First dash attack deals +80% shock damage",
            description = "Crackle of yellow lightning dances eternally along its serrated edge.",
            sellPrice = 4500,
            isEquipped = false
        ),
        EquipmentItem(
            id = "w5",
            name = "INFERNO REAVER",
            slot = EquipmentSlot.SWORD,
            category = ItemCategory.WEAPONS,
            rarity = ItemRarity.EPIC,
            attack = 510,
            defense = 10,
            speed = 5,
            critRate = 18f,
            upgradeLevel = 5,
            passiveDesc = "Flame Dance: Heavy attacks inflict stacking burn over 5s",
            description = "Tempered in volcanic magma beneath the demon-infested crags.",
            sellPrice = 2400,
            isEquipped = false
        ),
        // Body Armor
        EquipmentItem(
            id = "a1",
            name = "SHADOW HUNTER HAORI",
            slot = EquipmentSlot.BODY,
            category = ItemCategory.ARMOR,
            rarity = ItemRarity.EPIC,
            attack = 45,
            defense = 240,
            speed = 12,
            critRate = 5f,
            upgradeLevel = 6,
            passiveDesc = "Silent Stride: Reduces enemy detection radius by 30%",
            description = "Woven with durable silk and demon-warding threads of traditional pattern.",
            sellPrice = 1600,
            isEquipped = true
        ),
        EquipmentItem(
            id = "a2",
            name = "DAWN HUNTER ROBES",
            slot = EquipmentSlot.BODY,
            category = ItemCategory.ARMOR,
            rarity = ItemRarity.MYTHIC,
            attack = 120,
            defense = 520,
            speed = 22,
            critRate = 12f,
            upgradeLevel = 9,
            passiveDesc = "Dawn Aura: Regenerates 2% HP and 5% Stamina every 3 seconds",
            description = "Golden celestial raiment worn only by masters of Solar Breathing.",
            sellPrice = 9500,
            isEquipped = false
        ),
        // Head
        EquipmentItem(
            id = "h1",
            name = "WARDING FOX MASK",
            slot = EquipmentSlot.HEAD,
            category = ItemCategory.ARMOR,
            rarity = ItemRarity.RARE,
            attack = 20,
            defense = 85,
            speed = 6,
            critRate = 8f,
            upgradeLevel = 5,
            passiveDesc = "Spiritual Ward: +20% resistance against Blood Demon Arts",
            description = "Hand-carved cedar mask blessed by the mountain mentor Sakonji.",
            sellPrice = 900,
            isEquipped = true
        ),
        // Accessories
        EquipmentItem(
            id = "acc1",
            name = "HANAJIRUSHI SUN AMULET",
            slot = EquipmentSlot.ACCESSORY_1,
            category = ItemCategory.ITEMS,
            rarity = ItemRarity.EPIC,
            attack = 55,
            defense = 40,
            speed = 10,
            critRate = 7.5f,
            upgradeLevel = 4,
            passiveDesc = "Inner Flame: Increases Aether Breathing gauge fill rate by 20%",
            description = "A pendant bearing the blazing crest of the morning sun.",
            sellPrice = 2100,
            isEquipped = true
        ),
        EquipmentItem(
            id = "acc2",
            name = "BLOOD CRYSTAL MAGATAMA",
            slot = EquipmentSlot.ACCESSORY_2,
            category = ItemCategory.ITEMS,
            rarity = ItemRarity.RARE,
            attack = 30,
            defense = 25,
            speed = 4,
            critRate = 4f,
            upgradeLevel = 3,
            passiveDesc = "Life Leech: Heals 15 HP on critical strikes",
            description = "Curved gemstone that pulses faintly in sync with nearby demonic power.",
            sellPrice = 1100,
            isEquipped = true
        ),
        // Materials & Consumables
        EquipmentItem(
            id = "mat1",
            name = "DEMON FANG ×4",
            slot = EquipmentSlot.ACCESSORY_2,
            category = ItemCategory.MATERIALS,
            rarity = ItemRarity.UNCOMMON,
            description = "Hardened fang harvested from higher-order Dreadborn. Used in forge upgrades.",
            sellPrice = 300,
            isEquipped = false
        ),
        EquipmentItem(
            id = "mat2",
            name = "SPIRIT ORE ×10",
            slot = EquipmentSlot.ACCESSORY_2,
            category = ItemCategory.MATERIALS,
            rarity = ItemRarity.RARE,
            description = "Mystical metal that absorbs sunlight. Fundamental material for Nichirin blades.",
            sellPrice = 800,
            isEquipped = false
        ),
        EquipmentItem(
            id = "pot1",
            name = "HERBAL VITALITY ELIXIR",
            slot = EquipmentSlot.ACCESSORY_1,
            category = ItemCategory.ITEMS,
            rarity = ItemRarity.COMMON,
            description = "Instantly restores 650 HP and clears poison affliction.",
            sellPrice = 150,
            isEquipped = false
        )
    )

    val defaultSkills: List<SkillNode> = listOf(
        // Tide
        SkillNode("t1", "Flowing Edge", BreathingStyle.TIDE, "First Form", 3, 5, 240, 4.0f, 25, 400, true, "Delivers two sweeping horizontal water slashes that carry enemies forward."),
        SkillNode("t2", "Whirlpool Slash", BreathingStyle.TIDE, "Third Form", 2, 5, 390, 7.5f, 40, 650, true, "Spins rapidly, generating a swirling vortex of water that pulls in foes."),
        SkillNode("t3", "Ocean Breaker", BreathingStyle.TIDE, "Fifth Form (Ultimate)", 1, 5, 820, 15.0f, 75, 1200, true, "Leaps into the air and crashes downward in an immense tsunami strike."),
        // Inferno
        SkillNode("f1", "Ember Slash", BreathingStyle.INFERNO, "First Form", 2, 5, 290, 4.5f, 30, 450, true, "A fiery upward thrust that ignites the target on contact."),
        SkillNode("f2", "Flame Arc Wheel", BreathingStyle.INFERNO, "Second Form", 1, 5, 460, 8.0f, 45, 750, true, "Performs a 360-degree somersault slash leaving a burning ring of fire."),
        SkillNode("f3", "Solar Blaze Burst", BreathingStyle.INFERNO, "Fourth Form", 1, 5, 940, 18.0f, 85, 1500, false, "Concentrates internal body heat into a forward-sweeping conflagration."),
        // Storm
        SkillNode("s1", "Flash Step Pierce", BreathingStyle.STORM, "First Form", 4, 5, 310, 3.5f, 25, 500, true, "Dashes instantaneously through targets in a blinding lightning strike."),
        SkillNode("s2", "Thunder Chain", BreathingStyle.STORM, "Third Form", 1, 5, 520, 9.0f, 50, 800, false, "Chains lightning arcs between up to five adjacent enemies."),
        // Gale
        SkillNode("g1", "Wind Cutter", BreathingStyle.GALE, "First Form", 1, 5, 260, 4.0f, 25, 400, false, "Fires razor-sharp sonic blades of pressurized wind."),
        // Lunar
        SkillNode("m1", "Crescent Moon Slash", BreathingStyle.LUNAR, "First Form", 1, 5, 480, 8.0f, 50, 900, false, "A shadowy crescent wave that cuts through physical defenses."),
        // Blossom
        SkillNode("b1", "Bloom Flurry", BreathingStyle.BLOSSOM, "Second Form", 1, 5, 350, 6.0f, 35, 600, false, "Rapid-fire precision rapiers accompanied by scattering cherry blossom petals."),
        // Dragon
        SkillNode("d1", "Dragon Roar Fang", BreathingStyle.DRAGON, "Third Form", 1, 5, 680, 12.0f, 65, 1400, false, "Summons an ethereal dragon spirit that charges through the battlefield."),
        // Solar
        SkillNode("sol1", "DAWNBRINGER", BreathingStyle.SOLAR, "Final Form (Divine)", 1, 5, 1650, 25.0f, 100, 5000, false, "The supreme art. Channels morning sunlight to completely vaporize darkness.")
    )

    val defaultQuests: List<Quest> = listOf(
        Quest(
            id = "q1",
            title = "Find the Hidden Demon",
            category = QuestCategory.MAIN,
            chapter = "Chapter 1: Ashen Village",
            objective = "Track the demonic miasma into the Whispering Forest and eliminate the den mother.",
            location = "Whispering Forest",
            distanceMeters = 325,
            xpReward = 1200,
            coinsReward = 850,
            rewardItem = "Nightfall Katana [Epic]",
            isCompleted = false,
            isTracked = true
        ),
        Quest(
            id = "q2",
            title = "Defeat the Forest Demon",
            category = QuestCategory.MAIN,
            chapter = "Chapter 1: Ashen Village",
            objective = "Slay Gorvak the Forest King lurking in the deep ancient grove.",
            location = "Deep Grove",
            distanceMeters = 780,
            xpReward = 1800,
            coinsReward = 1200,
            rewardItem = "King's Fang Dagger",
            isCompleted = false,
            isTracked = false
        ),
        Quest(
            id = "q3",
            title = "A Sister's Herb",
            category = QuestCategory.SIDE,
            chapter = "Side Quest: Ashen Village",
            objective = "Gather 5 Moonlight Herbs from the misty cliffs for Mira's medicine.",
            location = "Moonlit Ridge",
            distanceMeters = 190,
            xpReward = 450,
            coinsReward = 300,
            rewardItem = "Herbal Vitality Elixir ×3",
            isCompleted = false,
            isTracked = false
        ),
        Quest(
            id = "q4",
            title = "The Broken Bell",
            category = QuestCategory.BOUNTY,
            chapter = "Bounty: Bell Tower",
            objective = "Hunt down the sonic demon Bell Eater haunting the old cathedral spire.",
            location = "Ruined Bell Tower",
            distanceMeters = 1140,
            xpReward = 900,
            coinsReward = 650,
            rewardItem = "Sonic Resistance Charm",
            isCompleted = false,
            isTracked = false
        ),
        Quest(
            id = "q5",
            title = "First Breath Trial",
            category = QuestCategory.COMPLETED,
            chapter = "Chapter 1: Training",
            objective = "Master the rhythm of Total Concentration Breathing under Riven's guidance.",
            location = "Training Dojo",
            distanceMeters = 0,
            xpReward = 500,
            coinsReward = 300,
            rewardItem = "Tide Breathing Form I",
            isCompleted = true,
            isTracked = false
        )
    )

    val campaignMissions: List<CampaignMission> = listOf(
        // Chapter 1
        CampaignMission(1, "The Last Ember", "Chapter 1: Ashen Village", "Investigate the ominous silence in Ashen Village.", "Ashen Village", "Night Crawlers ×6", "None", 100, 50, "Rusted Hunter Blade"),
        CampaignMission(2, "Blood in the Snow", "Chapter 1: Ashen Village", "Search for survivors amidst the burning ruins.", "Destroyed Village", "Night Crawlers, Blood Imps", "Blood Imp Captain", 150, 75, "Small Healing Potions ×3"),
        CampaignMission(3, "The Survivor", "Chapter 1: Ashen Village", "Rescue wounded companion Mira from the burning barn.", "Burning Barn", "Flame Bats ×8", "Ash Maw", 200, 100, "Mira Companion Unlocked"),
        CampaignMission(4, "The Red-Eyed Hunter", "Chapter 1: Ashen Village", "Survive the trial with wandering master Riven.", "Northern Forest", "Forest Stalkers ×12", "Riven, Master Hunter", 250, 125, "Hunter Training"),
        CampaignMission(5, "First Breath", "Chapter 1: Ashen Village", "Learn Tide Breathing technique from Riven.", "Mountain Training Grounds", "Training Constructs", "Stone Guardian", 300, 150, "Tide Breathing Lv.1"),
        CampaignMission(6, "Into the Woods", "Chapter 1: Ashen Village", "Investigate reports of missing vanguard scouts.", "Whispering Forest", "Web Crawlers ×15", "Silk Fang", 350, 175, "Silk Armor"),
        CampaignMission(7, "The Hidden Den", "Chapter 1: Ashen Village", "Infiltrate and cleanse the underground demon nest.", "Underground Den", "Cave Demons ×20", "Mother Web", 400, 200, "Web Cutter Blade"),
        CampaignMission(8, "Village Guardian", "Chapter 1: Ashen Village", "Defend Ashen Village from the blood moon siege.", "Ashen Village", "Night Crawlers ×20", "Bloodhorn", 450, 225, "Village Reputation +20"),
        CampaignMission(9, "The Hunter's Oath", "Chapter 1: Ashen Village", "Take the solemn oath of the Shadow Hunter Order.", "Hunter HQ", "Trial Warriors", "Riven", 500, 300, "Shadow Hunter Rank II"),

        // Chapter 2
        CampaignMission(10, "Trial of Speed", "Chapter 2: Hunter's Trial", "Ascend Bamboo Valley before the hourglass empties.", "Bamboo Valley", "Razor Imps", "Swiftfang", 550, 300, "Dash Skill"),
        CampaignMission(11, "Trial of Strength", "Chapter 2: Hunter's Trial", "Break through the heavy boulder barricades.", "Boulder Canyon", "Rock Beasts", "Ironback", 600, 325, "Heavy Strike"),
        CampaignMission(12, "Trial of Focus", "Chapter 2: Hunter's Trial", "Pierce illusions in the mirror chamber.", "Silent Temple", "Illusion Spirits", "Mirror Wraith", 650, 350, "Focus Mode"),
        CampaignMission(13, "Trial of Courage", "Chapter 2: Hunter's Trial", "Withstand the terrifying spectral miasma.", "Haunted Cemetery", "Grave Walkers", "Bone Priest", 700, 375, "Fear Resistance"),
        CampaignMission(14, "Trial of Fire", "Chapter 2: Hunter's Trial", "Endure the magma heat of Ember Peak.", "Ember Mountain", "Flame Imps", "Magma Claw", 750, 400, "Inferno Breathing"),
        CampaignMission(15, "Trial of the Blade", "Chapter 2: Hunter's Trial", "Master the art of the perfect blade deflection.", "Ancient Dojo", "Training Warriors", "Master Kiro", 800, 425, "Blade Parry"),
        CampaignMission(16, "Three Shadows", "Chapter 2: Hunter's Trial", "Defeat the synchronized ninja assassin squad.", "Moonlit Forest", "Shadow Assassins", "Shadow Trio", 850, 450, "Shadow Cloak"),
        CampaignMission(17, "The Broken Bell", "Chapter 2: Hunter's Trial", "Silence the maddening bell resonance.", "Bell Tower", "Winged Demons", "Bell Eater", 900, 475, "Sonic Blade"),
        CampaignMission(18, "Hunter's Graduation", "Chapter 2: Hunter's Trial", "Face the reigning arena champion.", "Hunter Arena", "Elite Hunters", "Trial Champion", 1000, 600, "Spirit Katana"),

        // Chapter 3
        CampaignMission(22, "Storm Awakening", "Chapter 3: Cursed Forest", "Awaken the lightning within your diaphragm.", "Thunder Plains", "Storm Imps", "Thunder Fang", 1200, 700, "Storm Breathing"),
        CampaignMission(26, "The Forest King", "Chapter 3: Cursed Forest", "Confront Gorvak, monarch of the cursed timber.", "Ancient Grove", "Elite Forest Demons", "Gorvak, Forest King", 1500, 900, "King's Fang"),
        CampaignMission(27, "Escape from the Forest", "Chapter 3: Cursed Forest", "Flee the conflagration before the canopy collapses.", "Collapsing Forest", "Elite Demons", "Burning Stalker", 1600, 1000, "Inferno Blade"),

        // Chapter 4
        CampaignMission(28, "Arrival at Nightfall City", "Chapter 4: Blood Moon City", "Enter the shadowed alleyways of the metropolis.", "Nightfall City", "Street Demons", "Alley Butcher", 1800, 1100, "City Map & Teleport"),
        CampaignMission(31, "The Silent Assassin", "Chapter 4: Blood Moon City", "Rooftop duel against the phantom of the night.", "City Rooftops", "Shadow Ninjas", "Vexa, Silent Blade", 2100, 1300, "Shadow Step"),
        CampaignMission(36, "Break the Blood Seal", "Chapter 4: Blood Moon City", "Shatter the crimson sigil controlling the populace.", "Underground Temple", "Blood Serpent Cult", "Blood Serpent", 2500, 1500, "Dawn Relic Fragment I"),

        // Chapter 5
        CampaignMission(41, "Dragon Breath", "Chapter 5: Iron Mountain", "Learn Dragon Breathing inside the wyrm cavern.", "Dragon Cavern", "Drake Demons", "Elder Drake", 3000, 1800, "Dragon Breathing"),
        CampaignMission(45, "The Master Forge", "Chapter 5: Iron Mountain", "Forge the legendary blade with blacksmith Eron.", "Legendary Forge", "Furnace Beasts", "None", 3500, 2200, "Legendary Dawnblade"),

        // Chapter 6
        CampaignMission(48, "Trial of Wind", "Chapter 6: Sky Temple", "Harness the gale winds atop the floating pagoda.", "Wind Arena", "Gale Monks", "Gale Master", 4000, 2500, "Gale Breathing"),
        CampaignMission(53, "The Second Relic", "Chapter 6: Sky Temple", "Defeat the temple dragon guarding the relic.", "Sky Ruins", "Temple Sentinels", "Temple Dragon", 4500, 3000, "Dawn Relic Fragment II"),

        // Chapter 7
        CampaignMission(58, "Lunar Temple", "Chapter 7: Crimson Wasteland", "Master the esoteric rhythms of Lunar Breathing.", "Lunar Shrine", "Moon Priests", "Lunar Priestess", 5500, 3500, "Lunar Breathing"),
        CampaignMission(62, "The Third Fragment", "Chapter 7: Crimson Wasteland", "Recover the last remaining fragment of the Dawn.", "Relic Altar", "Wasteland Horrors", "Relic Guardian", 6500, 4200, "Dawn Relic Fragment III"),

        // Chapter 8
        CampaignMission(67, "The Flame General", "Chapter 8: Nightfall War", "Vanquish General Ignar upon the burning ramparts.", "Hunter Capital Gate", "Demon Vanguard", "General Ignar", 8000, 5000, "Inferno Ultimate"),
        CampaignMission(70, "The Eight Demon Generals", "Chapter 8: Nightfall War", "Stand as the vanguard against Muzan's high guard.", "Battlefield Center", "8 Demon Elite Squad", "Eight Demon Generals", 9500, 6500, "General Slayer Armor"),
        CampaignMission(71, "Protect the Dawn Relic", "Chapter 8: Nightfall War", "Shield the reunited holy artifact from ruin.", "Sanctuary", "Relic Destroyers", "Relic Destroyer", 11000, 7500, "Completed Dawn Relic"),

        // Chapter 9
        CampaignMission(75, "Hall of Blades", "Chapter 9: Endless Fortress", "Duel the legendary corrupted master of the sword.", "Hall of Blades", "Sword Demons", "Blade Saint", 14000, 9000, "Saint's Katana"),
        CampaignMission(79, "The Ancient Warrior", "Chapter 9: Endless Fortress", "Learn the truth of the first hunters' fall.", "Ancestral Hall", "Corrupted Knights", "Zareth, First Hunter", 18000, 12000, "Ancient Blade"),

        // Chapter 10
        CampaignMission(82, "The Crimson King: Phase 1", "Chapter 10: Dawn of Hunters", "Survive the Blood Monarch's first devastating assault.", "Throne Room", "Blood Parasites", "Crimson King (Monarch)", 22000, 15000, "King's Blood Crystal"),
        CampaignMission(85, "Seven Breaths Harmony", "Chapter 10: Dawn of Hunters", "Harmonize all seven elemental breathing styles.", "Spiritual Realm", "Manifestations of Doubt", "Inner Demon", 28000, 20000, "Advanced Skill Tree"),
        CampaignMission(89, "Dawn Strike", "Chapter 10: Dawn of Hunters", "Gather the remaining hunters for the final stand.", "Fortress Exterior", "Demonic Swarm", "Crimson King (Shadow)", 35000, 25000, "Solar Breathing Unlocked"),
        CampaignMission(90, "Dawn of Hunters — The First Sunrise", "Chapter 10: Dawn of Hunters", "Defeat the Crimson King and hold the line until the sun rises over Elyndra.", "Final Battlefield", "Crimson Champions", "CRIMSON KING (TRUE FORM)", 50000, 50000, "Dawn Hunter Armor & Dawnbringer")
    )

    val sampleDialogue: List<DialogueLine> = listOf(
        DialogueLine(
            speaker = "KAI",
            title = "Shadow Demon Hunter",
            text = "The night is getting darker... something ancient is stirring in the forest.",
            portraitAvatar = "kai",
            choices = listOf("Draw Nichirin Katana", "Activate Aether Breathing", "Signal Mira")
        ),
        DialogueLine(
            speaker = "MIRA",
            title = "Survivor of Ashen Village",
            text = "Kai, be careful! The miasma here is thicker than before. The Crimson King's influence is spreading.",
            portraitAvatar = "mira",
            choices = listOf("I will protect you", "Stay behind my blade", "Prepare your healing barrier")
        ),
        DialogueLine(
            speaker = "RIVEN",
            title = "Master of the First Breath",
            text = "Do not rely on sight alone, boy. Listen to the wind, control your heartbeat, and become the tide.",
            portraitAvatar = "riven",
            choices = listOf("Total Concentration: Constant!", "Teach me the next form", "I won't let you down")
        ),
        DialogueLine(
            speaker = "CRIMSON KING",
            title = "Monarch of Eternal Darkness",
            text = "You foolish mortals dare challenge eternity with fragile steel? Tonight, humanity ends.",
            portraitAvatar = "demon_king",
            choices = listOf("Eternity ends when the sun rises!", "Dawnbringer will purge you!", "Taste my blade!")
        )
    )
}
