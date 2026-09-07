package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.EquipmentItem
import com.example.model.EquipmentSlot
import com.example.model.ItemCategory
import com.example.state.GameScreen
import com.example.state.GameViewModel
import com.example.ui.components.JapaneseBrushHeader
import com.example.ui.components.OrnateGlassPanel
import com.example.ui.theme.*

@Composable
fun InventoryScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag("inventory_screen")
            .fillMaxSize()
            .background(DemonNavyBlack)
    ) {
        // Dark anime background art
        Image(
            painter = painterResource(id = R.drawable.anime_game_bg),
            contentDescription = "Bg",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xF207080E))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 20.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .testTag("back_button")
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DemonNavyCard)
                            .border(1.dp, DemonAetherCyan, CircleShape)
                            .clickable { viewModel.navigateTo(GameScreen.MAIN_MENU) },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))
                    JapaneseBrushHeader(title = "INVENTORY", subtitle = "Armory of the Demon Slayer")
                }

                // Currency Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(DemonNavyCard)
                        .border(1.dp, DemonGold, RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🪙", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${viewModel.coins}",
                        color = DemonGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Category Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ItemCategory.values().forEach { category ->
                    val isSelected = viewModel.inventoryTab == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) DemonCrimsonDark else DemonNavySurface)
                            .border(1.dp, if (isSelected) DemonGold else DemonAetherCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .clickable { viewModel.inventoryTab = category }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = category.label.uppercase(),
                            color = if (isSelected) Color.White else DemonMistGray,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3-Column Layout: [Equipped Gear] | [Item Grid] | [Item Inspector]
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Column 1: Equipped Gear Slots
                OrnateGlassPanel(
                    modifier = Modifier
                        .weight(0.28f)
                        .fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "EQUIPPED",
                            color = DemonGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )

                        EquipmentSlot.values().forEach { slot ->
                            val equipped = viewModel.equipment.firstOrNull { it.slot == slot && it.isEquipped }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(DemonNavyCard)
                                    .border(1.dp, equipped?.rarity?.borderGlow ?: Color(0x33FFFFFF), RoundedCornerShape(6.dp))
                                    .clickable {
                                        if (equipped != null) viewModel.selectedItem = equipped
                                    }
                                    .padding(6.dp)
                            ) {
                                Column {
                                    Text(
                                        text = slot.label,
                                        color = DemonMistGray,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = equipped?.name ?: "— Empty —",
                                        color = equipped?.rarity?.color ?: Color.Gray,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }

                // Column 2: Inventory Grid
                OrnateGlassPanel(
                    modifier = Modifier
                        .weight(0.42f)
                        .fillMaxHeight()
                ) {
                    val filteredItems = viewModel.equipment.filter { it.category == viewModel.inventoryTab }
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredItems) { item ->
                            val isSelected = item.id == viewModel.selectedItem.id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) DemonCrimsonDark.copy(alpha = 0.7f) else DemonNavyCard)
                                    .border(1.5.dp, if (isSelected) DemonGold else item.rarity.borderGlow, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.selectedItem = item }
                                    .padding(8.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.Start) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = if (item.slot == EquipmentSlot.SWORD) "🗡️" else "🛡️",
                                            fontSize = 14.sp
                                        )
                                        if (item.isEquipped) {
                                            Text(
                                                text = "EQUIPPED",
                                                color = DemonGold,
                                                fontSize = 7.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.name,
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = item.rarity.label,
                                        color = item.rarity.color,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    if (item.upgradeLevel > 0) {
                                        Text(
                                            text = "+${item.upgradeLevel}",
                                            color = DemonAetherCyan,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Column 3: Item Stats Inspector & Actions
                val item = viewModel.selectedItem
                OrnateGlassPanel(
                    modifier = Modifier
                        .weight(0.30f)
                        .fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = item.name,
                                color = item.rarity.color,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "${item.rarity.label} • ${item.slot.label}",
                                color = DemonMistGray,
                                fontSize = 9.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Stats list
                            if (item.attack > 0) {
                                Text("⚔️ ATK: +${item.attack}", color = DemonCrimsonGlow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            if (item.defense > 0) {
                                Text("🛡️ DEF: +${item.defense}", color = DemonAetherCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            if (item.speed > 0) {
                                Text("💨 SPEED: +${item.speed}", color = DemonWindGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            if (item.critRate > 0) {
                                Text("🎯 CRIT: +${item.critRate}%", color = DemonGold, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            if (item.passiveDesc.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = item.passiveDesc,
                                    color = DemonGold,
                                    fontSize = 8.sp,
                                    lineHeight = 10.sp
                                )
                            }
                        }

                        // Bottom Actions: Equip, Upgrade, Sell
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            // Equip / Unequip
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (item.isEquipped) DemonNavyCard else DemonCrimsonDark)
                                    .border(1.dp, DemonGold, RoundedCornerShape(6.dp))
                                    .clickable { viewModel.equipItem(item) }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (item.isEquipped) "UNEQUIP" else "EQUIP",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Upgrade (+1)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(DemonNavySurface)
                                    .border(1.dp, DemonAetherCyan, RoundedCornerShape(6.dp))
                                    .clickable { viewModel.upgradeItem(item) }
                                    .padding(vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "UPGRADE (500🪙)",
                                    color = DemonAetherCyan,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
