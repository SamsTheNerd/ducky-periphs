package com.samsthenerd.duckyperiphs.hexcasting.lonelyPeripherals;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class AkashicBookshelfPeripheral implements IPeripheral{
    private BlockEntityAkashicBookshelf bookshelf;

    public List<IComputerAccess> computers = new ArrayList<IComputerAccess>();

    AkashicBookshelfPeripheral(BlockEntityAkashicBookshelf _bookshelf){
        bookshelf = _bookshelf;
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
        return "akashic_bookshelf";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other )
    {
        return this == other;
    }
}
