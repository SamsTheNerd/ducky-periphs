package com.samsthenerd.duckyperiphs.hexcasting;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;
import com.samsthenerd.duckyperiphs.hexcasting.utils.IotaLuaUtils;

import at.petrak.hexcasting.api.spell.iota.Iota;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class FocalPortPeripheral implements IPeripheral{
    private final FocalPortBlockEntity fpTile;

    public List<IComputerAccess> computers = new ArrayList<IComputerAccess>();


    FocalPortPeripheral( FocalPortBlockEntity _fpTile){
        fpTile = _fpTile;
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
        return "focal_port";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other )
    {
        return this == other;
    }

    @LuaFunction
    public final MethodResult readIota(){
        Iota iota = fpTile.getIota();
        World world = fpTile.getWorld();
        Object luaObject;
        if(world instanceof ServerWorld){
            ServerWorld sworld = (ServerWorld)world;
            luaObject = IotaLuaUtils.getLuaObject(iota, sworld);
        } else {
            // I guess pass null ? maybe shouldn't pass anything
            DuckyPeriphs.logPrint("reading iota in client world");
            luaObject = IotaLuaUtils.getLuaObject(iota, null);
        }
        return MethodResult.of(luaObject);
    }

    @LuaFunction
    public final MethodResult writeIota(Object luaObject){
        if(fpTile.getWorld().isClient() || fpTile.getWorld() == null){
            return MethodResult.of(false);
        }
        Iota iota = IotaLuaUtils.getIota(luaObject, (ServerWorld)fpTile.getWorld());
        return MethodResult.of(fpTile.writeIota(iota, false));
    }

    @LuaFunction
    public final MethodResult canWriteIota(Object luaObject){
        if(fpTile.getWorld().isClient() || fpTile.getWorld() == null){
            return MethodResult.of(false);
        }
        Iota iota = IotaLuaUtils.getIota(luaObject, (ServerWorld)fpTile.getWorld());
        return MethodResult.of(fpTile.writeIota(iota, true));
    }

    @LuaFunction
    public final MethodResult hasFocus(Object luaObject){
        if(fpTile.getWorld().isClient() || fpTile.getWorld() == null){
            return MethodResult.of(false);
        }
        return MethodResult.of(fpTile.hasFocus());
    }

    @LuaFunction 
    public final MethodResult getIotaType(){
        if(fpTile.getWorld().isClient() || fpTile.getWorld() == null){
            return MethodResult.of();
        }
        Iota iota = fpTile.getIota();
        if(iota == null){
            return MethodResult.of("empty");
        }
        return MethodResult.of(IotaLuaUtils.getIotaTypeID(iota.getType()));
    }

    @LuaFunction
    public final MethodResult getSlotCount(){
        return MethodResult.of(fpTile.getSlotCount());
    }

    @LuaFunction
    public final MethodResult getCurrentSlot(){
        return MethodResult.of(fpTile.getCurrentSlot());
    }

    @LuaFunction
    public final MethodResult setCurrentSlot(int slot){
        return MethodResult.of(fpTile.setCurrentSlot(slot));
    }

    public void updateIota(){
        for(IComputerAccess computer : computers){
            computer.queueEvent("new_iota", computer.getAttachmentName());
        }
    }

    public void attachFocus(){
        for(IComputerAccess computer : computers){
            computer.queueEvent("focus_inserted", computer.getAttachmentName());
        }
    }

    public void detachFocus(){
        for(IComputerAccess computer : computers){
            computer.queueEvent("focus_removed", computer.getAttachmentName());
        }
    }


}
