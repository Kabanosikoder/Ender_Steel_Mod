package net.panther.endersteel;

import net.fabricmc.api.ModInitializer;
import net.panther.endersteel.advancement.ModCriteria;
import net.panther.endersteel.block.ModBlocks;
import net.panther.endersteel.block.custom.EnderSteelStareBlock;
import net.panther.endersteel.component.EnderSteelDataComponents;
import net.panther.endersteel.config.ModConfig;
import net.panther.endersteel.effect.ModEffects;
import net.panther.endersteel.enchantment.ModEnchantmentEffects;
import net.panther.endersteel.event.EnderSteelArmorEvents;
import net.panther.endersteel.item.ModItemGroup;
import net.panther.endersteel.item.ModItems;
import net.panther.endersteel.loot.ModLootTableModifiers;
import net.panther.endersteel.world.ModFeatures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnderSteel implements ModInitializer {
    public static final String MOD_ID = "endersteel";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Load config first
        ModConfig.loadConfig();

        ModItemGroup.registerItemGroups();

        ModLootTableModifiers.modifyLootTables();

        ModItems.registerModItems();

        ModBlocks.registerModBlocks();

        EnderSteelArmorEvents.register();

        EnderSteelStareBlock.LookAtBlockHandler.register();

        ModEffects.registerModEffects();

        ModCriteria.register();

        ModFeatures.registerFeatures();

        EnderSteelDataComponents.registerDataComponents();

        ModEnchantmentEffects.registerModEnchantmentEffects();

        LOGGER.info("Hey goobers :3 " + MOD_ID);
    }
}