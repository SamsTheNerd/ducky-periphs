package com.samsthenerd.duckyperiphs.peripherals;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.samsthenerd.duckyperiphs.hexcasting.lonelyPeripherals.DuckyCastingLonelyPeripheralProvider;

import dan200.computercraft.api.peripheral.IPeripheral;
import dev.architectury.platform.Platform;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

// will need to be called by classes on fabric and forge since they return @Nullable vs LazyOptional
public class DuckyPeriphsProviderCommon{
    @Nullable
    public static IPeripheral getPeripheral( @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Direction side ){
        BlockEntity be = world.getBlockEntity(pos);
        return getPeripheral(be, side);
    }

    @Nullable
    public static IPeripheral getPeripheral(BlockEntity be, @Nonnull Direction side){
        if(be instanceof IPeripheralTileDucky){
            return ((IPeripheralTileDucky)be).getPeripheral(side);
        }
        if(Platform.isModLoaded("hexcasting")){
            IPeripheral lonelyHexPeriph = DuckyCastingLonelyPeripheralProvider.getPeripheral(be, side);
            if(lonelyHexPeriph != null){
                return lonelyHexPeriph;
            }
        }
        return null;
    }
}
