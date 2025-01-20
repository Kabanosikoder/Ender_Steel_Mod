package net.panther.endersteel.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.panther.endersteel.config.ModConfig;
import net.panther.endersteel.item.custom.EnderSteelArmorItem;

import java.util.List;

public class RepulsiveShriekEnchantment extends Enchantment {
    private static final double NORMAL_RADIUS = 5.0;
    private static final double LAST_CHARGE_RADIUS = 10.0;

    public RepulsiveShriekEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentTarget.ARMOR_CHEST, new EquipmentSlot[]{EquipmentSlot.CHEST});
    }

    @Override
    public int getMinPower(int level) {
        return 25;
    }

    @Override
    public int getMaxPower(int level) {
        return 50;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof EnderSteelArmorItem;
    }

    public void onPlayerDamaged(PlayerEntity player, Entity attacker, float amount, boolean isLastCharge) {
        if (!player.getWorld().isClient && player.getWorld() instanceof ServerWorld serverWorld) {
            double effectRadius = isLastCharge ? LAST_CHARGE_RADIUS : NORMAL_RADIUS;
            
            Box box = player.getBoundingBox().expand(effectRadius);
            List<Entity> nearbyEntities = player.getWorld().getOtherEntities(player, box);
            
            Vec3d playerPos = player.getPos();
            Random random = player.getRandom();
            
            // Visual effects for the shriek
            for (int i = 0; i < 360; i += isLastCharge ? 5 : 10) {
                double angle = Math.toRadians(i);
                double x = playerPos.x + Math.cos(angle) * effectRadius;
                double z = playerPos.z + Math.sin(angle) * effectRadius;
                
                serverWorld.spawnParticles(
                    ParticleTypes.SONIC_BOOM,
                    x, playerPos.y + 1.0, z,
                    1, 0, 0, 0, 0
                );
                
                if (isLastCharge || random.nextFloat() < 0.3f) {
                    serverWorld.spawnParticles(
                        ParticleTypes.SCULK_SOUL,
                        x, playerPos.y + random.nextFloat() * 2.0, z,
                        isLastCharge ? 2 : 1, 0, 0.2, 0, 0.05
                    );
                }
            }
            
            // Apply knockback and damage to nearby entities
            for (Entity entity : nearbyEntities) {
                if (entity instanceof LivingEntity livingEntity) {
                    Vec3d entityPos = entity.getPos();
                    Vec3d pushDirection = entityPos.subtract(playerPos).normalize();
                    
                    // Visual effects for each affected entity
                    for (double d = 0.5; d < entityPos.distanceTo(playerPos); d += 0.5) {
                        Vec3d particlePos = playerPos.add(pushDirection.multiply(d));
                        serverWorld.spawnParticles(
                            ParticleTypes.SONIC_BOOM,
                            particlePos.x, particlePos.y + 1.0, particlePos.z,
                            1, 0, 0, 0, 0
                        );
                        if (random.nextFloat() < (isLastCharge ? 0.5 : 0.3)) {
                            serverWorld.spawnParticles(
                                ParticleTypes.PORTAL,
                                particlePos.x, particlePos.y + 1.0, particlePos.z,
                                2, 0, 0, 0, 0.5
                            );
                        }
                    }
                    
                    // Apply knockback
                    double knockbackMultiplier = isLastCharge ? 1.5 : 1.0;
                    entity.setVelocity(
                        pushDirection.x * ModConfig.REPULSIVE_SHRIEK_KNOCKBACK * knockbackMultiplier,
                        0.4,
                        pushDirection.z * ModConfig.REPULSIVE_SHRIEK_KNOCKBACK * knockbackMultiplier
                    );
                    entity.velocityModified = true;

                    // Apply damage on last charge
                    if (isLastCharge) {
                        float distance = (float) entityPos.distanceTo(playerPos);
                        float damageMultiplier = 1.0f - (distance / (float) LAST_CHARGE_RADIUS);
                        if (damageMultiplier > 0) {
                            livingEntity.damage(player.getDamageSources().magic(), amount * ModConfig.REPULSIVE_SHRIEK_DAMAGE_REFLECTION * damageMultiplier);
                        }
                    }
                }
            }
            
            // Play sound effect
            float pitch = isLastCharge ? 0.8f : 1.5f;
            player.getWorld().playSound(
                null,
                playerPos.x,
                playerPos.y,
                playerPos.z,
                SoundEvents.ENTITY_WARDEN_SONIC_BOOM,
                SoundCategory.PLAYERS,
                1.0f,
                pitch
            );
        }
    }
}
