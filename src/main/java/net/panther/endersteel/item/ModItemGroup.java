package net.panther.endersteel.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.block.ModBlocks;

public class ModItemGroup {
    public static final ItemGroup ENDER_STEEL_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of(EnderSteel.MOD_ID, "ender_steel_group"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.ender_steel_group"))
                    .icon(() -> new ItemStack(ModItems.ENDER_STEEL_INGOT)).entries((displayContext, entries) -> {

                        // Items:
                        entries.add(ModItems.ENDER_STEEL_INGOT);
                        entries.add(ModItems.ENDER_SCRAP);
                        entries.add(ModItems.ENDER_STEEL_UPGRADE_SMITHING_TEMPLATE);

                        entries.add(ModItems.ENDER_STEEL_SWORD);
                        entries.add(ModItems.ENDER_STEEL_PICKAXE);
                        entries.add(ModItems.ENDER_STEEL_AXE);
                        entries.add(ModItems.ENDER_STEEL_SCYTHE);
                        entries.add(ModItems.ENDER_STEEL_SHOVEL);

                        entries.add(ModItems.ENDER_STEEL_HELMET);
                        entries.add(ModItems.ENDER_STEEL_CHESTPLATE);
                        entries.add(ModItems.ENDER_STEEL_LEGGINGS);
                        entries.add(ModItems.ENDER_STEEL_BOOTS);

                        entries.add(ModItems.BAGEL); // funny bagel ngl

                        // Blocks
                        entries.add(ModBlocks.ENDER_STEEL_BLOCK);
                        entries.add(ModBlocks.ENDER_REMNANT);

                        // Add enchanted books
                        var enchantments = displayContext.lookup().getOptionalWrapper(RegistryKeys.ENCHANTMENT).orElseThrow();
                        enchantments.streamEntries().forEach(enchantmentEntry -> {
                            if (enchantmentEntry.getKey().orElseThrow().getValue().getNamespace().equals(EnderSteel.MOD_ID)) {
                                entries.add(EnchantedBookItem.forEnchantment(
                                    new EnchantmentLevelEntry(enchantmentEntry, 1)));
                            }
                        });

                    }).build());

    public static void registerItemGroups() {
        EnderSteel.LOGGER.info("Registering Item Groups for " + EnderSteel.MOD_ID);
    }
}
