package net.panther.endersteel.enchantment;

import com.mojang.serialization.MapCodec;

import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;
import net.panther.endersteel.enchantment.effect.*;

public class ModEnchantmentEffects {
    public static final MapCodec<? extends EnchantmentEntityEffect> GAZING_VOID =
            registerEntityEffect("gazing_void", GazingVoidEffect.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> VOID_STRIKE =
            registerEntityEffect("void_strike", VoidStrikeEffect.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> ENDER_STREAK =
            registerEntityEffect("ender_streak", EnderStreakEffect.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> PHANTOM_HARVEST =
            registerEntityEffect("phantom_harvest", PhantomHarvestEffect.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> REPULSIVE_SHRIEK =
            registerEntityEffect("repulsive_shriek", RepulsiveShriekEffect.CODEC);
    public static final MapCodec<? extends EnchantmentEntityEffect> GRAVITIDE =
            registerEntityEffect("gravitide", GravitideEffect.CODEC);

    private static MapCodec<? extends EnchantmentEntityEffect> registerEntityEffect(String name,
        MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.of(EnderSteel.MOD_ID, name), codec);
    }

    public static void registerModEnchantmentEffects() {
        // Effects are registered via the static fields above
        EnderSteel.LOGGER.info("Registering enchantment effects for " + EnderSteel.MOD_ID);
    }
}
