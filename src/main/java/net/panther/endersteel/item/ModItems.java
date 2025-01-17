package net.panther.endersteel.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.block.ModBlocks;
import net.panther.endersteel.item.custom.BagelItem;
import net.panther.endersteel.item.custom.EnderSteelArmorItem;
import net.panther.endersteel.item.custom.EnderSteelScytheItem;
import net.panther.endersteel.item.custom.EnderSteelSwordItem;

import java.util.Arrays;

public class ModItems {
    public static final Item ENDER_SCRAP = registerItem("ender_scrap",
            new Item(new FabricItemSettings()));
    public static final Item ENDER_STEEL_INGOT = registerItem("ender_steel_ingot",
            new Item(new FabricItemSettings()));
    public static final Item ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE = registerItem("ender_steel_upgrade_smithing_template",
            new SmithingTemplateItem(
                    Text.translatable("item.endersteel.smithing_template.ender_steel_upgrade.applies_to"),
                    Text.translatable("item.endersteel.smithing_template.ender_steel_upgrade.ingredients"),
                    Text.translatable("item.endersteel.smithing_template.ender_steel_upgrade.title"),
                    Text.translatable("item.endersteel.smithing_template.ender_steel_upgrade.base_slot_description"),
                    Text.translatable("item.endersteel.smithing_template.ender_steel_upgrade.additions_slot_description"),
                    Arrays.asList(
                            new Identifier("item/empty_slot_sword"),
                            new Identifier("item/empty_slot_pickaxe"),
                            new Identifier("item/empty_slot_axe"),
                            new Identifier("item/empty_slot_shovel"),
                            new Identifier("item/empty_slot_helmet"),
                            new Identifier("item/empty_slot_chestplate"),
                            new Identifier("item/empty_slot_leggings"),
                            new Identifier("item/empty_slot_boots")
                    ),
                    Arrays.asList(new Identifier("item/empty_slot_ingot"))));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(EnderSteel.MOD_ID, name), item);
    }
        // Tools
    public static final Item ENDER_STEEL_SWORD = registerItem("ender_steel_sword",
            new EnderSteelSwordItem(EndSteelToolMaterial.ENDER_STEEL, 3, -2.5f, new FabricItemSettings()));
    public static final Item ENDER_STEEL_PICKAXE = registerItem("ender_steel_pickaxe",
            new PickaxeItem(EndSteelToolMaterial.ENDER_STEEL, 1, -2.8f, new FabricItemSettings()));
    public static final Item ENDER_STEEL_AXE = registerItem("ender_steel_axe",
            new AxeItem(EndSteelToolMaterial.ENDER_STEEL, 6, -3.2f, new FabricItemSettings()));
    public static final Item ENDER_STEEL_SCYTHE = registerItem("ender_steel_scythe",
            new EnderSteelScytheItem(EndSteelToolMaterial.ENDER_STEEL, 5, -2.7f, new FabricItemSettings()));
    public static final Item ENDER_STEEL_SHOVEL = registerItem("ender_steel_shovel",
            new ShovelItem(EndSteelToolMaterial.ENDER_STEEL, 1, -2.8f, new FabricItemSettings()));
        // Armor
    public static final Item ENDER_STEEL_HELMET = registerItem("ender_steel_helmet",
            new EnderSteelArmorItem(EndSteelArmorMaterials.ENDER_STEEL, ArmorItem.Type.HELMET, new FabricItemSettings()));
    public static final Item ENDER_STEEL_CHESTPLATE = registerItem("ender_steel_chestplate",
            new EnderSteelArmorItem(EndSteelArmorMaterials.ENDER_STEEL, ArmorItem.Type.CHESTPLATE, new FabricItemSettings()));
    public static final Item ENDER_STEEL_LEGGINGS = registerItem("ender_steel_leggings",
            new EnderSteelArmorItem(EndSteelArmorMaterials.ENDER_STEEL, ArmorItem.Type.LEGGINGS, new FabricItemSettings()));
    public static final Item ENDER_STEEL_BOOTS = registerItem("ender_steel_boots",
            new EnderSteelArmorItem(EndSteelArmorMaterials.ENDER_STEEL, ArmorItem.Type.BOOTS, new FabricItemSettings()));
        // Bagel :3
    public static final Item BAGEL = registerItem("bagel", new BagelItem(new FabricItemSettings()));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(ENDER_SCRAP);
        entries.add(ENDER_STEEL_INGOT);

        entries.add(BAGEL);

        entries.add(ENDER_STEEL_SWORD);
        entries.add(ENDER_STEEL_PICKAXE);
        entries.add(ENDER_STEEL_AXE);
        entries.add(ENDER_STEEL_SCYTHE);
        entries.add(ENDER_STEEL_SHOVEL);

        entries.add(ENDER_STEEL_HELMET);
        entries.add(ENDER_STEEL_CHESTPLATE);
        entries.add(ENDER_STEEL_LEGGINGS);
        entries.add(ENDER_STEEL_BOOTS);
        
        entries.add(ModBlocks.ENDER_STEEL_BLOCK);
    }

    public static void registerModItems() {
        EnderSteel.LOGGER.info("Registering Mod Items for " + EnderSteel.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}
