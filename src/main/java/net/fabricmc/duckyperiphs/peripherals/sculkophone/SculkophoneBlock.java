package net.fabricmc.duckyperiphs.peripherals.sculkophone;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.duckyperiphs.DuckyPeriph;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.SculkSensorPhase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.GameEventListener;

public class SculkophoneBlock extends BlockWithEntity{
    public static final int field_31239 = 40;
    public static final int field_31240 = 1;
    public static final EnumProperty<SculkSensorPhase> SCULK_SENSOR_PHASE = Properties.SCULK_SENSOR_PHASE;
    private final int range;

    public SculkophoneBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(SCULK_SENSOR_PHASE, SculkSensorPhase.INACTIVE)));
        this.range = 8;
    }

    public int getRange() {
        return this.range;
    }

    @Override
    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState();
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (SculkophoneBlock.getPhase(state) != SculkSensorPhase.ACTIVE) {
            if (SculkophoneBlock.getPhase(state) == SculkSensorPhase.COOLDOWN) {
                world.setBlockState(pos, (BlockState)state.with(SCULK_SENSOR_PHASE, SculkSensorPhase.INACTIVE), Block.NOTIFY_ALL);
            }
            return;
        }
        SculkophoneBlock.setCooldown(world, pos, state);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClient() && SculkophoneBlock.isInactive(state) && entity.getType() != EntityType.WARDEN) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof SculkophoneBlockEntity) {
                SculkophoneBlockEntity SculkophoneBlockEntity = (SculkophoneBlockEntity)blockEntity;
                // remove below once we have a way to send events

                // SculkophoneBlockEntity.setLastVibrationFrequency(FREQUENCIES.get(GameEvent.STEP));
            }
            SculkophoneBlock.setActive(entity, world, pos, state);
        }
        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.isOf(newState.getBlock())) {
            return;
        }
        if (SculkophoneBlock.getPhase(state) == SculkSensorPhase.ACTIVE) {
            SculkophoneBlock.updateNeighbors(world, pos);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    private static void updateNeighbors(World world, BlockPos pos) {
        world.updateNeighborsAlways(pos, Blocks.SCULK_SENSOR);
        world.updateNeighborsAlways(pos.offset(Direction.UP.getOpposite()), Blocks.SCULK_SENSOR);
    }

    @Override
    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SculkophoneBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> GameEventListener getGameEventListener(ServerWorld world, T blockEntity) {
        if (blockEntity instanceof SculkophoneBlockEntity) {
            return ((SculkophoneBlockEntity)blockEntity).getEventListener();
        }
        return null;
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world2, BlockState state2, BlockEntityType<T> type) {
        if (!world2.isClient) {
            return SculkophoneBlock.checkType(type, DuckyPeriph.SCULKOPHONE_BLOCK_ENTITY, (world, pos, state, blockEntity) -> blockEntity.getEventListener().tick(world));
        }
        return null;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    public static SculkSensorPhase getPhase(BlockState state) {
        return state.get(SCULK_SENSOR_PHASE);
    }

    public static boolean isInactive(BlockState state) {
        return SculkophoneBlock.getPhase(state) == SculkSensorPhase.INACTIVE;
    }

    public static void setCooldown(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, ((BlockState)state.with(SCULK_SENSOR_PHASE, SculkSensorPhase.COOLDOWN)), Block.NOTIFY_ALL);
        world.createAndScheduleBlockTick(pos, state.getBlock(), 1);
        world.playSound(null, pos, DuckyPeriph.SCULKOPHONE_CLICKING_STOP_EVENT, SoundCategory.BLOCKS, 0.25f, world.random.nextFloat() * 0.2f + 0.8f);
        SculkophoneBlock.updateNeighbors(world, pos);
    }

    public static void setActive(@Nullable Entity entity, World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, ((BlockState)state.with(SCULK_SENSOR_PHASE, SculkSensorPhase.ACTIVE)), Block.NOTIFY_ALL);
        world.createAndScheduleBlockTick(pos, state.getBlock(), 20); // not sure if this is just for animation or for cooldown?
        SculkophoneBlock.updateNeighbors(world, pos);
        // world.emitGameEvent(entity, DuckyPeriph.SCULKOPHONE_CLICKING_GAME_EVENT, pos); // remove this when we're done testing
        world.playSound(null, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, DuckyPeriph.SCULKOPHONE_CLICKING_EVENT, SoundCategory.BLOCKS, 0.25f, world.random.nextFloat() * 0.2f + 0.8f);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(SCULK_SENSOR_PHASE);
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof SculkophoneBlockEntity) {
            SculkophoneBlockEntity SculkophoneBlockEntity = (SculkophoneBlockEntity)blockEntity;
            return SculkophoneBlock.getPhase(state) == SculkSensorPhase.ACTIVE ? SculkophoneBlockEntity.getLastVibrationFrequency() : 0;
        }
        return 0;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        return false;
    }
}


