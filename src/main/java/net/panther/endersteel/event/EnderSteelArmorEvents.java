package net.panther.endersteel.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.panther.endersteel.item.custom.EnderSteelArmorItem;
import net.panther.endersteel.util.TeleportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnderSteelArmorEvents {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnderSteelArmorEvents.class);
    private static final String EVASION_CHARGES_KEY = "evasion_charges";
    private static final String EVASION_COOLDOWN_KEY = "evasion_cooldown";
    private static final int MAX_CHARGES = 5;
    private static final int CHARGE_COOLDOWN_TICKS = 280; // 14 seconds per charge (70/5 seconds)
    
    public static void register() {
        LOGGER.info("Registering Ender Steel Armor Events");
        
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                if (isWearingFullEnderSteelArmor(player)) {
                    updateCharges(player);
                }
            }
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            PlayerEntity player = handler.getPlayer();
            if (isWearingFullEnderSteelArmor(player)) {
                ItemStack chestplate = player.getInventory().getArmorStack(2);
                NbtCompound nbt = chestplate.getOrCreateNbt();
                if (!nbt.contains(EVASION_CHARGES_KEY)) {
                    nbt.putInt(EVASION_CHARGES_KEY, MAX_CHARGES);
                }
            }
        });
    }

    public static boolean handleDamage(PlayerEntity player) {
        if (!(player instanceof ServerPlayerEntity)) {
            return false;
        }
        
        if (!isWearingFullEnderSteelArmor(player)) {
            return false;
        }
        
        if (!tryUseCharge(player)) {
            return false;
        }

        if (TeleportUtil.teleportRandomly(player, 5.0)) {
            return true;
        }
        return false;
    }

    private static void updateCharges(PlayerEntity player) {
        ItemStack chestplate = player.getInventory().getArmorStack(2);
        if (!chestplate.isEmpty() && chestplate.getItem() instanceof EnderSteelArmorItem) {
            NbtCompound nbt = chestplate.getOrCreateNbt();
            int currentCharges = nbt.getInt(EVASION_CHARGES_KEY);
            
            if (currentCharges < MAX_CHARGES) {
                int cooldown = nbt.getInt(EVASION_COOLDOWN_KEY);
                if (cooldown > 0) {
                    nbt.putInt(EVASION_COOLDOWN_KEY, cooldown - 1);
                    if (cooldown == 1) {
                        // Add one charge and start cooldown for next charge if needed
                        currentCharges++;
                        nbt.putInt(EVASION_CHARGES_KEY, currentCharges);
                        if (currentCharges < MAX_CHARGES) {
                            nbt.putInt(EVASION_COOLDOWN_KEY, CHARGE_COOLDOWN_TICKS);
                        }
                        
                        // Play charge restored sound with increasing pitch
                        float pitch = 0.8f + (currentCharges * 0.2f); // Pitch increases with each charge
                        player.getWorld().playSound(
                            null, 
                            player.getX(), 
                            player.getY(), 
                            player.getZ(),
                            SoundEvents.BLOCK_END_PORTAL_FRAME_FILL,
                            SoundCategory.PLAYERS,
                            0.3f, // Lower volume to be less intrusive
                            pitch
                        );
                    }
                }
            }
        }
    }

    private static boolean tryUseCharge(PlayerEntity player) {
        ItemStack chestplate = player.getInventory().getArmorStack(2);
        if (!chestplate.isEmpty() && chestplate.getItem() instanceof EnderSteelArmorItem) {
            NbtCompound nbt = chestplate.getOrCreateNbt();
            int charges = nbt.getInt(EVASION_CHARGES_KEY);
            
            if (charges > 0) {
                charges--;
                nbt.putInt(EVASION_CHARGES_KEY, charges);
                if (charges < MAX_CHARGES && nbt.getInt(EVASION_COOLDOWN_KEY) == 0) {
                    // Start cooldown for next charge if we're not already recharging
                    nbt.putInt(EVASION_COOLDOWN_KEY, CHARGE_COOLDOWN_TICKS);
                }
                return true;
            }
        }
        return false;
    }

    private static boolean isWearingFullEnderSteelArmor(PlayerEntity player) {
        for (ItemStack stack : player.getArmorItems()) {
            if (!(stack.getItem() instanceof EnderSteelArmorItem)) {
                return false;
            }
        }
        return true;
    }
}
