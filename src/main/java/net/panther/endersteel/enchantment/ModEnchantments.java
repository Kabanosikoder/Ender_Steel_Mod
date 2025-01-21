package net.panther.endersteel.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;

public class ModEnchantments {
    public static final RegistryKey<Enchantment> GAZING_VOID = of("gazing_void");
    public static final RegistryKey<Enchantment> VOID_STRIKE = of("void_strike");
    public static final RegistryKey<Enchantment> ENDER_STREAK = of("ender_streak");
    public static final RegistryKey<Enchantment> PHANTOM_HARVEST = of("phantom_harvest");
    public static final RegistryKey<Enchantment> REPULSIVE_SHRIEK = of("repulsive_shriek");

    private static RegistryKey<Enchantment> of(String path) {
        Identifier id = Identifier.of(EnderSteel.MOD_ID, path);
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, id);
    }

    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment enchantment) {
        registry.register(key, enchantment);
    }

    public static void registerModEnchantments(Registerable<Enchantment> registry) {
        register(registry, GAZING_VOID, new GazingVoidEnchantment());
        register(registry, VOID_STRIKE, new VoidStrikeEnchantment());
        register(registry, ENDER_STREAK, new EnderStreakEnchantment());
        register(registry, PHANTOM_HARVEST, new PhantomHarvestEnchantment());
        register(registry, REPULSIVE_SHRIEK, new RepulsiveShriekEnchantment());
        
        EnderSteel.LOGGER.info("Registering enchantments for " + EnderSteel.MOD_ID);
    }
}