package com.samsthenerd.duckyperiphs.peripherals.WeatherMachine;
import com.samsthenerd.duckyperiphs.DuckyPeriph;

import dan200.computercraft.shared.common.BlockGeneric;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

// based off of BlockSpeaker, idk how best practice this is for what I'm doing
public class WeatherMachineBlock extends BlockGeneric{
    // need a facing
    public static final DirectionProperty FACING = FacingBlock.FACING;
    public static final IntProperty TEMP = IntProperty.of("temp", 0, 4);
    public static final IntProperty TANK = IntProperty.of("tank", 0, 4);

    public WeatherMachineBlock(FabricBlockSettings settings) {
        super(settings.nonOpaque(), () -> DuckyPeriph.WEATHER_MACHINE_TILE);
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
        return checkType(type, DuckyPeriph.WEATHER_MACHINE_TILE, (worldin, pos, statein, be) -> WeatherMachineTile.tick(worldin, pos, statein, be));
    }

}