package net.fabricmc.duckyperiphs.hexcasting;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import at.petrak.hexcasting.api.spell.iota.Iota;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class FocalPortPeripheral implements IPeripheral{
    private final FocalPortBlockEntity fpTile;

    public List<IComputerAccess> computers = new ArrayList<IComputerAccess>();


    FocalPortPeripheral( FocalPortBlockEntity _fpTile){
        fpTile = _fpTile;
    }

    @Override
    public void attach(IComputerAccess computer) {
        computers.add(computer);
    }

    @Override
    public void detach(IComputerAccess computer) {
        computers.remove(computer);
    }

    @Nonnull
    @Override
    public String getType()
    {
        return "focal_port";
    }

    @Override
    public boolean equals( IPeripheral other )
    {
        return this == other;
    }

    @LuaFunction
    public final MethodResult getIota(){
        Iota iota = fpTile.getIota();
        Object luaObject = IotaLuaUtils.getLuaObject(iota);
        return MethodResult.of(luaObject);
    }
}
