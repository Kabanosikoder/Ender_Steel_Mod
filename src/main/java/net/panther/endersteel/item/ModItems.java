package net.panther.endersteel.item;

import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.item.custom.BagelItem;
import net.panther.endersteel.item.custom.EnderSteelArmorItem;
import net.panther.endersteel.item.custom.EnderSteelScytheItem;
import net.panther.endersteel.item.custom.EnderSteelSwordItem;
import net.panther.endersteel.item.custom.VoidMaceItem;
import java.util.Arrays;
import java.util.List;

public class ModItems {
    // Basic Items
    public static final Item ENDER_SCRAP = registerItem("ender_scrap",
            new Item(new Item.Settings()));
    public static final Item ENDER_STEEL_INGOT = registerItem("ender_steel_ingot",
            new Item(new Item.Settings()));
    public static final Item ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE = registerItem("ender_steel_upgrade_smithing_template",
            new SmithingTemplateItem(
                    Text.translatable("item.endersteel.smithing_template.ender_steel_upgrade.applies_to"),
                    Text.translatable("item.endersteel.smithing_template.ender_steel_upgrade.ingredients"),
                    Text.translatable("item.endersteel.smithing_template.ender_steel_upgrade.title"),
                    Text.translatable("item.endersteel.smithing_template.ender_steel_upgrade.base_slot_description"),
                    Text.translatable("item.endersteel.smithing_template.ender_steel_upgrade.additions_slot_description"),
                    Arrays.asList(
                            Identifier.of("minecraft", "item/empty_slot_sword"),
                            Identifier.of("minecraft", "item/empty_slot_pickaxe"),
                            Identifier.of("minecraft", "item/empty_slot_axe"),
                            Identifier.of("minecraft", "item/empty_slot_shovel"),
                            Identifier.of("minecraft", "item/empty_slot_helmet"),
                            Identifier.of("minecraft", "item/empty_slot_chestplate"),
                            Identifier.of("minecraft", "item/empty_slot_leggings"),
                            Identifier.of("minecraft", "item/empty_slot_boots")
                    ),
                    List.of(Identifier.of("minecraft", "item/empty_slot_ingot"))));

    // Tools
    public static final Item ENDER_STEEL_SWORD = registerItem("ender_steel_sword",
            new EnderSteelSwordItem(EndSteelToolMaterial.ENDER_STEEL, new Item.Settings()
                    .attributeModifiers(SwordItem.createAttributeModifiers(EndSteelToolMaterial.ENDER_STEEL, 3, -2.5f))));

    public static final Item ENDER_STEEL_PICKAXE = registerItem("ender_steel_pickaxe",
            new PickaxeItem(EndSteelToolMaterial.ENDER_STEEL, new Item.Settings()
                    .attributeModifiers(PickaxeItem.createAttributeModifiers(EndSteelToolMaterial.ENDER_STEEL, -1, -2.8f))));

    public static final Item ENDER_STEEL_AXE = registerItem("ender_steel_axe",
            new AxeItem(EndSteelToolMaterial.ENDER_STEEL, new Item.Settings()
                    .attributeModifiers(AxeItem.createAttributeModifiers(EndSteelToolMaterial.ENDER_STEEL, 8, -3.2f))));

    public static final Item ENDER_STEEL_SCYTHE = registerItem("ender_steel_scythe",
            new EnderSteelScytheItem(EndSteelToolMaterial.ENDER_STEEL, new Item.Settings()
                    .attributeModifiers(SwordItem.createAttributeModifiers(EndSteelToolMaterial.ENDER_STEEL, 6, -2.75f))));

    public static final Item ENDER_STEEL_SHOVEL = registerItem("ender_steel_shovel",
            new ShovelItem(EndSteelToolMaterial.ENDER_STEEL, new Item.Settings()
                    .attributeModifiers(ShovelItem.createAttributeModifiers(EndSteelToolMaterial.ENDER_STEEL, 0.5f, -2.8f))));

    public static final Item ENDER_STEEL_CORE = registerItem("ender_steel_core",
            new EnderSteelCoreItem(new Item.Settings()));

    public static final Item ENDER_STEEL_ROD = registerItem("ender_steel_rod",
            new EnderSteelRodItem(new Item.Settings()));
            
    public static final Item VOID_MACE = registerItem("void_mace",
            new VoidMaceItem(EndSteelToolMaterial.ENDER_STEEL, new Item.Settings()
                    .maxDamage(3000)));

    // Armor
    public static final Item ENDER_STEEL_HELMET = registerItem("ender_steel_helmet",
            new EnderSteelArmorItem(EndSteelArmorMaterials.ENDER_STEEL_ARMOR_MATERIAL, ArmorItem.Type.HELMET, new Item.Settings()
                    .maxDamage(ArmorItem.Type.HELMET.getMaxDamage(37))));
    public static final Item ENDER_STEEL_CHESTPLATE = registerItem("ender_steel_chestplate",
            new EnderSteelArmorItem(EndSteelArmorMaterials.ENDER_STEEL_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Settings()
                    .maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(37))));
    public static final Item ENDER_STEEL_LEGGINGS = registerItem("ender_steel_leggings",
            new EnderSteelArmorItem(EndSteelArmorMaterials.ENDER_STEEL_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(37))));
    public static final Item ENDER_STEEL_BOOTS = registerItem("ender_steel_boots",
            new EnderSteelArmorItem(EndSteelArmorMaterials.ENDER_STEEL_ARMOR_MATERIAL, ArmorItem.Type.BOOTS, new Item.Settings()
                    .maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(37))));

    // Food
    public static final Item BAGEL = registerItem("bagel",
            new BagelItem(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(EnderSteel.MOD_ID, name), item);
    }

    public static void registerModItems() {
        EnderSteel.LOGGER.info("Registering Mod Items for " + EnderSteel.MOD_ID);
    }
}