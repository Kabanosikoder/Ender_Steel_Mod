package net.panther.endersteel;

import net.fabricmc.api.ModInitializer;
import net.panther.endersteel.block.ModBlocks;
import net.panther.endersteel.block.custom.EnderSteelStareBlock;
import net.panther.endersteel.effect.ModEffects;
import net.panther.endersteel.enchantment.ModEnchantments;
import net.panther.endersteel.item.ModItemGroup;
import net.panther.endersteel.item.ModItems;
import net.panther.endersteel.util.ModEventHandlers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnderSteel implements ModInitializer {
    public static final String MOD_ID = "endersteel";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItemGroup.registerItemGroups();

        ModEffects.registerEffects();
        ModItems.registerModItems();
        ModBlocks.registerModBlocks();
        ModEnchantments.registerModEnchantments();

        // Register the block state handler
        EnderSteelStareBlock.LookAtBlockHandler.register();

        ModEventHandlers.registerEventHandlers();

        LOGGER.info("Initializing " + MOD_ID);
    }
}