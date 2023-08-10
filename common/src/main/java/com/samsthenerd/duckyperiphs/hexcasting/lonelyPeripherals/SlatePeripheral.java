package com.samsthenerd.duckyperiphs.hexcasting.lonelyPeripherals;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.samsthenerd.duckyperiphs.hexcasting.utils.IotaLuaUtils;

import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.common.blocks.circles.BlockEntitySlate;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class SlatePeripheral implements IPeripheral {
    private BlockEntitySlate slate;

    public List<IComputerAccess> computers = new ArrayList<IComputerAccess>();

    SlatePeripheral(BlockEntitySlate _slate){
        slate = _slate;
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
        return "slate";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other )
    {
        return this == other;
    }

    @LuaFunction
    public final MethodResult readPattern(){
        HexPattern pat = slate.pattern;
        return MethodResult.of(IotaLuaUtils.getLuaObject(pat));
    }
}
