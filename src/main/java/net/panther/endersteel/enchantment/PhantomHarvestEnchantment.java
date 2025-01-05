package net.panther.endersteel.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.panther.endersteel.event.EnderSteelArmorEvents;
import net.panther.endersteel.item.custom.EnderSteelArmorItem;
import net.panther.endersteel.item.custom.EnderSteelScytheItem;

public class PhantomHarvestEnchantment extends Enchantment {
    private Entity lastKilledEntity = null;

    public PhantomHarvestEnchantment() {
        super(Rarity.RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinPower(int level) {
        return 15;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof EnderSteelScytheItem;
    }

    @Override
    public boolean canAccept(Enchantment other) {
        return super.canAccept(other) && !(other instanceof GazingVoidEnchantment);
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (!target.isAlive() && target instanceof LivingEntity livingTarget && user instanceof PlayerEntity player) {
            if (target == lastKilledEntity) {
                return;
            }
            lastKilledEntity = target;

            // Check if wearing full Ender Steel armor
            boolean hasFullSet = true;
            for (ItemStack armorPiece : player.getArmorItems()) {
                if (!(armorPiece.getItem() instanceof EnderSteelArmorItem)) {
                    hasFullSet = false;
                    break;
                }
            }

            if (hasFullSet) {
                // Add 1 charge on kill
                ItemStack chestplate = player.getInventory().getArmorStack(2);
                if (!chestplate.isEmpty() && chestplate.getItem() instanceof EnderSteelArmorItem) {
                    NbtCompound nbt = chestplate.getOrCreateNbt();
                    int currentCharges = nbt.getInt("evasion_charges");
                    if (currentCharges < 5) {
                        nbt.putInt("evasion_charges", currentCharges + 1);
                        // Play recharge sound
                        player.getWorld().playSound(
                            null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundEvents.BLOCK_RESPAWN_ANCHOR_CHARGE,
                            SoundCategory.PLAYERS,
                            0.5f,
                            1.2f
                        );
                        
                        if (!player.getWorld().isClient()) {
                            ServerWorld serverWorld = (ServerWorld) player.getWorld();
                            Random random = Random.create();
                            for (int i = 0; i < 15; i++) {
                                double offsetX = (random.nextDouble() - 0.5) * 2.0;
                                double offsetY = random.nextDouble() * 2.0;
                                double offsetZ = (random.nextDouble() - 0.5) * 2.0;
                                serverWorld.spawnParticles(
                                    ParticleTypes.PORTAL,
                                    player.getX() + offsetX,
                                    player.getY() + offsetY,
                                    player.getZ() + offsetZ,
                                    1, 0, 0, 0, 0
                                );
                            }
                        }
                    }
                }
            } else {
                // Heal player by 1 heart on kill
                player.heal(1.0f);

                player.getWorld().playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.ITEM_HONEY_BOTTLE_DRINK,
                    SoundCategory.PLAYERS,
                    0.5f,
                    1.0f
                );
            }
        }
        super.onTargetDamaged(user, target, level);
    }
}
