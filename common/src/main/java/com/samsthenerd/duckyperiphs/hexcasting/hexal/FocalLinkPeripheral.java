package com.samsthenerd.duckyperiphs.hexcasting.hexal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.samsthenerd.duckyperiphs.hexcasting.utils.IotaLuaUtils;

import at.petrak.hexcasting.api.spell.iota.GarbageIota;
import at.petrak.hexcasting.api.spell.iota.Iota;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import ram.talia.hexal.api.linkable.ILinkable;

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

    @LuaFunction
    public final void storeWorld(){
        flTile.storeWorld();
    }

    @LuaFunction
    public MethodResult receiveIota(){
        Iota iota = flTile.nextReceivedIota();
        return MethodResult.of(IotaLuaUtils.getLuaObject(iota, (ServerWorld)flTile.getWorld()));
    }

    @LuaFunction
    public void clearReceivedIotas(){
        flTile.clearReceivedIotas();
    }

    @LuaFunction
    public MethodResult remainingIotaCount(){
        return MethodResult.of(flTile.numRemainingIota());
    }

    @LuaFunction
    public MethodResult numLinked(){
        return MethodResult.of(flTile.numLinked());
    }

    // no idea if this will do what we want !
    @LuaFunction
    public MethodResult getLinked(){
        List<String> ourLinks = new ArrayList<String>();
        for(int i = 0; i < flTile.numLinked(); i++){
            ILinkable link = flTile.getLinked(i);
            if(link == null){
                ourLinks.add("null");
            } else {
                List<Text> linkText = link.transmittingTargetReturnDisplay();
                for(Text t : linkText){
                    ourLinks.add(t.getString() + ' ');
                }
            }
        }
        return MethodResult.of(ourLinks);
    }

    @LuaFunction(mainThread = true)
    public void sendIota(int index, Object luaObject){
        Iota iota = IotaLuaUtils.getIota(luaObject, (ServerWorld)flTile.getWorld());
        if(iota == null){
            iota = new GarbageIota();
        }
        flTile.sendIota(index, iota);
    }

    public void receivedIota(){
        for(IComputerAccess computer : computers){
            computer.queueEvent("received_iota", computer.getAttachmentName());
        }
    }

    // unused 
    public void receivedIota(Iota iota){
        Object luaObject = IotaLuaUtils.getLuaObject(iota, (ServerWorld)flTile.getWorld());
        for(IComputerAccess computer : computers){
            computer.queueEvent("received_iota", luaObject, computer.getAttachmentName());
        }
    }
}
