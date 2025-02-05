package net.panther.endersteel.enchantment.effect;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.panther.endersteel.util.TeleportUtil;

public record VoidStrikeEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<VoidStrikeEffect> CODEC = MapCodec.unit(VoidStrikeEffect::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {
        if (target instanceof LivingEntity victim && context.owner() instanceof PlayerEntity) {
            // 50% chance to teleport on hit
            if (world.getRandom().nextFloat() < 0.5f) {
                // Teleport the entity within the radius
                TeleportUtil.teleportRandomly(victim, 5.0);
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
