package net.panther.endersteel.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class EnderSteelStareBlock extends Block {
    public static final EnumProperty<OpenState> OPEN_STATE = EnumProperty.of("open_state", OpenState.class);

    private static final Set<BlockPos> blocksBeingLookedAt = new HashSet<>();

    public EnderSteelStareBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(OPEN_STATE, OpenState.CLOSED));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(OPEN_STATE);
    }

    public enum OpenState implements StringIdentifiable {
        CLOSED("closed"),
        PARTLY_OPEN("partly_open"),
        FULLY_OPEN("fully_open");

        private final String name;

        OpenState(String name) {
            this.name = name;
        }
        
        @Override
        public String asString() {
            return this.name;
        }
    }

    public static class LookAtBlockHandler {
        public static void register() {
            ServerTickEvents.END_SERVER_TICK.register(server -> {
                for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                    handlePlayerLookingAtBlocks(player);
                }
                handleBlocksNoLongerLookedAt(server.getPlayerManager().getPlayerList());
            });
        }

        private static void handlePlayerLookingAtBlocks(ServerPlayerEntity player) {
            BlockHitResult hitResult = rayTrace(player);
            if (hitResult != null && hitResult.getType() == BlockHitResult.Type.BLOCK) {
                BlockPos blockPos = hitResult.getBlockPos();
                BlockState blockState = player.getWorld().getBlockState(blockPos);

                if (blockState.getBlock() instanceof EnderSteelStareBlock) {
                    blocksBeingLookedAt.add(blockPos);

                    EnderSteelStareBlock.OpenState currentState = blockState.get(EnderSteelStareBlock.OPEN_STATE);
                    EnderSteelStareBlock.OpenState nextState;

                    switch (currentState) {
                        case CLOSED -> nextState = OpenState.PARTLY_OPEN;
                        case PARTLY_OPEN -> nextState = OpenState.FULLY_OPEN;
                        default -> nextState = currentState; // Stay at FULLY_OPEN whilst being looked at
                    }

                    if (currentState != nextState) {
                        player.getWorld().setBlockState(blockPos, blockState.with(EnderSteelStareBlock.OPEN_STATE, nextState));
                    }
                }
            }
        }

        private static void handleBlocksNoLongerLookedAt(Iterable<ServerPlayerEntity> players) {
            Set<BlockPos> toRemove = new HashSet<>();

            for (BlockPos blockPos : blocksBeingLookedAt) {
                boolean isBeingLookedAt = false;

                for (ServerPlayerEntity player : players) {
                    BlockHitResult hitResult = rayTrace(player);
                    if (hitResult != null && hitResult.getBlockPos().equals(blockPos)) {
                        isBeingLookedAt = true;
                        break;
                    }
                }
                if (!isBeingLookedAt) {
                    toRemove.add(blockPos);
                }
            }

            for (BlockPos blockPos : toRemove) {
                blocksBeingLookedAt.remove(blockPos);

                // Set the block directly to CLOSED state once the player looks away
                for (ServerPlayerEntity player : players) {
                    BlockState blockState = player.getWorld().getBlockState(blockPos);
                    if (blockState.getBlock() instanceof EnderSteelStareBlock) {
                        EnderSteelStareBlock.OpenState currentState = blockState.get(EnderSteelStareBlock.OPEN_STATE);

                        if (currentState != EnderSteelStareBlock.OpenState.CLOSED) {
                            player.getWorld().setBlockState(blockPos, blockState.with(EnderSteelStareBlock.OPEN_STATE, EnderSteelStareBlock.OpenState.CLOSED));
                        }
                    }
                }
            }
        }

        private static BlockHitResult rayTrace(ServerPlayerEntity player) {
            float reachDistance = player.getAbilities().creativeMode ? 5.0f : 4.5f;
            return (BlockHitResult) player.raycast(reachDistance, 0.0f, false);
        }
    }
}