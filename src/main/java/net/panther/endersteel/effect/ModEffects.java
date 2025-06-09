package net.panther.endersteel.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.panther.endersteel.EnderSteel;

public class ModEffects {
    public static final RegistryKey<StatusEffect> GAZING_VOID_KEY = RegistryKey.of(RegistryKeys.STATUS_EFFECT,
        Identifier.of(EnderSteel.MOD_ID, "gazing_void"));
    public static final RegistryEntry<StatusEffect> GAZING_VOID = RegistryEntry.of(new GazingVoidEffect());

    public static void registerModEffects() {
        Registry.register(Registries.STATUS_EFFECT, GAZING_VOID_KEY, GAZING_VOID.value());
        EnderSteel.LOGGER.info("Registering status effects for " + EnderSteel.MOD_ID);
    }

    private static class GazingVoidEffect extends StatusEffect {
        private final Random random = Random.create();

        public GazingVoidEffect() {
            super(StatusEffectCategory.HARMFUL, 0x400C40);
        }

        @Override
        public boolean canApplyUpdateEffect(int duration, int amplifier) {
            return duration % 5 == 0;
        }

        @Override
        public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
            if (!entity.getWorld().isClient && entity.getWorld() instanceof ServerWorld serverWorld) {
                double shakeX = (random.nextDouble() - 1) * 0.2;
                double shakeZ = (random.nextDouble() - 1) * 0.2;
                entity.addVelocity(shakeX, 0, shakeZ);
                entity.velocityModified = true;

                for (int i = 0; i < 2; i++) {
                    double x = entity.getX() + (random.nextDouble() - 0.5) * 1.5;
                    double y = entity.getY() + random.nextDouble() * 2;
                    double z = entity.getZ() + (random.nextDouble() - 0.5) * 1.5;
                    
                    serverWorld.spawnParticles(ParticleTypes.PORTAL, x, y, z, 1, 0, 0, 0, 0.1);
                }

                return true;
            }
            return false;
        }
    }
}
