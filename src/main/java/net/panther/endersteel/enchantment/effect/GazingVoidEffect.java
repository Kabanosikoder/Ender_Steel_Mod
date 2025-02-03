package net.panther.endersteel.enchantment.effect;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.panther.endersteel.effect.ModEffects;

public record GazingVoidEffect(EnchantmentLevelBasedValue duration) implements EnchantmentEntityEffect {
    public static final MapCodec<GazingVoidEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    EnchantmentLevelBasedValue.CODEC.fieldOf("duration").forGetter(GazingVoidEffect::duration)
            ).apply(instance, GazingVoidEffect::new)
    );

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {
        if (target instanceof LivingEntity victim && context.owner() instanceof PlayerEntity player) {
            int durationTicks = (int) (this.duration.getValue(level) * 20); // Convert seconds to ticks

            victim.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, durationTicks, 0));
            victim.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, durationTicks, 1));
            victim.addStatusEffect(new StatusEffectInstance(ModEffects.GAZING_VOID, durationTicks,  0));
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
