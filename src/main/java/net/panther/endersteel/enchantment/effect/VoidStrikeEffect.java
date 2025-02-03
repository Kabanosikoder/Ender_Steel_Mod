package net.panther.endersteel.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.panther.endersteel.util.TeleportUtil;

public record VoidStrikeEffect(net.minecraft.enchantment.EnchantmentLevelBasedValue enchantmentLevelBasedValue) implements EnchantmentEntityEffect {
    public static final MapCodec<VoidStrikeEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.point(new VoidStrikeEffect(new EnchantmentLevelBasedValue() {
                @Override
                public float getValue(int level) {
                    return 0;
                }

                @Override
                public MapCodec<? extends EnchantmentLevelBasedValue> getCodec() {
                    return null;
                }
            }))
    );

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {
        if (target instanceof LivingEntity victim && context.owner() instanceof PlayerEntity) {
            // 50% chance to teleport on hit
            if (world.getRandom().nextFloat() < 0.5f) {
                // Attempt to teleport the entity within 16 blocks
                TeleportUtil.teleportRandomly(victim, 16.0);
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
