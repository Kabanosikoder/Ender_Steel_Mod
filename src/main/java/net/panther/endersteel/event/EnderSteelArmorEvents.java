package net.panther.endersteel.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.panther.endersteel.advancement.ModCriteria;
import net.panther.endersteel.config.ModConfig;
import net.panther.endersteel.enchantment.ModEnchantments;
import net.panther.endersteel.enchantment.RepulsiveShriekEnchantment;
import net.panther.endersteel.item.custom.EnderSteelArmorItem;
import net.panther.endersteel.util.TeleportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EnderSteelArmorEvents {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnderSteelArmorEvents.class);
    private static final String EVASION_CHARGES_KEY = "evasion_charges";
    private static final String EVASION_COOLDOWN_KEY = "evasion_cooldown";
    private static final Random random = new Random();
    
    private static final Set<RegistryKey<DamageType>> UNDODGEABLE_DAMAGE = new HashSet<>();
    static {
        UNDODGEABLE_DAMAGE.add(DamageTypes.IN_WALL);          // Suffocation
        UNDODGEABLE_DAMAGE.add(DamageTypes.FALL);             // Fall damage
        UNDODGEABLE_DAMAGE.add(DamageTypes.FLY_INTO_WALL);    // Kinetic (Elytra)
        UNDODGEABLE_DAMAGE.add(DamageTypes.GENERIC_KILL);     //  /kill
        UNDODGEABLE_DAMAGE.add(DamageTypes.WITHER);           // Wither effect
        UNDODGEABLE_DAMAGE.add(DamageTypes.STARVE);           // Starvation
        UNDODGEABLE_DAMAGE.add(DamageTypes.OUT_OF_WORLD);     // Void damage
        UNDODGEABLE_DAMAGE.add(DamageTypes.DRAGON_BREATH);    // Dragon breath
        UNDODGEABLE_DAMAGE.add(DamageTypes.INDIRECT_MAGIC);   // Poison/harming effects
        UNDODGEABLE_DAMAGE.add(DamageTypes.MAGIC);            // Magic damage
        UNDODGEABLE_DAMAGE.add(DamageTypes.STALAGMITE);       // Falling on stalagmites
        UNDODGEABLE_DAMAGE.add(DamageTypes.SWEET_BERRY_BUSH); // Sweet berry bush
        UNDODGEABLE_DAMAGE.add(DamageTypes.THROWN);           // Thrown projectiles (eggs, snowballs)
    }

    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            ItemStack chestplate = player.getInventory().getArmorStack(2);
            if (chestplate.getItem() instanceof EnderSteelArmorItem armorItem) {
                NbtCompound nbt = chestplate.getOrCreateNbt();
                if (!nbt.contains(EVASION_CHARGES_KEY)) {
                    nbt.putInt(EVASION_CHARGES_KEY, ModConfig.MAX_EVASION_CHARGES);
                }
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                ItemStack chestplate = player.getInventory().getArmorStack(2);
                if (!(chestplate.getItem() instanceof EnderSteelArmorItem armorItem)) {
                    continue;
                }

                NbtCompound nbt = chestplate.getOrCreateNbt();
                int cooldown = nbt.getInt(EVASION_COOLDOWN_KEY);
                if (cooldown > 0) {
                    nbt.putInt(EVASION_COOLDOWN_KEY, cooldown - 1);

                    if (cooldown == 1) {
                        armorItem.setCharges(chestplate, ModConfig.MAX_EVASION_CHARGES);
                        
                        float pitch = 1.5f; // High pitch for full recharge
                        player.getWorld().playSound(
                            null,
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundEvents.BLOCK_BEACON_POWER_SELECT,
                            SoundCategory.PLAYERS,
                            1.0f,
                            pitch
                        );
                    }
                } else if (cooldown == 0) {
                    nbt.putInt(EVASION_COOLDOWN_KEY, ModConfig.EVASION_COOLDOWN_SECONDS * 20); // Convert seconds to ticks
                }
            }
        });
    }

    public static boolean tryEvade(PlayerEntity player, DamageSource source) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return false;
        }

        ItemStack chestplate = player.getInventory().getArmorStack(2);
        if (!(chestplate.getItem() instanceof EnderSteelArmorItem armorItem)) {
            return false;
        }

        // Check if damage type is undodgeable
        if (source.getTypeRegistryEntry().getKey().isPresent() && 
            UNDODGEABLE_DAMAGE.contains(source.getTypeRegistryEntry().getKey().get())) {
            return false;
        }

        // 40% chance to try evading for both abilities
        if (random.nextFloat() >= ModConfig.EVASION_CHANCE) {
            return false;
        }

        // Check if we have charges
        NbtCompound nbt = chestplate.getOrCreateNbt();
        int charges = nbt.getInt(EVASION_CHARGES_KEY);
        if (charges <= 0) {
            return false;
        }

        // Decrement charges
        armorItem.setCharges(chestplate, charges - 1);

        // Play sound effect
        player.getWorld().playSound(
            null,
            player.getX(),
            player.getY(),
            player.getZ(),
            SoundEvents.ENTITY_ENDERMAN_TELEPORT,
            SoundCategory.PLAYERS,
            1.0f,
            1.0f
        );

        // Try to teleport
        if (!player.getWorld().isClient) {
            boolean teleported = TeleportUtil.teleportRandomly(player, 5.0);
            if (teleported) {
                ModCriteria.ENDER_STEEL_TELEPORT.trigger(serverPlayer);
            }
            return teleported;
        }

        return true;
    }

    // Add this new method to handle charge usage
    public static boolean useCharge(PlayerEntity player) {
        ItemStack chestplate = player.getInventory().getArmorStack(2);
        if (!(chestplate.getItem() instanceof EnderSteelArmorItem armorItem)) {
            return false;
        }

        NbtCompound nbt = chestplate.getOrCreateNbt();
        int charges = nbt.getInt(EVASION_CHARGES_KEY);
        if (charges <= 0) {
            return false;
        }

        // Decrement charges
        armorItem.setCharges(chestplate, charges - 1);
        return true;
    }
}