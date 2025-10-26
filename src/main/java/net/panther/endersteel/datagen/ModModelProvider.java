package net.panther.endersteel.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
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

        itemModelGenerator.registerArmor(((ArmorItem) ModItems.ENDER_STEEL_HELMET), Identifier.of(EnderSteel.MOD_ID, "ender_steel"),
                EndSteelArmorMaterials.ENDER_STEEL, EquipmentSlot.HEAD);
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.ENDER_STEEL_CHESTPLATE), Identifier.of(EnderSteel.MOD_ID, "ender_steel"),
                EndSteelArmorMaterials.ENDER_STEEL, EquipmentSlot.CHEST);
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.ENDER_STEEL_LEGGINGS), Identifier.of(EnderSteel.MOD_ID, "ender_steel"),
                EndSteelArmorMaterials.ENDER_STEEL, EquipmentSlot.LEGS);
        itemModelGenerator.registerArmor(((ArmorItem) ModItems.ENDER_STEEL_BOOTS), Identifier.of(EnderSteel.MOD_ID, "ender_steel"),
                EndSteelArmorMaterials.ENDER_STEEL, EquipmentSlot.FEET);
    }
}
