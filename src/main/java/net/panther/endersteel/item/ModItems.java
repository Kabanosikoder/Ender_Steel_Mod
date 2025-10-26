package net.panther.endersteel.item;

import net.minecraft.item.*;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.item.custom.*;

import java.util.function.Function;

public class ModItems {
    public static final Item ENDER_SCRAP = registerItem("ender_scrap", Item::new);
    public static final Item ENDER_STEEL_INGOT = registerItem("ender_steel_ingot", Item::new);
    public static final Item ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE = registerItem("ender_steel_upgrade_smithing_template",
            SmithingTemplateItem::of);

    // Tools
    public static final Item ENDER_STEEL_SWORD = registerItem("ender_steel_sword",
            setting -> new EnderSteelSwordItem(EndSteelToolMaterial.ENDER_STEEL, 3, -2.5f, setting));

    public static final Item ENDER_STEEL_PICKAXE = registerItem("ender_steel_pickaxe",
            setting -> new PickaxeItem(EndSteelToolMaterial.ENDER_STEEL,-1, -2.8f, setting));

    public static final Item ENDER_STEEL_AXE = registerItem("ender_steel_axe",
            setting -> new AxeItem(EndSteelToolMaterial.ENDER_STEEL,  8, -3.2f, setting));

    public static final Item ENDER_STEEL_SCYTHE = registerItem("ender_steel_scythe",
            setting -> new EnderSteelScytheItem(EndSteelToolMaterial.ENDER_STEEL,setting));

    public static final Item ENDER_STEEL_SHOVEL = registerItem("ender_steel_shovel",
            setting -> new ShovelItem(EndSteelToolMaterial.ENDER_STEEL, 0.5f, -2.8f, setting));
            
    public static final Item VOID_MACE = registerItem("void_mace",
            setting -> new VoidMaceItem(EndSteelToolMaterial.ENDER_STEEL, setting
                    .maxDamage(1500)));

    // Armor
    public static final Item ENDER_STEEL_HELMET = registerItem("ender_steel_helmet",
            setting -> new EnderSteelArmorItem(EndSteelArmorMaterials.ENDER_STEEL_ARMOR_MATERIAL, EquipmentType.HELMET, setting
                    .maxDamage(EquipmentType.HELMET.getMaxDamage(37))));
    public static final Item ENDER_STEEL_CHESTPLATE = registerItem("ender_steel_chestplate",
            setting -> new EnderSteelArmorItem(EndSteelArmorMaterials.ENDER_STEEL_ARMOR_MATERIAL, EquipmentType.CHESTPLATE, setting
                    .maxDamage(EquipmentType.CHESTPLATE.getMaxDamage(37))));
    public static final Item ENDER_STEEL_LEGGINGS = registerItem("ender_steel_leggings",
            setting -> new EnderSteelArmorItem(EndSteelArmorMaterials.ENDER_STEEL_ARMOR_MATERIAL, EquipmentType.LEGGINGS, setting
                    .maxDamage(EquipmentType.LEGGINGS.getMaxDamage(37))));
    public static final Item ENDER_STEEL_BOOTS = registerItem("ender_steel_boots",
            setting -> new EnderSteelArmorItem(EndSteelArmorMaterials.ENDER_STEEL_ARMOR_MATERIAL, EquipmentType.BOOTS, setting
                    .maxDamage(EquipmentType.BOOTS.getMaxDamage(37))));

    // Food
    public static final Item BAGEL = registerItem("bagel",
            BagelItem::new);

        private static Item registerItem(String name, Function<Item.Settings, Item> function) {
            return Registry.register(Registries.ITEM, Identifier.of(EnderSteel.MOD_ID, name),
                    function.apply(new Item.Settings().registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(EnderSteel.MOD_ID, name)))));
        }

    public static void registerModItems() {
        EnderSteel.LOGGER.info("Registering Mod Items for " ,EnderSteel.MOD_ID);
    }
}