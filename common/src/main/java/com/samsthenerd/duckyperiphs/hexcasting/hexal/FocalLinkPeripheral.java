package com.samsthenerd.duckyperiphs.hexcasting.hexal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class FocalLinkPeripheral implements IPeripheral{
    private final FocalLinkBlockEntity flTile;

    public List<IComputerAccess> computers = new ArrayList<IComputerAccess>();


    FocalLinkPeripheral( FocalLinkBlockEntity _flTile){
        flTile = _flTile;
    }

    @Override
    public void attach(@Nonnull IComputerAccess computer) {
        computers.add(computer);
    }

    @Override
    public void detach(@Nonnull IComputerAccess computer) {
        computers.remove(computer);
    }

    @Nonnull
    @Override
    public String getType()
    {
        return "focal_link";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other )
    {
        return this == other;
    }
}
