package com.samsthenerd.duckyperiphs;

import javax.annotation.Nullable;

import com.samsthenerd.duckyperiphs.peripherals.DuckyPeriphsProviderCommon;

import dan200.computercraft.api.peripheral.PeripheralLookup;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PeriphLookupFabric{
    public static void registerPeripherals(){
        // it says this might be bad for performance but i don't see any other way to do it that will grab things in a reflection-y way
        PeripheralLookup.get().registerFallback((World world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, Direction side)->{
            if(blockEntity == null) return null;
            if(side == null) side = Direction.NORTH;
            return DuckyPeriphsProviderCommon.getPeripheral(blockEntity, side);
        });
    }
}
