package net.panther.endersteel.enchantment.effect;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.panther.endersteel.effect.ModEffects;

public record GazingVoidEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<GazingVoidEffect> CODEC = MapCodec.unit(GazingVoidEffect::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {
        if (target instanceof LivingEntity victim && context.owner() instanceof PlayerEntity player) {
            victim.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 5, 0));
            victim.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 6, 1));
            victim.addStatusEffect(new StatusEffectInstance(ModEffects.GAZING_VOID, 7,  0));
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
