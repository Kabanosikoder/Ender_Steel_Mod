package net.panther.endersteel.world;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.panther.endersteel.EnderSteel;

public class ModFeatures {
    public static final RegistryKey<net.minecraft.world.gen.feature.PlacedFeature> ENDER_REMNANT_LARGE_PLACED_KEY = RegistryKey.of(
            RegistryKeys.PLACED_FEATURE, Identifier.of(EnderSteel.MOD_ID, "ender_remnant_large"));
    
    public static final RegistryKey<net.minecraft.world.gen.feature.PlacedFeature> ENDER_REMNANT_SMALL_PLACED_KEY = RegistryKey.of(
            RegistryKeys.PLACED_FEATURE, Identifier.of(EnderSteel.MOD_ID, "ender_remnant_small"));

    public static void registerFeatures() {
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES, ENDER_REMNANT_LARGE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInTheEnd(),
                GenerationStep.Feature.UNDERGROUND_ORES, ENDER_REMNANT_SMALL_PLACED_KEY);
    }
}
