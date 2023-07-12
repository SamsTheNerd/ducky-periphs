package com.samsthenerd.duckyperiphs.hexcasting.lonelyPeripherals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import at.petrak.hexcasting.api.misc.TriPredicate;
import at.petrak.hexcasting.api.spell.math.HexPattern;
import at.petrak.hexcasting.common.blocks.akashic.AkashicFloodfiller;
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AkashicRecordPeripheral implements IPeripheral{
    private BlockPos recordPos;
    private World recordWorld;

    public List<IComputerAccess> computers = new ArrayList<IComputerAccess>();

    AkashicRecordPeripheral(BlockPos _pos, World world){
        recordPos = _pos;
        recordWorld = world;
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
        return "akashic_record";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other )
    {
        return this == other;
    }

    @LuaFunction(mainThread=true)
    public final MethodResult readRecord(){
        FloodfillLogger sneakyPredicate = new FloodfillLogger();
        AkashicFloodfiller.floodFillFor(recordPos, recordWorld, 0, sneakyPredicate, 128);
        return MethodResult.of(sneakyPredicate.getRecordTable());
    }

    private class FloodfillLogger implements TriPredicate<BlockPos, BlockState, World>{
        private Map<String, Object> recordTable = new HashMap<String, Object>();

        @Override
        public boolean test(BlockPos pos, BlockState state, World world) {
            // DuckyPeriphs.logPrint("testing block (" + state.getBlock().getName().toString() + ") at " + pos.toString());
            BlockEntity be = world.getBlockEntity(pos);
            if(be == null) {
                return false;
            }
            // DuckyPeriphs.logPrint("block entity is " + be.toString());
            if(be instanceof BlockEntityAkashicBookshelf shelf){
                HexPattern pattern = shelf.getPattern();
                if(pattern == null){
                    return false;
                }
                String angleSig = pattern.anglesSignature();
                if(!recordTable.containsKey(angleSig)){
                    recordTable.put(angleSig, AkashicBookshelfPeripheral.shelfData(shelf));
                }
            }
            return false;
        }

        public Map<String, Object> getRecordTable(){
            return recordTable;
        }
    }
}
