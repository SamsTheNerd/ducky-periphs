package com.samsthenerd.duckyperiphs;

import javax.annotation.Nonnull;

import com.samsthenerd.duckyperiphs.peripherals.DuckyPeriphsProviderCommon;

import blue.endless.jankson.annotation.Nullable;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class DuckyPeriphsProviderFabric implements IPeripheralProvider{
    @Nullable
    @Override
    public IPeripheral getPeripheral( @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Direction side ){
        return DuckyPeriphsProviderCommon.getPeripheral(world, pos, side);
    }
}
