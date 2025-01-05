package net.panther.endersteel.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;

public class ModEnchantments {
    public static final Enchantment GAZING_VOID = register("gazing_void", new GazingVoidEnchantment());
    public static final Enchantment VOID_STRIKE = register("void_strike", new VoidStrikeEnchantment());
    public static final Enchantment ENDER_STREAK = register("ender_streak", new EnderStreakEnchantment());
    public static final Enchantment PHANTOM_HARVEST = register("phantom_harvest", new PhantomHarvestEnchantment());
    public static final Enchantment REPULSIVE_SHRIEK = register("repulsive_shriek", new RepulsiveShriekEnchantment());

    public static void registerModEnchantments() {
        EnderSteel.LOGGER.info("Registering enchantments for " + EnderSteel.MOD_ID);
    }

    private static Enchantment register(String name, Enchantment enchantment) {
        return Registry.register(
                Registries.ENCHANTMENT,
                new Identifier(EnderSteel.MOD_ID, name),
                enchantment
        );
    }
}