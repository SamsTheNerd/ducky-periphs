package com.samsthenerd.duckyperiphs.peripherals.WeatherMachine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class WeatherMachineTile extends TileGeneric implements IPeripheralTile {
    private WeatherMachinePeripheral wmPeriph;

    public WeatherMachineTile(BlockEntityType<WeatherMachineTile> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        wmPeriph = new WeatherMachinePeripheral(this);
    }

    public WeatherMachineTile(BlockPos pos, BlockState state) {
        this(DuckyPeriphs.WEATHER_MACHINE_TILE, pos, state);
    }

    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull Direction side){
        if(wmPeriph == null)
            wmPeriph = new WeatherMachinePeripheral(this);
        return wmPeriph;
    }


    // get pos and get world so we can get the weather from our WeatherMachinePeripheral
    public BlockPos getPos(){
        return this.pos;
    }

    public World getWorld(){
        return this.world;
    }

    public int tempStateFromVal(float tempVal){
        if(tempVal <= 0){
            return 0;
        } else if(tempVal >= 1.5){
            return 4;
        } else if(tempVal <= 0.5){
            return 1;
        } else if(tempVal >= 0.8){
            return 3;
        }
        return 2;
    }

    public void updateFrontStateFull(){
        if(wmPeriph == null) // not sure if needed but oh well
            return;
        // probably only need to check rain, just do temperature once? We don't expect biome to change idk
        // maybe update it when it's called in the lua function?
        boolean isPrecip = wmPeriph.isPrecipitating();
        float tempVal = wmPeriph.getTemperature();
        // add more complex filling up and draining logic for tank here later
        updateFrontState(tempStateFromVal(tempVal), isPrecip ? 3 : 0);
    }

    public void updateFrontState(int tempState, int tank){
        World world = getWorld();
        BlockPos pos = getPos();
        BlockState state = world.getBlockState(pos);
        if(state.getBlock() instanceof WeatherMachineBlock){
            world.setBlockState(pos, state.with(WeatherMachineBlock.TEMP, tempState).with(WeatherMachineBlock.TANK, tank), Block.NOTIFY_ALL);
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, WeatherMachineTile wm_tile){
        if(wm_tile == null)
            return;
        wm_tile.updateFrontStateFull();
    }
}
