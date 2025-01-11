package net.panther.endersteel.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;
import net.panther.endersteel.item.custom.EnderSteelArmorItem;
import net.panther.endersteel.util.TeleportUtil;
import net.panther.endersteel.advancement.ModCriteria;
import net.panther.endersteel.enchantment.ModEnchantments;
import net.panther.endersteel.enchantment.RepulsiveShriekEnchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EnderSteelArmorEvents {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnderSteelArmorEvents.class);
    private static final String EVASION_CHARGES_KEY = "evasion_charges";
    private static final String EVASION_COOLDOWN_KEY = "evasion_cooldown";
    private static final int MAX_CHARGES = 5;
    private static final int CHARGE_COOLDOWN_TICKS = 480;
    private static final float EVASION_CHANCE = 0.40f;
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
        UNDODGEABLE_DAMAGE.add(DamageTypes.MAGIC);            // Direct magic damage
        UNDODGEABLE_DAMAGE.add(DamageTypes.STALAGMITE);       // Falling on stalagmites
        UNDODGEABLE_DAMAGE.add(DamageTypes.SWEET_BERRY_BUSH); // Sweet berry bush
        UNDODGEABLE_DAMAGE.add(DamageTypes.THROWN);           // Thrown projectiles (eggs, snowballs)
    }
    
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

    public static void updateCharges(PlayerEntity player) {
        ItemStack chestplate = player.getInventory().getArmorStack(2);
        if (!chestplate.isEmpty() && chestplate.getItem() instanceof EnderSteelArmorItem armorItem) {
            int currentCharges = armorItem.getCharges(chestplate);
            
            if (currentCharges == 0) {
                NbtCompound nbt = chestplate.getOrCreateNbt();
                int cooldown = nbt.getInt(EVASION_COOLDOWN_KEY);
                if (cooldown > 0) {
                    nbt.putInt(EVASION_COOLDOWN_KEY, cooldown - 1);

                    if (cooldown == 1) {
                        armorItem.setCharges(chestplate, MAX_CHARGES);
                        
                        float pitch = 1.5f; // High pitch for full recharge
                        player.getWorld().playSound(
                            null, 
                            player.getX(), 
                            player.getY(), 
                            player.getZ(),
                            SoundEvents.BLOCK_END_PORTAL_FRAME_FILL,
                            SoundCategory.PLAYERS,
                            0.3f,
                            pitch
                        );
                    }
                } else if (cooldown == 0) {
                    nbt.putInt(EVASION_COOLDOWN_KEY, CHARGE_COOLDOWN_TICKS * MAX_CHARGES);
                }
            }
        }
    }

    public static boolean handleDamage(PlayerEntity player, DamageSource source, float amount) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return false;
        }

        if (!isWearingFullEnderSteelArmor(player)) {
            return false;
        }

        if (UNDODGEABLE_DAMAGE.contains(source.getTypeRegistryEntry().getKey().orElse(null))) {
            return false;
        }

        if (player.isBlocking()) {
            return false;
        }

        // 40% chance to try evading for both abilities
        if (random.nextFloat() >= EVASION_CHANCE) {
            return false;
        }

        ItemStack chestplate = player.getInventory().getArmorStack(2);
        int shriekLevel = EnchantmentHelper.getLevel(ModEnchantments.REPULSIVE_SHRIEK, chestplate);
        if (shriekLevel > 0) {
            if (chestplate.getItem() instanceof EnderSteelArmorItem armorItem) {
                int chargesBefore = armorItem.getCharges(chestplate);
                
                if (tryUseCharge(player)) {
                    boolean isLastCharge = (chargesBefore == 1);
                    
                    RepulsiveShriekEnchantment shriek = (RepulsiveShriekEnchantment) ModEnchantments.REPULSIVE_SHRIEK;
                    shriek.onPlayerDamaged(player, source.getAttacker(), amount, isLastCharge);
                    return true;
                }
            }
            return false;
        }

        // Default teleport behavior
        if (!tryUseCharge(player)) {
            return false;
        }

        boolean teleported = TeleportUtil.teleportRandomly(player, 5.0);
        if (teleported) {
            ModCriteria.ENDER_STEEL_TELEPORT.trigger(serverPlayer);
        }
        return teleported;
    }

    private static boolean tryUseCharge(PlayerEntity player) {
        ItemStack chestplate = player.getInventory().getArmorStack(2);
        if (!chestplate.isEmpty() && chestplate.getItem() instanceof EnderSteelArmorItem armorItem) {
            int currentCharges = armorItem.getCharges(chestplate);
            if (currentCharges > 0) {
                armorItem.setCharges(chestplate, currentCharges - 1);
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