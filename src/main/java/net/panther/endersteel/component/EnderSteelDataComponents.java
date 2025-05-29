package net.panther.endersteel.component;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;

import java.util.function.UnaryOperator;

public class EnderSteelDataComponents {
    public static final ComponentType<Integer> STORED_PEARLS = register("stored_pearls", 
        builder -> builder.codec(Codec.INT));

    public static final ComponentType<Integer> TELEPORT_STREAK = register("teleport_streak", 
        builder -> builder.codec(Codec.INT));

    public static final ComponentType<Boolean> VOID_GAZE_ACTIVE = register("void_gaze_active", 
        builder -> builder.codec(Codec.BOOL));

    public static final ComponentType<Integer> EVASION_CHARGES = register("evasion_charges",
        builder -> builder.codec(Codec.INT));
        
    public static final ComponentType<Integer> VOID_MACE_SOCKETS = register("void_mace_sockets",
        builder -> builder.codec(Codec.INT));
        
    public static final ComponentType<String> SOCKET_TYPE = register("socket_type",
        builder -> builder.codec(Codec.STRING));

    public static <T> ComponentType<T> register(String name, UnaryOperator<ComponentType.Builder<T>> builderOperator) {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Identifier.of(EnderSteel.MOD_ID, name),
            builderOperator.apply(ComponentType.builder()).build());
    }

    public static void registerDataComponents() {
        EnderSteel.LOGGER.info("Registering Data Components for " + EnderSteel.MOD_ID);
    }
}
