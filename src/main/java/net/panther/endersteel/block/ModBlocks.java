package net.panther.endersteel.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.block.custom.EnderSteelStareBlock;

public class ModBlocks {

    public static final Block ENDER_STEEL_BLOCK = registerBlock("ender_steel_block",
            new EnderSteelStareBlock(FabricBlockSettings.create().mapColor(MapColor.TERRACOTTA_BLUE).strength(6f).requiresTool()
                    .luminance(state -> state.get(EnderSteelStareBlock.OPEN_STATE) == EnderSteelStareBlock.OpenState.FULLY_OPEN ? 7 : 0)));

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(EnderSteel.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM, new Identifier(EnderSteel.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }

    public static void registerModBlocks(){
        EnderSteel.LOGGER.info("Registering Mod Blocks for "+ EnderSteel.MOD_ID);
    }
}
