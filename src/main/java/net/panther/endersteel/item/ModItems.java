package net.panther.endersteel.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.block.ModBlocks;

public class ModItems {
    public static final Item ENDER_SCRAP = registerItem("ender_scrap",
            new Item(new FabricItemSettings()));
    public static final Item ENDER_STEEL_INGOT = registerItem("ender_steel_ingot",
            new Item(new FabricItemSettings()));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(EnderSteel.MOD_ID, name), item);
    }

    private static void itemGroupIngredients(FabricItemGroupEntries entries){
        entries.add(ENDER_SCRAP);
        entries.add(ENDER_STEEL_INGOT);

        entries.add(ModBlocks.ENDER_STEEL_BLOCK);
    }

    public static void registerModItems(){
        EnderSteel.LOGGER.info("Registering Mod items for "+EnderSteel.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::itemGroupIngredients);
    }
}
