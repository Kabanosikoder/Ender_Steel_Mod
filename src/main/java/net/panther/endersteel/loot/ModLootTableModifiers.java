package net.panther.endersteel.loot;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.panther.endersteel.item.ModItems;


public class ModLootTableModifiers {

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source) -> {
            if (key.equals(LootTables.END_CITY_TREASURE_CHEST)) {
                LootPool.Builder pb = LootPool.builder()
                        .with(ItemEntry.builder(ModItems.ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE).weight(25)) // 25% chance
                        .with(ItemEntry.builder(Items.AIR).weight(75)); // 75% chance for no drop
                tableBuilder.pool(pb);
            }
        });
    }
}
