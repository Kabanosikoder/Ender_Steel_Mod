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
    public static final TagKey<Item> SCYTHE_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("endersteel", "scythe_enchantable"));
    // Define custom ender steel chestplate tag for enchanting
    public static final TagKey<Item> ENDER_STEEL_CHESTPLATE = TagKey.of(RegistryKeys.ITEM, Identifier.of("endersteel", "ender_steel_chestplate"));
    // Define custom sword enchantable tag
    public static final TagKey<Item> SWORD_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("endersteel", "sword_enchantable"));

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.ENDER_STEEL_HELMET, ModItems.ENDER_STEEL_CHESTPLATE,
                        ModItems.ENDER_STEEL_LEGGINGS, ModItems.ENDER_STEEL_BOOTS);

        // Add the scythe to the custom enchantable tag
        getOrCreateTagBuilder(SCYTHE_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_SCYTHE);
        
        // Add the ender steel chestplate to its custom tag
        getOrCreateTagBuilder(ENDER_STEEL_CHESTPLATE)
                .add(ModItems.ENDER_STEEL_CHESTPLATE);

        // Add the sword to its custom enchantable tag
        getOrCreateTagBuilder(SWORD_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_SWORD);

        // Add tools that can mine level 5 blocks (Ender Steel tier)
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("fabric", "needs_tool_level_5")))
                .add(ModItems.ENDER_STEEL_PICKAXE)
                .add(ModItems.ENDER_STEEL_AXE)
                .add(ModItems.ENDER_STEEL_SCYTHE)
                .add(ModItems.ENDER_STEEL_SHOVEL);

    }
}
