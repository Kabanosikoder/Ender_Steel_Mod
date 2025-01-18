package net.panther.endersteel.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.panther.endersteel.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.ENDER_STEEL_HELMET, ModItems.ENDER_STEEL_CHESTPLATE,
                ModItems.ENDER_STEEL_LEGGINGS, ModItems.ENDER_STEEL_BOOTS);

        // Add tools that can mine level 5 blocks (Ender Steel tier)
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.ITEM, new Identifier("fabric", "needs_tool_level_5")))
                .add(ModItems.ENDER_STEEL_PICKAXE)
                .add(ModItems.ENDER_STEEL_AXE)
                .add(ModItems.ENDER_STEEL_SCYTHE)
                .add(ModItems.ENDER_STEEL_SHOVEL);

    }
}
