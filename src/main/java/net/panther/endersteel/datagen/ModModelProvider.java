package net.panther.endersteel.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.item.ArmorItem;
import net.panther.endersteel.item.ModItems;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }


    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.ENDER_STEEL_INGOT, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENDER_SCRAP, Models.GENERATED);

        itemModelGenerator.register(ModItems.ENDER_STEEL_SWORD, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENDER_STEEL_PICKAXE, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENDER_STEEL_AXE, Models.GENERATED);
        itemModelGenerator.register(ModItems.ENDER_STEEL_SHOVEL, Models.GENERATED);

        itemModelGenerator.registerArmor(((ArmorItem) ModItems.ENDER_STEEL_HELMET));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.ENDER_STEEL_CHESTPLATE));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.ENDER_STEEL_LEGGINGS));
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.ENDER_STEEL_BOOTS));

        itemModelGenerator.register(ModItems.ENDER_STEEL_ARMOR_SMITHING_TEMPLATE, Models.GENERATED);

    }
}
