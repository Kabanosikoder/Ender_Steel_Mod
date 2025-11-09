package net.panther.endersteel.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.block.custom.EnderSteelStareBlock;

import java.util.function.Function;

public class ModBlocks {

    public static final Block ENDER_STEEL_BLOCK = registerBlock("ender_steel_block", key ->
            new EnderSteelStareBlock(
                    AbstractBlock.Settings.create().registryKey(key).mapColor(MapColor.TERRACOTTA_BLUE)
                            .strength(8f)
                            .requiresTool()
                            .luminance(state ->
                                    state.get(EnderSteelStareBlock.OPEN_STATE) == EnderSteelStareBlock.OpenState.FULLY_OPEN ? 7 : 0
                            )));

    public static final Block ENDER_REMNANT = registerBlock("ender_remnant", key ->
            new Block(
                    AbstractBlock.Settings.copy(Blocks.ANCIENT_DEBRIS)
                            .registryKey(key)
                            .strength(30.0f, 1200.0f)
                            .requiresTool()
            ));

    private static Block registerBlock(String name, Function<RegistryKey<Block>, Block> factory) {
        Identifier id = Identifier.of(EnderSteel.MOD_ID, name);

        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
        Block block = factory.apply(blockKey);

        Block registered = Registry.register(Registries.BLOCK, id, block);

        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
        Registry.register(Registries.ITEM, id, new BlockItem(registered, new Item.Settings().registryKey(itemKey)));

        return registered;
    }

    public static void registerModBlocks() {
        EnderSteel.LOGGER.info("Registering Mod Blocks for {}", EnderSteel.MOD_ID);
        // Optional: touch to force class init order
        ENDER_STEEL_BLOCK.getDefaultState();
        ENDER_REMNANT.getDefaultState();
    }
}

