package net.panther.endersteel.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.panther.endersteel.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    // Define your custom scythe enchantable tag
    public static final TagKey<Item> SCYTHE_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("endersteel", "enchantable/scythe"));
    public static final TagKey<Item> ARMOR_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("endersteel", "enchantable/armor"));
    // Define custom sword enchantable tag
    public static final TagKey<Item> SWORD_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("endersteel", "enchantable/sword"));

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // Add armor to vanilla tags
        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.ENDER_STEEL_HELMET, ModItems.ENDER_STEEL_CHESTPLATE,
                        ModItems.ENDER_STEEL_LEGGINGS, ModItems.ENDER_STEEL_BOOTS);

        // Add armor to enchantable tags
        getOrCreateTagBuilder(ItemTags.ARMOR_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_HELMET)
                .add(ModItems.ENDER_STEEL_CHESTPLATE)
                .add(ModItems.ENDER_STEEL_LEGGINGS)
                .add(ModItems.ENDER_STEEL_BOOTS);
        getOrCreateTagBuilder(ItemTags.HEAD_ARMOR_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_HELMET);
        getOrCreateTagBuilder(ItemTags.CHEST_ARMOR_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_CHESTPLATE);
        getOrCreateTagBuilder(ItemTags.LEG_ARMOR_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_LEGGINGS);
        getOrCreateTagBuilder(ItemTags.FOOT_ARMOR_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_BOOTS);

        // Add tools to vanilla tags
        getOrCreateTagBuilder(ItemTags.SWORDS)
                .add(ModItems.ENDER_STEEL_SWORD);
        getOrCreateTagBuilder(ItemTags.PICKAXES)
                .add(ModItems.ENDER_STEEL_PICKAXE);
        getOrCreateTagBuilder(ItemTags.AXES)
                .add(ModItems.ENDER_STEEL_AXE);
        getOrCreateTagBuilder(ItemTags.SHOVELS)
                .add(ModItems.ENDER_STEEL_SHOVEL);

        // Add tools to enchantable tags
        getOrCreateTagBuilder(ItemTags.SWORD_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_SWORD);
        getOrCreateTagBuilder(ItemTags.MINING_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_PICKAXE)
                .add(ModItems.ENDER_STEEL_AXE)
                .add(ModItems.ENDER_STEEL_SHOVEL);
        getOrCreateTagBuilder(ItemTags.WEAPON_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_SWORD)
                .add(ModItems.ENDER_STEEL_AXE);

        // Add the scythe to the custom enchantable tag
        getOrCreateTagBuilder(SCYTHE_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_SCYTHE);

        // Add tools that can mine level 5 blocks (Ender Steel tier)
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("fabric", "needs_tool_level_5")))
                .add(ModItems.ENDER_STEEL_PICKAXE)
                .add(ModItems.ENDER_STEEL_AXE)
                .add(ModItems.ENDER_STEEL_SCYTHE)
                .add(ModItems.ENDER_STEEL_SHOVEL);
    }
}
