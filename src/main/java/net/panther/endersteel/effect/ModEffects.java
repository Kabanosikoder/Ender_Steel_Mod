package net.panther.endersteel.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.panther.endersteel.EnderSteel;

public class ModEffects {
    public static final StatusEffect GAZING_VOID = new GazingVoidEffect();

    public static void registerEffects() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(EnderSteel.MOD_ID, "gazing_void"), GAZING_VOID);
        EnderSteel.LOGGER.info("Registering Effects for " + EnderSteel.MOD_ID);
    }

    private static class GazingVoidEffect extends StatusEffect {
        public GazingVoidEffect() {
            super(StatusEffectCategory.HARMFUL, 0x1F1F1F);
        }
    }
}
