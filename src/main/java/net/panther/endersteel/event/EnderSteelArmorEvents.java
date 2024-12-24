package net.panther.endersteel.event;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.panther.endersteel.item.ModItems;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

public class EnderSteelArmorEvents {
    private static final Map<String, Integer> playerEvasionCharges = new HashMap<>();
    private static final Map<String, Long> playerEvasionCooldowns = new HashMap<>();
    private static final int MAX_EVASION_CHARGES = 5;
    private static final long COOLDOWN_DURATION = 120000; // 2 minutes in milliseconds

    public static void register() {
        // Register any necessary event listeners here
    }

    public static void handlePlayerDamage(PlayerEntity player, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!(player instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

        String playerId = player.getName().getString();
        if (isWearingFullEnderSteelArmor(player)) {
            // Initialize evasion charges if not present
            playerEvasionCharges.putIfAbsent(playerId, MAX_EVASION_CHARGES);
            
            // Check if we have charges and not on cooldown
            if (playerEvasionCharges.get(playerId) > 0) {
                // Dodge the hit
                cir.cancel();
                
                // Decrease charges
                int remainingCharges = playerEvasionCharges.get(playerId) - 1;
                playerEvasionCharges.put(playerId, remainingCharges);

                // Teleport the player
                teleportRandomly(serverPlayer);
                
                // Play effect and send message
                player.sendMessage(Text.literal("Evasion! ").formatted(Formatting.DARK_PURPLE)
                    .append(Text.literal(remainingCharges + " charges remaining").formatted(Formatting.GRAY)), true);
                
                // If we're out of charges, start cooldown
                if (remainingCharges == 0) {
                    playerEvasionCooldowns.put(playerId, System.currentTimeMillis());
                }
            } else {
                // Check if cooldown is over
                Long cooldownStart = playerEvasionCooldowns.get(playerId);
                if (cooldownStart != null) {
                    long timeElapsed = System.currentTimeMillis() - cooldownStart;
                    if (timeElapsed >= COOLDOWN_DURATION) {
                        // Reset charges
                        playerEvasionCharges.put(playerId, MAX_EVASION_CHARGES);
                        playerEvasionCooldowns.remove(playerId);
                        player.sendMessage(Text.literal("Evasion recharged! ").formatted(Formatting.DARK_PURPLE)
                            .append(Text.literal(MAX_EVASION_CHARGES + " charges available").formatted(Formatting.GRAY)), true);
                    } else {
                        // Show remaining cooldown if hit while on cooldown
                        long remainingSeconds = (COOLDOWN_DURATION - timeElapsed) / 1000;
                        if (remainingSeconds > 0) {
                            player.sendMessage(Text.literal("Evasion on cooldown: ").formatted(Formatting.RED)
                                .append(Text.literal(remainingSeconds + "s").formatted(Formatting.GRAY)), true);
                        }
                    }
                }
            }
        }
    }

    private static void teleportRandomly(ServerPlayerEntity player) {
        World world = player.getWorld();
        double originalX = player.getX();
        double originalY = player.getY();
        double originalZ = player.getZ();
        Random random = world.getRandom();

        for(int i = 0; i < 16; ++i) {
            double targetX = player.getX() + (random.nextDouble() - 0.5D) * 16.0D;
            double targetY = MathHelper.clamp(player.getY() + (double)(random.nextInt(16) - 8), 
                world.getBottomY(), world.getBottomY() + ((world.getTopY() - world.getBottomY()) / 2));
            double targetZ = player.getZ() + (random.nextDouble() - 0.5D) * 16.0D;

            if (player.hasVehicle()) {
                player.stopRiding();
            }

            if (player.teleport(targetX, targetY, targetZ, true)) {
                world.playSound(null, originalX, originalY, originalZ, 
                    SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                break;
            }
        }
    }

    private static boolean isWearingFullEnderSteelArmor(PlayerEntity player) {
        Iterable<ItemStack> armorPieces = player.getArmorItems();
        boolean hasHelmet = false;
        boolean hasChestplate = false;
        boolean hasLeggings = false;
        boolean hasBoots = false;

        for (ItemStack armorPiece : armorPieces) {
            if (armorPiece.isOf(ModItems.ENDER_STEEL_HELMET)) {
                hasHelmet = true;
            } else if (armorPiece.isOf(ModItems.ENDER_STEEL_CHESTPLATE)) {
                hasChestplate = true;
            } else if (armorPiece.isOf(ModItems.ENDER_STEEL_LEGGINGS)) {
                hasLeggings = true;
            } else if (armorPiece.isOf(ModItems.ENDER_STEEL_BOOTS)) {
                hasBoots = true;
            }
        }

        return hasHelmet && hasChestplate && hasLeggings && hasBoots;
    }
}
