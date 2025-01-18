package net.panther.endersteel.loot;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.util.Identifier;
import net.panther.endersteel.item.ModItems;

public class ModLootTableModifiers {
    private static final Identifier END_CITY_CHEST_ID
            = new Identifier("minecraft", "chests/end_city_treasure");

    public static void modifyLootTables() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (END_CITY_CHEST_ID.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.15f)) // 15% chance
                        .with(ItemEntry.builder(ModItems.ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE))
                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f)).build());

                tableBuilder.pool(poolBuilder.build());
            }
        });
    }
}
