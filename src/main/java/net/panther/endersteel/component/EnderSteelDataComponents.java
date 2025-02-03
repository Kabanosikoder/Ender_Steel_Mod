package net.panther.endersteel.component;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;

public class EnderSteelDataComponents {
    public static final ComponentType<Integer> STORED_PEARLS = Registry.register(
        Registries.DATA_COMPONENT_TYPE, Identifier.of(EnderSteel.MOD_ID, "stored_pearls"),
        ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static final ComponentType<Integer> TELEPORT_STREAK = Registry.register(
        Registries.DATA_COMPONENT_TYPE, Identifier.of(EnderSteel.MOD_ID, "teleport_streak"),
        ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static void register() {
        // Register components
    }
}
