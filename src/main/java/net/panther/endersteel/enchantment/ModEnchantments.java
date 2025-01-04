package net.panther.endersteel.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;

public class ModEnchantments {
    public static final Enchantment GAZING_VOID = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier(EnderSteel.MOD_ID, "gazing_void"),
            new GazingVoidEnchantment()
    );

    public static final Enchantment VOID_STRIKE = register("void_strike",
            new VoidStrikeEnchantment());

    public static final Enchantment ENDER_STREAK = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier(EnderSteel.MOD_ID, "ender_streak"),
            new EnderStreakEnchantment()
    );

    public static final Enchantment PHANTOM_HARVEST = Registry.register(
        Registries.ENCHANTMENT,
        new Identifier(EnderSteel.MOD_ID, "phantom_harvest"),
        new PhantomHarvestEnchantment()
    );

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