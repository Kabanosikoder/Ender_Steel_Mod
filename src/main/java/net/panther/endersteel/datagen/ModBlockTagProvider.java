package net.panther.endersteel.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.panther.endersteel.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public static final TagKey<Block> NEEDS_ENDER_STEEL_TOOL = TagKey.of(RegistryKeys.BLOCK, Identifier.of("endersteel", "needs_ender_steel_tool"));
    public static final TagKey<Block> INCORRECT_FOR_ENDER_STEEL_TOOL = TagKey.of(RegistryKeys.BLOCK, Identifier.of("endersteel", "incorrect_for_ender_steel_tool"));

    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        // Requires Ender Steel tool
        getOrCreateTagBuilder(NEEDS_ENDER_STEEL_TOOL)
                .add(ModBlocks.ENDER_REMNANT)
                .add(ModBlocks.ENDER_STEEL_BLOCK);

        // Marks blocks that shouldn't be mined with Ender Steel tools
        getOrCreateTagBuilder(INCORRECT_FOR_ENDER_STEEL_TOOL);

        // Requires level 5 mining level (Ender Steel Tier)
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.BLOCK, Identifier.of("fabric", "needs_tool_level_5")))
                .add(ModBlocks.ENDER_REMNANT)
                .add(ModBlocks.ENDER_STEEL_BLOCK);

        // Requires diamond tool
        getOrCreateTagBuilder(TagKey.of(RegistryKeys.BLOCK, Identifier.of("fabric", "needs_tool_level_4")))
                .add(ModBlocks.ENDER_REMNANT)
                .add(ModBlocks.ENDER_STEEL_BLOCK);

        // Requires pickaxe
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.ENDER_REMNANT)
                .add(ModBlocks.ENDER_STEEL_BLOCK);
    }
}
