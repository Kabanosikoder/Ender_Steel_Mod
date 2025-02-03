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
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.panther.endersteel.EnderSteel;
import net.minecraft.block.Blocks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// THIS BLOCK IS VERY """EYE""" OPENING AHAHAHAH GET IT?
// DO YOU FCUKING GET IT? IM GOING CRAZY BECAUSE OF THIS ONE BLOCK
// Raycasting hell

public class EnderSteelStareBlock extends Block {
    public static final EnumProperty<OpenState> OPEN_STATE = EnumProperty.of("open_state", OpenState.class);

    private static final Set<BlockPos> blocksBeingLookedAt = new HashSet<>();
    private static final Map<BlockPos, Map<ServerPlayerEntity, Integer>> staringTicks = new HashMap<>();
    private static final int STARING_CONTEST_TICKS = 2400;
    private static final float RAYCAST_RANGE = 20.0f;

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

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(OPEN_STATE) == OpenState.FULLY_OPEN ? 15 : 0;
    }

    @Override
    public int getStrongRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(OPEN_STATE) == OpenState.FULLY_OPEN ? 15 : 0;
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!world.isClient) {
            boolean hasAdjacentRedstone = hasAdjacentRedstoneBlock(world, pos);
            OpenState currentState = state.get(OPEN_STATE);
            
            if (hasAdjacentRedstone && currentState == OpenState.CLOSED) {
                world.setBlockState(pos, state.with(OPEN_STATE, OpenState.FULLY_OPEN));
            } else if (!hasAdjacentRedstone && currentState == OpenState.FULLY_OPEN && !blocksBeingLookedAt.contains(pos)) {
                world.setBlockState(pos, state.with(OPEN_STATE, OpenState.CLOSED));
            }
        }
    }

    private static boolean hasAdjacentRedstoneBlock(World world, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockState neighborState = world.getBlockState(pos.offset(direction));
            if (neighborState.isOf(Blocks.REDSTONE_BLOCK)) {
                return true;
            }
        }
        return false;
    }

    public static class LookAtBlockHandler {
        public static void register() {
            EnderSteel.LOGGER.info("Registering LookAtBlockHandler");
            ServerTickEvents.END_SERVER_TICK.register(server -> {
                for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                    handlePlayerLookingAtBlocks(player);
                }
                handleBlocksNoLongerLookedAt(server.getPlayerManager().getPlayerList());
            });
        }

        private static void handlePlayerLookingAtBlocks(ServerPlayerEntity player) {
            BlockHitResult hitResult = rayTrace(player);
            if (hitResult == null) {
                return;
            }
            
            if (hitResult.getType() == BlockHitResult.Type.BLOCK) {
                BlockPos blockPos = hitResult.getBlockPos();
                BlockState blockState = player.getWorld().getBlockState(blockPos);

                if (blockState.getBlock() instanceof EnderSteelStareBlock) {
                    blocksBeingLookedAt.add(blockPos);

                    staringTicks.computeIfAbsent(blockPos, k -> new HashMap<>());
                    Map<ServerPlayerEntity, Integer> playerTicks = staringTicks.get(blockPos);
                    int currentTicks = playerTicks.getOrDefault(player, Integer.valueOf(0)) + 1;
                    playerTicks.put(player, Integer.valueOf(currentTicks));

                    EnderSteelStareBlock.OpenState currentState = blockState.get(EnderSteelStareBlock.OPEN_STATE);
                    EnderSteelStareBlock.OpenState nextState;

                    switch (currentState) {
                        case CLOSED -> nextState = OpenState.PARTLY_OPEN;
                        case PARTLY_OPEN -> nextState = OpenState.FULLY_OPEN;
                        default -> nextState = OpenState.FULLY_OPEN;
                    }

                    if (currentState != nextState) {
                        EnderSteel.LOGGER.info("Changing state from " + currentState + " to " + nextState);
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
                    EnderSteel.LOGGER.info("Block no longer being looked at: " + blockPos);
                    toRemove.add(blockPos);
                    staringTicks.getOrDefault(blockPos, new HashMap<>()).clear();
                }
            }

            for (BlockPos blockPos : toRemove) {
                blocksBeingLookedAt.remove(blockPos);

                for (ServerPlayerEntity player : players) {
                    BlockState blockState = player.getWorld().getBlockState(blockPos);
                    if (blockState.getBlock() instanceof EnderSteelStareBlock) {
                        EnderSteelStareBlock.OpenState currentState = blockState.get(EnderSteelStareBlock.OPEN_STATE);

                        // Only close the eye if there's no adjacent redstone block
                        if (currentState != EnderSteelStareBlock.OpenState.CLOSED && 
                            !hasAdjacentRedstoneBlock((World)player.getWorld(), blockPos)) {
                            EnderSteel.LOGGER.info("Closing eye at " + blockPos);
                            player.getWorld().setBlockState(blockPos, blockState.with(EnderSteelStareBlock.OPEN_STATE, EnderSteelStareBlock.OpenState.CLOSED));
                        }
                    }
                }
            }
        }

        private static BlockHitResult rayTrace(ServerPlayerEntity player) {
            return (BlockHitResult) player.raycast(RAYCAST_RANGE, 1.0f, false);
        }
    }
}