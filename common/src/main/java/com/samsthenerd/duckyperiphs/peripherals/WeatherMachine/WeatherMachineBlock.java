package com.samsthenerd.duckyperiphs.peripherals.WeatherMachine;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class WeatherMachineBlock extends BlockWithEntity{
    // need a facing
    public static final DirectionProperty FACING = FacingBlock.FACING;
    public static final IntProperty TEMP = IntProperty.of("temp", 0, 4);
    public static final IntProperty TANK = IntProperty.of("tank", 0, 4);

    public WeatherMachineBlock(Block.Settings settings) {
        super(settings.nonOpaque());
        setDefaultState( getStateManager().getDefaultState());
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getPlayerFacing().getOpposite();
        if(direction == Direction.DOWN || direction == Direction.UP)
            direction = Direction.NORTH;
        return (BlockState)this.getDefaultState().with(FACING, direction).with(TEMP, 0).with(TANK, 0);
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(TEMP).add(TANK);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type){
        return checkType(type, DuckyPeriphs.WEATHER_MACHINE_TILE.get(), (worldin, pos, statein, be) -> WeatherMachineTile.tick(worldin, pos, statein, be));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state )
    {
        return new WeatherMachineTile(pos, state);
    }

    @Nonnull
    @Override
    public BlockRenderType getRenderType( @Nonnull BlockState state )
    {
        return BlockRenderType.MODEL;
    }
}