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

    public static final Enchantment ENDERS_EDGE = Registry.register(
            Registries.ENCHANTMENT,
            new Identifier(EnderSteel.MOD_ID, "enders_edge"),
            new EndersEdgeEnchantment()
    );

    public static void registerModEnchantments() {
        EnderSteel.LOGGER.info("Registering Enchantments for " + EnderSteel.MOD_ID);
    }
}
