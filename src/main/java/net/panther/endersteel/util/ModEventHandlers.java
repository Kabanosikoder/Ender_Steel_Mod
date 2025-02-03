package net.panther.endersteel.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.panther.endersteel.block.custom.EnderSteelStareBlock;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModEventHandlers {
    private static final Set<BlockPos> blocksBeingLookedAt = new HashSet<>();
    private static final Map<BlockPos, Integer> blockTickDelays = new HashMap<>();
    private static final int UPDATE_DELAY_TICKS = 15;

    public static void registerEventHandlers() {
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                handlePlayerLookingAtBlock(player);
            }
            handleBlocksNoLongerLookedAt(server.getPlayerManager().getPlayerList());
        });
    }

    private static void handlePlayerLookingAtBlock(ServerPlayerEntity player) {
        BlockHitResult hitResult = rayTrace(player);
        if (hitResult != null && hitResult.getType() == BlockHitResult.Type.BLOCK) {
            BlockPos blockPos = hitResult.getBlockPos();
            BlockState blockState = player.getWorld().getBlockState(blockPos);

            if (blockState.getBlock() instanceof EnderSteelStareBlock) {
                blocksBeingLookedAt.add(blockPos);

                int ticksRemaining = blockTickDelays.getOrDefault(blockPos, Integer.valueOf(0));
                if (ticksRemaining > 0) {
                    blockTickDelays.put(blockPos, Integer.valueOf(ticksRemaining - 1));
                    return;
                }

                EnderSteelStareBlock.OpenState currentState = blockState.get(EnderSteelStareBlock.OPEN_STATE);
                EnderSteelStareBlock.OpenState nextState;

                switch (currentState) {
                    case CLOSED -> nextState = EnderSteelStareBlock.OpenState.PARTLY_OPEN;
                    case PARTLY_OPEN -> nextState = EnderSteelStareBlock.OpenState.FULLY_OPEN;
                    default -> nextState = EnderSteelStareBlock.OpenState.FULLY_OPEN; // Stay at FULLY_OPEN
                }

                if (nextState != currentState) {
                    player.getWorld().setBlockState(blockPos, blockState.with(EnderSteelStareBlock.OPEN_STATE, nextState));
                    blockTickDelays.put(blockPos, Integer.valueOf(UPDATE_DELAY_TICKS)); // Set delay for next update
                }
            }
        }
    }

    private static void handleBlocksNoLongerLookedAt(Iterable<ServerPlayerEntity> players) {
        Set<BlockPos> noLongerLookedAt = new HashSet<>();

        for (BlockPos blockPos : blocksBeingLookedAt) {
            boolean isBeingLookedAt = false;

            // Check if any player is still looking at this block
            for (ServerPlayerEntity player : players) {
                BlockHitResult hitResult = rayTrace(player);
                if (hitResult != null && hitResult.getBlockPos().equals(blockPos)) {
                    isBeingLookedAt = true;
                    break;
                }
            }

            // If no players are looking at the block, mark it for removal
            if (!isBeingLookedAt) {
                noLongerLookedAt.add(blockPos);
            }
        }

        // Process blocks that are no longer being looked at
        for (BlockPos blockPos : noLongerLookedAt) {
            blocksBeingLookedAt.remove(blockPos);

            int ticksRemaining = blockTickDelays.getOrDefault(blockPos, Integer.valueOf(0));
            if (ticksRemaining > 0) {
                blockTickDelays.put(blockPos, Integer.valueOf(ticksRemaining - 1));
                continue; // Wait for delay before transitioning
            }

            // Set the block state directly to CLOSED
            BlockState blockState = players.iterator().next().getWorld().getBlockState(blockPos);
            if (blockState.getBlock() instanceof EnderSteelStareBlock) {
                EnderSteelStareBlock.OpenState currentState = blockState.get(EnderSteelStareBlock.OPEN_STATE);

                EnderSteelStareBlock.OpenState nextState = EnderSteelStareBlock.OpenState.CLOSED;
                if (currentState != nextState) {
                    players.iterator().next().getWorld().setBlockState(blockPos, blockState.with(EnderSteelStareBlock.OPEN_STATE, nextState));
                    blockTickDelays.put(blockPos, Integer.valueOf(UPDATE_DELAY_TICKS));
                }
            }
        }
    }

    private static BlockHitResult rayTrace(ServerPlayerEntity player) {
        float reachDistance = player.getAbilities().creativeMode ? 5.0f : 4.5f;
        return (BlockHitResult) player.raycast(reachDistance, 0.0f, false);
    }
}