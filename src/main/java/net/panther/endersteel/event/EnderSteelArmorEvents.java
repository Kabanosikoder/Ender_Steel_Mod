package net.panther.endersteel.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.math.Vec3d;
import net.panther.endersteel.item.custom.EnderSteelArmorItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EnderSteelArmorEvents {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnderSteelArmorEvents.class);
    private static final String EVASION_CHARGES_KEY = "evasion_charges";
    private static final String EVASION_COOLDOWN_KEY = "evasion_cooldown";
    private static final int MAX_CHARGES = 5;
    private static final int CHARGE_COOLDOWN_TICKS = 2400; // 2 minutes
    private static final Random random = Random.create();

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
                    LOGGER.info("Initialized evasion charges for player: " + player.getName().getString());
                }
            }
        });
    }

    public static boolean handleDamage(PlayerEntity player) {
        LOGGER.info("Handling damage for player: " + player.getName().getString());
        
        if (!(player instanceof ServerPlayerEntity)) {
            LOGGER.info("Not a server player, skipping");
            return false;
        }
        
        if (!isWearingFullEnderSteelArmor(player)) {
            LOGGER.info("Not wearing full armor, skipping");
            return false;
        }
        
        if (!tryUseCharge(player)) {
            LOGGER.info("No charges available");
            return false;
        }

        LOGGER.info("Teleporting player");
        teleportRandomly(player);
        player.sendMessage(Text.literal("Evasion! ").formatted(Formatting.DARK_PURPLE), true);
        return true;
    }

    private static void updateCharges(PlayerEntity player) {
        ItemStack chestplate = player.getInventory().getArmorStack(2);
        if (!chestplate.isEmpty() && chestplate.getItem() instanceof EnderSteelArmorItem) {
            NbtCompound nbt = chestplate.getOrCreateNbt();
            
            int cooldown = nbt.getInt(EVASION_COOLDOWN_KEY);
            if (cooldown > 0) {
                nbt.putInt(EVASION_COOLDOWN_KEY, cooldown - 1);
                if (cooldown == 1) {
                    nbt.putInt(EVASION_CHARGES_KEY, MAX_CHARGES);
                    player.sendMessage(Text.literal("Evasion charges restored!").formatted(Formatting.DARK_PURPLE), true);
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
                nbt.putInt(EVASION_CHARGES_KEY, charges - 1);
                if (charges - 1 == 0) {
                    nbt.putInt(EVASION_COOLDOWN_KEY, CHARGE_COOLDOWN_TICKS);
                }
                return true;
            }
        }
        return false;
    }

    private static void teleportRandomly(PlayerEntity player) {
        double radius = 5.0;
        double theta = random.nextDouble() * 2 * Math.PI;
        
        Vec3d pos = player.getPos();
        double newX = pos.x + radius * Math.cos(theta);
        double newZ = pos.z + radius * Math.sin(theta);
        
        player.teleport(newX, pos.y, newZ);
        player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), 
                                  net.minecraft.sound.SoundEvents.ENTITY_ENDERMAN_TELEPORT, 
                                  net.minecraft.sound.SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    private static boolean isWearingFullEnderSteelArmor(PlayerEntity player) {
        for (ItemStack stack : player.getArmorItems()) {
            if (stack.isEmpty() || !(stack.getItem() instanceof EnderSteelArmorItem)) {
                return false;
            }
        }
        return true;
    }
}
