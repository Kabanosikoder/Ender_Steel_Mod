package net.panther.endersteel.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.block.ModBlocks;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.block.ModBlocks;

public class ModItemGroup {
    public static final ItemGroup ENDER_STEEL_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(EnderSteel.MOD_ID, "ender_steel_group"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.ender_steel_group"))
                    .icon(() -> new ItemStack(ModItems.ENDER_STEEL_INGOT)).entries((displayContext, entries) -> {
                        // adds entries to the Item Group in the creative mode tab
                        // Items:
                        entries.add(ModItems.ENDER_STEEL_INGOT);
                        entries.add(ModItems.ENDER_SCRAP);

                        entries.add(ModItems.ENDER_STEEL_SWORD);
                        entries.add(ModItems.ENDER_STEEL_PICKAXE);
                        entries.add(ModItems.ENDER_STEEL_AXE);
                        entries.add(ModItems.ENDER_STEEL_HOE);
                        entries.add(ModItems.ENDER_STEEL_SHOVEL);

                        // Blocks
                        entries.add(ModBlocks.ENDER_STEEL_BLOCK);


                    }).build());

    public static void registerItemGroups(){

    }
}
