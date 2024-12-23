package net.panther.endersteel.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.panther.endersteel.block.ModBlocks;
import net.panther.endersteel.item.ModItems;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleCubeAll(ModBlocks.ENDER_STEEL_BLOCK);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.ENDER_STEEL_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENDER_SCRAP, Models.GENERATED);

        itemModelGenerator.register(ModItems.ENDER_STEEL_SWORD, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENDER_STEEL_PICKAXE, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENDER_STEEL_AXE, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENDER_STEEL_SCYTHE, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENDER_STEEL_SHOVEL, Models.GENERATED);
    }
}
