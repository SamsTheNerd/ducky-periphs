package com.samsthenerd.duckyperiphs.hexcasting.hexal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.samsthenerd.duckyperiphs.hexcasting.utils.IotaLuaUtils;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
import at.petrak.hexcasting.api.spell.iota.GarbageIota;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.common.lib.HexItems;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper.Argb;
import net.minecraft.util.registry.Registry;
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
    public final MethodResult receiveIota(){
        Iota iota = flTile.nextReceivedIota();
        return MethodResult.of(IotaLuaUtils.getLuaObject(iota, (ServerWorld)flTile.getWorld()));
    }

    @LuaFunction
    public final void clearReceivedIotas(){
        flTile.clearReceivedIotas();
    }

    @LuaFunction
    public final MethodResult remainingIotaCount(){
        return MethodResult.of(flTile.numRemainingIota());
    }

    @LuaFunction
    public final MethodResult numLinked(){
        return MethodResult.of(flTile.numLinked());
    }
    
    // no idea if this will do what we want !
    @LuaFunction( mainThread = true)
    public final MethodResult getLinked(){
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
    public final void sendIota(int index, Object luaObject){
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

    @LuaFunction(mainThread = true)
    public final void setPigmentRGB(int argb){
        flTile.setRGBColorizer(argb);
    }

    @LuaFunction(mainThread = true)
    public final void setPigmentRGB(int r, int g, int b){
        setPigmentRGB(Argb.getArgb(255, r, g, b));
    }

    @LuaFunction
    public final MethodResult getPigment(){
        FrozenColorizer col = flTile.colouriser();
        Identifier id = Registry.ITEM.getId(col.item().getItem());
        String idString = id.toString();
        if(col.item().getItem() == DuckyHexal.ITEM_RGB_COLORIZER){
            idString = "rgb:" + getPigmentRGB();
        }
        if(col.item().getItem() == HexItems.UUID_COLORIZER){
            idString = "soulglimmer:" + col.owner().toString();
        }
        return MethodResult.of(idString);
    }

    @LuaFunction
    public final MethodResult getPigmentRGB(){
        FrozenColorizer col = flTile.colouriser();
        int rgb = ItemRGBColorizer.getRGB(col.item());
        return MethodResult.of(rgb);
    }

    public void newColorizer(){
        for(IComputerAccess computer : computers){
            computer.queueEvent("new_pigment", computer.getAttachmentName());
        }
    }
}
