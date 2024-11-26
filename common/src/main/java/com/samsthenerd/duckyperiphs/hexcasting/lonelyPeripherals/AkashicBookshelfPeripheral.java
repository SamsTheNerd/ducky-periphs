package com.samsthenerd.duckyperiphs.hexcasting.lonelyPeripherals;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;
import com.samsthenerd.duckyperiphs.hexcasting.utils.IotaLuaUtils;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class AkashicBookshelfPeripheral implements IPeripheral{
    private BlockEntityAkashicBookshelf bookshelf;

    public Set<IComputerAccess> computers = Collections.newSetFromMap(new ConcurrentHashMap<>());

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

    @LuaFunction
    public final MethodResult readShelf(){
        return MethodResult.of(shelfData(bookshelf));
    }

    public static Map<String, Object> shelfData(BlockEntityAkashicBookshelf bookshelf){
        NbtCompound tag = bookshelf.getIotaTag();
        World world = bookshelf.getWorld();
        Object iotaObject;
        Iota iota = new NullIota();
        if(world instanceof ServerWorld){
            ServerWorld sworld = (ServerWorld)world;
            iotaObject = IotaLuaUtils.getLuaObject(tag, sworld);
            if(tag != null){
                iota = IotaType.deserialize(tag, sworld);
            }
        } else {
            // I guess pass null ? maybe shouldn't pass anything
            DuckyPeriphs.logPrint("reading iota in client world");
            iotaObject = IotaLuaUtils.getLuaObject(tag, null);
        }
        Map<String, Object> returnTable = new HashMap<String, Object>();

        returnTable.put("patternKey", IotaLuaUtils.getLuaObject(bookshelf.getPattern()));
        returnTable.put("shelfIota", iotaObject);
        returnTable.put("shelfIotaType", IotaLuaUtils.getIotaTypeID(iota.getType()));
        return returnTable;
    }
}
