package com.samsthenerd.duckyperiphs.hexcasting.lonelyPeripherals;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.samsthenerd.duckyperiphs.mixin.HexMixins.BEImpetusPeripheralMixin;
import com.samsthenerd.duckyperiphs.mixin.HexMixins.ClericImpetusAccessor;

import at.petrak.hexcasting.api.block.circle.BlockEntityAbstractImpetus;
import at.petrak.hexcasting.common.blocks.entity.BlockEntityLookingImpetus;
import at.petrak.hexcasting.common.blocks.entity.BlockEntityRightClickImpetus;
import at.petrak.hexcasting.common.blocks.entity.BlockEntityStoredPlayerImpetus;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class ImpetusPeripheral implements IPeripheral{
    private BlockEntityAbstractImpetus impetus;

    public List<IComputerAccess> computers = new ArrayList<IComputerAccess>();

    public ImpetusPeripheral(BlockEntityAbstractImpetus _impetus){
        impetus = _impetus;
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
        if(impetus instanceof BlockEntityLookingImpetus)
            return "fletcher_impetus";
        if(impetus instanceof BlockEntityStoredPlayerImpetus)
            return "cleric_impetus";
        if(impetus instanceof BlockEntityRightClickImpetus)
            return "toolsmith_impetus";
        return "impetus"; // sorry other addons, uhh dm me if you add an impetus i guess
    }

    @Override
    public boolean equals(@Nullable IPeripheral other )
    {
        return this == other;
    }

    @LuaFunction
    public final MethodResult getMedia(){
        return MethodResult.of(impetus.getMedia());
    }

    @LuaFunction
    public final MethodResult getRemainingMediaCapacity(){
        return MethodResult.of(impetus.remainingMediaCapacity());
    }

    @LuaFunction
    public final MethodResult getLastMishap(){
        Text mishap = impetus.getLastMishap();
        if(mishap == null)
            return MethodResult.of("");
        return MethodResult.of(mishap.getString());
    }

    @LuaFunction
    public final MethodResult getCaster(){
        UUID casterUUID = ((BEImpetusPeripheralMixin) impetus).getActivator();
        if(impetus instanceof BlockEntityStoredPlayerImpetus)
            casterUUID = ((ClericImpetusAccessor) impetus).getStoredPlayer();
        if(casterUUID == null)
            return MethodResult.of("");
        return MethodResult.of(casterUUID.toString());
    }

    @LuaFunction
    public final MethodResult isCasting(){
        return MethodResult.of(((BEImpetusPeripheralMixin) impetus).getNextBlock() != null);
    }

    @LuaFunction
    public final MethodResult activateCircle(){
        if(impetus instanceof BlockEntityStoredPlayerImpetus){
            if(((BlockEntityStoredPlayerImpetus) impetus).getStoredPlayer() instanceof ServerPlayerEntity sPlayer){
                ((BlockEntityStoredPlayerImpetus) impetus).activateSpellCircle(sPlayer);
                return MethodResult.of(true);
            }
        }
        return MethodResult.of(false);
    }

    public void impetusStarted(){
        for(IComputerAccess computer : computers){
            computer.queueEvent("circle_activated", computer.getAttachmentName());
        }
    }

    public void impetusEnded(){
        Text mishap = impetus.getLastMishap();
        String mishapString = "";
        if(mishap != null)
            mishapString = mishap.getString();
        for(IComputerAccess computer : computers){
            computer.queueEvent("circle_stopped", mishapString, computer.getAttachmentName());
        }
    }
}
