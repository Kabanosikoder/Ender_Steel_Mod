package net.panther.endersteel.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.block.ModBlocks;
import net.panther.endersteel.enchantment.ModEnchantments;
import net.panther.endersteel.item.ModItems;

public class ModItemGroup {
    public static final ItemGroup ENDER_STEEL_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(EnderSteel.MOD_ID, "ender_steel_group"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.ender_steel_group"))
                    .icon(() -> new ItemStack(ModItems.ENDER_STEEL_INGOT)).entries((displayContext, entries) -> {

                        // Items:
                        entries.add(ModItems.ENDER_STEEL_INGOT);
                        entries.add(ModItems.ENDER_SCRAP);

                        entries.add(ModItems.ENDER_STEEL_SWORD);
                        entries.add(ModItems.ENDER_STEEL_PICKAXE);
                        entries.add(ModItems.ENDER_STEEL_AXE);
                        entries.add(ModItems.ENDER_STEEL_SCYTHE);
                        entries.add(ModItems.ENDER_STEEL_SHOVEL);

                        entries.add(ModItems.ENDER_STEEL_HELMET);
                        entries.add(ModItems.ENDER_STEEL_CHESTPLATE);
                        entries.add(ModItems.ENDER_STEEL_LEGGINGS);
                        entries.add(ModItems.ENDER_STEEL_BOOTS);

                        entries.add(ModItems.BAGEL); // funny bagel

                        // Enchantments
                        entries.add(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(ModEnchantments.VOID_STRIKE, 1)));
                        entries.add(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(ModEnchantments.ENDER_STREAK, 1)));
                        entries.add(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(ModEnchantments.ENDER_STREAK, 2)));
                        entries.add(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(ModEnchantments.ENDER_STREAK, 3)));

                        // Blocks
                        entries.add(ModBlocks.ENDER_STEEL_BLOCK);

                    }).build());

    public static void registerItemGroups() {
        EnderSteel.LOGGER.info("Registering Item Groups for " + EnderSteel.MOD_ID);
    }
}
