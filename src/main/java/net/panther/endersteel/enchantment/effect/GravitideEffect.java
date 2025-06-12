package net.panther.endersteel.enchantment.effect;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public record GravitideEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<GravitideEffect> CODEC = MapCodec.unit(GravitideEffect::new);

    // WeakyStar proposed the idea for this enchantment, thank you :3
    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {
        if (target instanceof LivingEntity victim && context.owner() instanceof PlayerEntity player) {

            Vec3d toPlayer = player.getPos().subtract(victim.getPos());
            
            double pullStrength = 0.6; 
            Vec3d pullVector = toPlayer.normalize().multiply(pullStrength);
            
            // Cancel any existing velocity and apply pull
            victim.setVelocity(pullVector);
            victim.velocityModified = true;

            // Spawn particles at the victim's position
            world.spawnParticles(
                ParticleTypes.PORTAL,
                victim.getX(),
                victim.getY() + victim.getHeight() * 0.5,
                victim.getZ(),
                5, 0.2, 0.2, 0.2, 0.1
            );

            if (victim.isDead()) {
                applyKillEffect(world, player, level);
            }
        }
    }

    private void applyKillEffect(ServerWorld world, PlayerEntity player, int level) {
        double radius = level == 1 ? 4.0 : 6.0;
        double pullStrength = level == 1 ? 0.7 : 0.9;

        world.playSound(
            null, 
            player.getX(), 
            player.getY(), 
            player.getZ(), 
            SoundEvents.ENTITY_ENDERMAN_TELEPORT, 
            SoundCategory.PLAYERS, 
            0.75f,
            0.25f
        );

        // Get all entities in radius
        Box box = new Box(
            player.getX() - radius, 
            player.getY() - radius, 
            player.getZ() - radius,
            player.getX() + radius, 
            player.getY() + radius, 
            player.getZ() + radius
        );

        // Apply pull effect to all entities
        for (Entity entity : world.getOtherEntities(player, box)) {
            if (entity instanceof LivingEntity) {

                Vec3d toPlayer = player.getPos().subtract(entity.getPos());
                double distance = toPlayer.length();
                double distanceFactor = 1.0 - (distance / radius); // Stronger pull when closer
                
                double strength = pullStrength * (0.5 + distanceFactor * 0.5);
                Vec3d pullVector = toPlayer.normalize().multiply(strength);
                
                entity.setVelocity(pullVector);
                entity.velocityModified = true;

                world.spawnParticles(
                    ParticleTypes.PORTAL,
                    entity.getX(), 
                    entity.getY() + entity.getHeight() * 0.5, 
                    entity.getZ(),
                    12,
                    0.3, 0.3, 0.3,
                    0.5
                );
            }
        }

        for (int i = 0; i < 20; i++) {
            double angle = i * Math.PI * 2 / 20;
            double x = player.getX() + Math.cos(angle) * radius * 0.8;
            double z = player.getZ() + Math.sin(angle) * radius * 0.8;
            world.spawnParticles(
                ParticleTypes.REVERSE_PORTAL,
                x, 
                player.getY() + 0.5, 
                z,
                1, 0, 0, 0, 0.1
            );
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
