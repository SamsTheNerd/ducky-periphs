package com.samsthenerd.duckyperiphs.peripherals;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.util.math.Direction;

public interface IPeripheralTileDucky {
    @Nullable
    IPeripheral getPeripheral( @Nonnull Direction side );
}
