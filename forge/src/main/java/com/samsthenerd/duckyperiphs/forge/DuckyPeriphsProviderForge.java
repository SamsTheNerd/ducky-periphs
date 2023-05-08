package com.samsthenerd.duckyperiphs.forge;

import javax.annotation.Nonnull;

import com.samsthenerd.duckyperiphs.peripherals.DuckyPeriphsProviderCommon;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

public class DuckyPeriphsProviderForge implements IPeripheralProvider{
    @Override
    public LazyOptional<IPeripheral> getPeripheral( @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Direction side ){
        IPeripheral ip = DuckyPeriphsProviderCommon.getPeripheral(world, pos, side);
        if(ip == null){
            return LazyOptional.empty();
        } else {
            return LazyOptional.of(() -> ip);
        }
    }
}