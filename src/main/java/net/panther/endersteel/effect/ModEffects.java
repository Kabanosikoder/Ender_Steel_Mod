package net.panther.endersteel.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import net.panther.endersteel.EnderSteel;

public class ModEffects {
    public static final StatusEffect GAZING_VOID = new GazingVoidEffect();

    public static void registerEffects() {
        Registry.register(
            Registries.STATUS_EFFECT,
            new Identifier(EnderSteel.MOD_ID, "gazing_void"),
            GAZING_VOID
        );
    }

    private static class GazingVoidEffect extends StatusEffect {
        private final Random random = Random.create();

        public GazingVoidEffect() {
            super(StatusEffectCategory.HARMFUL, 0x400C40); // Dark purple color
        }

        @Override
        public boolean canApplyUpdateEffect(int duration, int amplifier) {
            return true;
        }

        @Override
        public void applyUpdateEffect(LivingEntity entity, int amplifier) {
            if (!entity.getWorld().isClient && entity.getWorld() instanceof ServerWorld serverWorld) {
                // Add random shaking
                double shakeX = (random.nextDouble() - 0.5) * 0.2;
                double shakeZ = (random.nextDouble() - 0.5) * 0.2;
                entity.addVelocity(shakeX, 0, shakeZ);
                entity.velocityModified = true;

                // Spawn particles
                for (int i = 0; i < 2; i++) {
                    double x = entity.getX() + (random.nextDouble() - 0.5) * 1.5;
                    double y = entity.getY() + random.nextDouble() * 2;
                    double z = entity.getZ() + (random.nextDouble() - 0.5) * 1.5;
                    
                    serverWorld.spawnParticles(
                        ParticleTypes.PORTAL,
                        x, y, z,
                        1, // particle count
                        0, 0, 0, // velocity
                        0.1 // speed
                    );
                }
            }
        }
    }
}
