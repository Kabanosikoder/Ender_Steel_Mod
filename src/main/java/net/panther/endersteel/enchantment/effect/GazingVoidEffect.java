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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.panther.endersteel.effect.ModEffects;
import net.panther.endersteel.item.custom.EnderSteelScytheItem;

public record GazingVoidEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<GazingVoidEffect> CODEC = MapCodec.unit(GazingVoidEffect::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {
        if (target instanceof LivingEntity victim && 
            context.owner() instanceof PlayerEntity && 
            context.stack() != null && 
            context.stack().getItem() instanceof EnderSteelScytheItem && 
            EnderSteelScytheItem.isVoidGazeActive(context.stack())) {
            
            int duration = 60 + (level * 20); // Base 3 seconds + 1 second per level
            victim.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, duration, 0));
            victim.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, duration, 1));
            victim.addStatusEffect(new StatusEffectInstance(ModEffects.GAZING_VOID, duration, 0));
            
            // Deactivate after successful hit
            EnderSteelScytheItem.setVoidGazeActive(context.stack(), false);
            
            // Play sound effect
            world.playSound(null, target.getX(), target.getY(), target.getZ(),
                SoundEvents.ENTITY_ENDERMAN_SCREAM, SoundCategory.PLAYERS, 1.0F, 0.5F);
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
