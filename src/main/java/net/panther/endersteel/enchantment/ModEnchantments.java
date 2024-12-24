package net.panther.endersteel.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;

public class ModEnchantments {
    public static final Enchantment GAZING_VOID = register("gazing_void",
            new GazingVoidEnchantment());

    private static Enchantment register(String name, Enchantment enchantment) {
        return Registry.register(Registries.ENCHANTMENT, new Identifier(EnderSteel.MOD_ID, name), enchantment);
    }

    public static void registerModEnchantments() {
        EnderSteel.LOGGER.info("Registering Mod Enchantments for " + EnderSteel.MOD_ID);
    }
}
