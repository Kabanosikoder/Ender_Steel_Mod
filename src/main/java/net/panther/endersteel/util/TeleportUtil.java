package net.panther.endersteel.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.Set;

public class TeleportUtil {
    private static final Random random = Random.create();
    private static final int MAX_VERTICAL_SEARCH = 8; // Maximum blocks to search up/down

    /**
     * Teleports an entity randomly within a radius
     * @param entity The entity to teleport
     * @param radius The radius to teleport within
     * @return true if teleport was successful
     */
    public static boolean teleportRandomly(Entity entity, double radius) {
        return teleportRandomly(entity, radius, true);
    }

    /**
     * @param addGlowingEffect Whether to add a glowing effect after teleport
     */
    public static boolean teleportRandomly(Entity entity, double radius, boolean addGlowingEffect) {
        if (entity == null || entity.getWorld().isClient) return false;

        World world = entity.getWorld();
        int attempts = 0;
        int maxAttempts = 10;

        // Distance checks
        double startX = entity.getX();
        double startZ = entity.getZ();

        while (attempts++ < maxAttempts) {
            double theta = random.nextDouble() * 2 * Math.PI;
            double dx = radius * Math.cos(theta);
            double dz = radius * Math.sin(theta);

            double newX = startX + dx;
            double newZ = startZ + dz;

            // Try to find a valid position, starting at current Y and searching up first
            BlockPos.Mutable checkPos = new BlockPos.Mutable(
                    (int)Math.floor(newX),
                    (int)Math.floor(entity.getY()),
                    (int)Math.floor(newZ)
            );

            // First try going up aggressively
            boolean foundSpot = false;
            for (int y = 0; y <= MAX_VERTICAL_SEARCH * 2; y++) {
                if (isValidTeleportSpot(world, checkPos.setY(checkPos.getY() + y))) {
                    double finalX = checkPos.getX() + 0.5;
                    double finalZ = checkPos.getZ() + 0.5;
                    double distSq = (finalX - startX) * (finalX - startX) +
                            (finalZ - startZ) * (finalZ - startZ);

                    if (distSq <= radius * radius) {
                        Vec3d pos = new Vec3d(finalX, checkPos.getY(), finalZ);
                        if (world instanceof ServerWorld serverWorld) {
                            entity.teleport(serverWorld, finalX, checkPos.getY(), finalZ, Set.of(), entity.getYaw(), entity.getPitch());
                        }
                        if (addGlowingEffect && entity instanceof LivingEntity living) {
                            living.addStatusEffect(new StatusEffectInstance(
                                    StatusEffects.GLOWING,
                                    60,
                                    0,
                                    false,
                                    false,
                                    false
                            ));
                        }
                        playTeleportEffects(entity);
                        return true;
                    }
                }
            }

            // Only if it can't go up, try going down a shorter distance
            if (!foundSpot) {
                checkPos.setY((int)Math.floor(entity.getY()));
                for (int y = 1; y <= MAX_VERTICAL_SEARCH / 2; y++) {  // Half the downward search range
                    if (isValidTeleportSpot(world, checkPos.setY(checkPos.getY() - y))) {
                        double finalX = checkPos.getX() + 0.5;
                        double finalZ = checkPos.getZ() + 0.5;
                        double distSq = (finalX - startX) * (finalX - startX) +
                                (finalZ - startZ) * (finalZ - startZ);

                        if (distSq <= radius * radius) {
                            Vec3d pos = new Vec3d(finalX, checkPos.getY(), finalZ);
                            if (world instanceof ServerWorld serverWorld) {
                                entity.teleport(serverWorld, finalX, checkPos.getY(), finalZ, Set.of(), entity.getYaw(), entity.getPitch());
                            }
                            if (addGlowingEffect && entity instanceof LivingEntity living) {
                                living.addStatusEffect(new StatusEffectInstance(
                                        StatusEffects.GLOWING,
                                        60,
                                        0,
                                        false,
                                        false,
                                        false
                                ));
                            }
                            playTeleportEffects(entity);
                            return true;
                        }
                    }
                }
            }
        }

        return false; // If it couldn't find a valid spot after max attempts
    }

    /**
     * Checks if a position is valid for teleporting
     * A valid position has solid ground below and 2 air blocks above
     * @param world The world to check in
     * @param pos The position to check
     * @return true if the position is valid for teleporting
     */
    private static boolean isValidTeleportSpot(World world, BlockPos pos) {
        return world.getBlockState(pos.down()).isSolid() &&
                world.getBlockState(pos).isAir() &&
                world.getBlockState(pos.up()).isAir();
    }

    /**
     * Plays teleport effects at the entity's location
     * @param entity The entity to play effects for
     */
    public static void playTeleportEffects(Entity entity) {
        if (entity.getWorld().isClient) return;
        ServerWorld world = (ServerWorld) entity.getWorld();

        // Play enderman teleport sound
        world.playSound(
                null,
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                SoundCategory.PLAYERS,
                1.0f,
                1.0f
        );

        for (int i = 0; i < 32; i++) {
            world.spawnParticles(
                    ParticleTypes.DRAGON_BREATH,
                    entity.getX(),
                    entity.getY() + random.nextDouble() * 2.0,
                    entity.getZ(),
                    1,
                    random.nextDouble() - 0.5,
                    random.nextDouble() - 0.5,
                    random.nextDouble() - 0.5,
                    0.1
            );
        }
    }
}