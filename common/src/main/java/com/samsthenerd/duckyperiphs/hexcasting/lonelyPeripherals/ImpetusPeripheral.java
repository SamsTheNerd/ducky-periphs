package com.samsthenerd.duckyperiphs.hexcasting.lonelyPeripherals;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus;
import at.petrak.hexcasting.api.casting.circles.CircleExecutionState;
import at.petrak.hexcasting.api.casting.circles.ICircleComponent;
import at.petrak.hexcasting.common.blocks.circles.impetuses.BlockEntityLookingImpetus;
import at.petrak.hexcasting.common.blocks.circles.impetuses.BlockEntityRedstoneImpetus;
import at.petrak.hexcasting.common.blocks.circles.impetuses.BlockEntityRightClickImpetus;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class ImpetusPeripheral implements IPeripheral{
    private BlockEntityAbstractImpetus impetus;

    public Set<IComputerAccess> computers = Collections.newSetFromMap(new ConcurrentHashMap<>());

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
        if(impetus instanceof BlockEntityRedstoneImpetus)
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
        Text mishap = impetus.getDisplayMsg();
        if(mishap == null)
            return MethodResult.of("");
        return MethodResult.of(mishap.getString());
    }

    @LuaFunction(mainThread = true)
    public final MethodResult getCaster(){
        UUID casterUUID = null;
        if(impetus.getWorld() instanceof ServerWorld sWorld){
            CircleExecutionState state = impetus.getExecutionState();
            if(state != null && state.getCaster(sWorld) != null){
                casterUUID = state.getCaster(sWorld).getUuid();
            } else {
                if(impetus instanceof BlockEntityRedstoneImpetus rsImpetus)
                    casterUUID = rsImpetus.getStoredPlayer().getUuid();
            }
        }
        if(casterUUID == null)
            return MethodResult.of("");
        return MethodResult.of(casterUUID.toString());
    }

    @LuaFunction(mainThread = true)
    public final MethodResult isCasting(){
        if(impetus.getWorld() instanceof ServerWorld sWorld){
            BlockPos pos = impetus.getPos();
            BlockState state = sWorld.getBlockState(pos);
            if(state.getBlock() instanceof ICircleComponent comp){
                return MethodResult.of(comp.isEnergized(pos, state, sWorld));
            }
        }
        return MethodResult.of(false);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult activateCircle(){
        if(impetus instanceof BlockEntityRedstoneImpetus rsImpetus){
            rsImpetus.startExecution(rsImpetus.getStoredPlayer());
            return MethodResult.of(true);
        }
        return MethodResult.of(false);
    }

    public void impetusStarted(){
        for(IComputerAccess computer : computers){
            computer.queueEvent("circle_activated", computer.getAttachmentName());
        }
    }

    public void impetusEnded(){
        Text mishap = impetus.getDisplayMsg();
        String mishapString = "";
        if(mishap != null)
            mishapString = mishap.getString();
        for(IComputerAccess computer : computers){
            computer.queueEvent("circle_stopped", mishapString, computer.getAttachmentName());
        }
    }
}
