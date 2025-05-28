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
    public static final TagKey<Item> SCYTHE_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("endersteel", "enchantable/scythe"));
    public static final TagKey<Item> SWORD_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("endersteel", "enchantable/sword"));
    public static final TagKey<Item> ENDER_STEEL_SWORD_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("endersteel", "enchantable/ender_steel_sword"));
    public static final TagKey<Item> ARMOR_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("endersteel", "enchantable/armor"));
    public static final TagKey<Item> REPULSIVE_SHRIEK_ENCHANTABLE = TagKey.of(RegistryKeys.ITEM, Identifier.of("endersteel", "enchantable/repulsive_shriek"));

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {

        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.ENDER_STEEL_HELMET, ModItems.ENDER_STEEL_CHESTPLATE,
                        ModItems.ENDER_STEEL_LEGGINGS, ModItems.ENDER_STEEL_BOOTS);

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

        getOrCreateTagBuilder(ItemTags.SWORDS)
                .add(ModItems.ENDER_STEEL_SWORD)
                .add(ModItems.ENDER_STEEL_SCYTHE);

        getOrCreateTagBuilder(ItemTags.PICKAXES)
                .add(ModItems.ENDER_STEEL_PICKAXE);

        getOrCreateTagBuilder(ItemTags.AXES)
                .add(ModItems.ENDER_STEEL_AXE);

        getOrCreateTagBuilder(ItemTags.SHOVELS)
                .add(ModItems.ENDER_STEEL_SHOVEL);

        getOrCreateTagBuilder(ItemTags.SWORD_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_SWORD)
                .add(ModItems.ENDER_STEEL_SCYTHE);

        getOrCreateTagBuilder(ItemTags.MINING_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_PICKAXE)
                .add(ModItems.ENDER_STEEL_AXE)
                .add(ModItems.ENDER_STEEL_SHOVEL);
        
        getOrCreateTagBuilder(ItemTags.WEAPON_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_SWORD)
                .add(ModItems.ENDER_STEEL_AXE)
                .add(ModItems.ENDER_STEEL_SCYTHE);  // YES I FIXED THE TAG ISSUE CUZ I'M NOT STUPID YIPEE

        getOrCreateTagBuilder(ENDER_STEEL_SWORD_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_SWORD);

        getOrCreateTagBuilder(SCYTHE_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_SCYTHE);

        getOrCreateTagBuilder(REPULSIVE_SHRIEK_ENCHANTABLE)
                .add(ModItems.ENDER_STEEL_CHESTPLATE);

        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, Identifier.of("fabric", "needs_tool_level_5")))
                .add(ModItems.ENDER_STEEL_PICKAXE)
                .add(ModItems.ENDER_STEEL_AXE)
                .add(ModItems.ENDER_STEEL_SCYTHE)
                .add(ModItems.ENDER_STEEL_SHOVEL);
    }
}
