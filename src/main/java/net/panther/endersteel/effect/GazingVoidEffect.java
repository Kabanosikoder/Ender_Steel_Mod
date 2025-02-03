package net.panther.endersteel.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;

public class GazingVoidEffect extends StatusEffect {
    public static final StatusEffect GAZING_VOID = new GazingVoidEffect();

    protected GazingVoidEffect() {
        super(StatusEffectCategory.HARMFUL, 0x1F1F1F);
    }

    public static void register() {
        Registry.register(Registries.STATUS_EFFECT,
             Identifier.of(EnderSteel.MOD_ID, "gazing_void"),
            GAZING_VOID);
    }
}
