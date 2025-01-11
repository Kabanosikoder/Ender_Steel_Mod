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
import net.panther.endersteel.item.custom.EnderSteelArmorItem;

import java.util.List;

public class RepulsiveShriekEnchantment extends Enchantment {
    private static final double NORMAL_RADIUS = 5.0;
    private static final double LAST_CHARGE_RADIUS = 10.0;
    private static final double KNOCKBACK_STRENGTH = 1.5;
    private static final float DAMAGE_REFLECTION_RATIO = 0.5f;
    private static final float LAST_CHARGE_DAMAGE = 4.0f;

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
            
            for (Entity entity : nearbyEntities) {
                if (entity instanceof LivingEntity livingEntity) {
                    Vec3d entityPos = entity.getPos();
                    Vec3d pushDirection = entityPos.subtract(playerPos).normalize();
                    
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
                    
                    double knockbackMultiplier = isLastCharge ? 1.5 : 1.0;
                    entity.setVelocity(
                        pushDirection.x * KNOCKBACK_STRENGTH * knockbackMultiplier,
                        0.4,
                        pushDirection.z * KNOCKBACK_STRENGTH * knockbackMultiplier
                    );
                    entity.velocityModified = true;

                    if (isLastCharge) {
                        float distance = (float) entityPos.distanceTo(playerPos);
                        float damageMultiplier = 1.0f - (distance / (float) LAST_CHARGE_RADIUS);
                        if (damageMultiplier > 0) {
                            livingEntity.damage(player.getDamageSources().magic(), LAST_CHARGE_DAMAGE * damageMultiplier);
                            
                            Box chainBox = livingEntity.getBoundingBox().expand(3.0);
                            List<Entity> chainEntities = livingEntity.getWorld().getOtherEntities(livingEntity, chainBox);
                            for (Entity chainEntity : chainEntities) {
                                if (chainEntity instanceof LivingEntity chainTarget && chainEntity != player) {
                                    float chainDistance = (float) chainEntity.getPos().distanceTo(entityPos);
                                    float chainMultiplier = 1.0f - (chainDistance / 3.0f);
                                    if (chainMultiplier > 0) {
                                        chainTarget.damage(player.getDamageSources().magic(), LAST_CHARGE_DAMAGE * damageMultiplier * chainMultiplier * 0.5f);
                                        
                                        Vec3d chainPos = chainEntity.getPos();
                                        serverWorld.spawnParticles(
                                            ParticleTypes.SONIC_BOOM,
                                            chainPos.x, chainPos.y + 1.0, chainPos.z,
                                            1, 0, 0, 0, 0
                                        );
                                        serverWorld.spawnParticles(
                                            ParticleTypes.END_ROD,
                                            entityPos.x, entityPos.y + 1.0, entityPos.z,
                                            10, 0.3, 0.3, 0.3, 0.1
                                        );
                                        
                                        Vec3d chainDirection = chainPos.subtract(entityPos);
                                        double chainLength = chainDirection.length();
                                        Vec3d normalizedDirection = chainDirection.normalize();
                                        for (double d = 0.5; d < chainLength; d += 0.5) {
                                            Vec3d particlePos = entityPos.add(normalizedDirection.multiply(d));
                                            serverWorld.spawnParticles(
                                                ParticleTypes.PORTAL,
                                                particlePos.x, particlePos.y + 1.0, particlePos.z,
                                                1, 0, 0, 0, 0
                                            );
                                        }
                                    }
                                }
                            }
                            
                            player.getWorld().playSound(
                                null,
                                entityPos.x,
                                entityPos.y,
                                entityPos.z,
                                SoundEvents.ENTITY_WARDEN_SONIC_CHARGE,
                                SoundCategory.PLAYERS,
                                0.3f,
                                1.5f
                            );
                        }
                    }
                }
            }
            
            if (attacker instanceof LivingEntity livingAttacker) {
                float reflectedDamage = amount * (isLastCharge ? DAMAGE_REFLECTION_RATIO * 1.5f : DAMAGE_REFLECTION_RATIO);
                livingAttacker.damage(player.getDamageSources().magic(), reflectedDamage);
                
                serverWorld.spawnParticles(
                    ParticleTypes.SCULK_SOUL,
                    attacker.getX(), attacker.getY() + 1.0, attacker.getZ(),
                    isLastCharge ? 25 : 15, 0.5, 0.5, 0.5, 0.1
                );
                
                if (isLastCharge) {
                    serverWorld.spawnParticles(
                        ParticleTypes.END_ROD,
                        attacker.getX(), attacker.getY() + 1.0, attacker.getZ(),
                        20, 0.5, 0.5, 0.5, 0.1
                    );
                }
            }
            
            float pitch = isLastCharge ? 0.8f : 1.5f;
            player.getWorld().playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.ENTITY_WARDEN_SONIC_BOOM,
                SoundCategory.PLAYERS,
                isLastCharge ? 1.5f : 1.0f,
                pitch
            );

            if (isLastCharge) {
                player.getWorld().playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.ENTITY_WARDEN_ROAR,
                    SoundCategory.PLAYERS,
                    1.0f,
                    0.8f
                );
                
                player.getWorld().playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.ENTITY_WARDEN_DEATH,
                    SoundCategory.PLAYERS,
                    0.5f,
                    1.2f
                );
            }
        }
    }
}
