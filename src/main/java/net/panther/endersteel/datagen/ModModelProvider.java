package net.panther.endersteel.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.equipment.EquipmentModel;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.item.EndSteelArmorMaterials;
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

        EquipmentModel enderSteelModel = EquipmentModel.builder()
                .addHumanoidLayers(Identifier.of(EnderSteel.MOD_ID, "ender_steel"))
                .build();

        itemModelGenerator.registerArmor(
                ModItems.ENDER_STEEL_HELMET,
                Identifier.of(EnderSteel.MOD_ID, "ender_steel"),
                enderSteelModel,
                EquipmentSlot.HEAD
        );
        itemModelGenerator.registerArmor(
                ModItems.ENDER_STEEL_CHESTPLATE,
                Identifier.of(EnderSteel.MOD_ID, "ender_steel"),
                enderSteelModel,
                EquipmentSlot.CHEST
        );
        itemModelGenerator.registerArmor(
                ModItems.ENDER_STEEL_LEGGINGS,
                Identifier.of(EnderSteel.MOD_ID, "ender_steel"),
                enderSteelModel,
                EquipmentSlot.LEGS
        );
        itemModelGenerator.registerArmor(
                ModItems.ENDER_STEEL_BOOTS,
                Identifier.of(EnderSteel.MOD_ID, "ender_steel"),
                enderSteelModel,
                EquipmentSlot.FEET
        );

    }
}
