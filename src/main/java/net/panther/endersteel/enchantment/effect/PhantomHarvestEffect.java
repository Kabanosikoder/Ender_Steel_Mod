package net.panther.endersteel.enchantment.effect;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.panther.endersteel.item.custom.EnderSteelArmorItem;
import net.panther.endersteel.config.ModConfig;

public record PhantomHarvestEffect() implements EnchantmentEntityEffect {
    private static final String EVASION_CHARGES_KEY = "evasion_charges";
    public static final MapCodec<PhantomHarvestEffect> CODEC = MapCodec.unit(PhantomHarvestEffect::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {
        if (target instanceof LivingEntity victim && !victim.isAlive() && context.owner() instanceof PlayerEntity player) {
            // Check if wearing full Ender Steel armor
            boolean hasFullSet = true;
            for (ItemStack armorPiece : player.getArmorItems()) {
                if (!(armorPiece.getItem() instanceof EnderSteelArmorItem)) {
                    hasFullSet = false;
                    break;
                }
            }

            if (hasFullSet) {
                // Get chestplate and add charge
                ItemStack chestplate = player.getInventory().getArmorStack(2);
                if (!chestplate.isEmpty() && chestplate.getItem() instanceof EnderSteelArmorItem) {
                    NbtComponent nbtComponent = chestplate.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
                    int currentCharges = nbtComponent.getNbt().getInt(EVASION_CHARGES_KEY);
                    int chargesToAdd = 1;
                    
                    // Update charges
                    NbtComponent.set(DataComponentTypes.CUSTOM_DATA, chestplate, nbt -> 
                        nbt.putInt(EVASION_CHARGES_KEY, Math.min(currentCharges + chargesToAdd, ModConfig.MAX_EVASION_CHARGES))
                    );
                    
                    world.playSound(null, pos.getX(), pos.getY(), pos.getZ(),
                            SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    world.spawnParticles(ParticleTypes.PORTAL,
                            victim.getX(), victim.getY() + 1, victim.getZ(),
                            20, 0.5, 0.5, 0.5, 0.1);
                }
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
