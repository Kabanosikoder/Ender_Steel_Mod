package net.panther.endersteel.enchantment.effect;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record EnderStreakEffect() implements EnchantmentEntityEffect {

    public static final MapCodec<EnderStreakEffect> CODEC = MapCodec.unit(EnderStreakEffect::new);

    private static final Map<UUID, StreakData> playerStreaks = new HashMap<>();
    private static final int STREAK_TIMEOUT_TICKS = 60; // 3 seconds (1 tick = 1/20th of a second)
    
    private record StreakData(int streak, long lastHitTime) {}

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {
        if (target instanceof LivingEntity victim && context.owner() instanceof PlayerEntity player) {

            UUID playerId = player.getUuid();
            long currentTime = world.getTime();
            StreakData currentStreak = playerStreaks.getOrDefault(playerId, new StreakData(0, currentTime));
            
            // Check if streak timed out
            int newStreak;
            if (currentTime - currentStreak.lastHitTime() > STREAK_TIMEOUT_TICKS) {
                newStreak = 1; // Reset streak
            } else {
                newStreak = currentStreak.streak() + 1;
            }
            
            // Update streak data
            playerStreaks.put(playerId, new StreakData(newStreak, currentTime));
            
            // Calculate and apply bonus damage based on streak
            float damagePerStreak = 2;
            float bonusDamage = damagePerStreak * (newStreak - 1); // -1 so first hit has no bonus
            
            if (bonusDamage > 0) {
                victim.damage(world.getDamageSources().playerAttack(player), bonusDamage);
            }
            
            // Visual feedback - more particles with higher streaks
            int particleCount = Math.min(5 * newStreak, 30);
            world.spawnParticles(ParticleTypes.PORTAL, 
                victim.getX(), victim.getY() + 1, victim.getZ(),
                particleCount, 0.2, 0.2, 0.2, 0.1);
            
            float pitch = Math.min(1.0f + (newStreak * 0.1f), 2.0f);
            world.playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 0.5f, pitch);
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
