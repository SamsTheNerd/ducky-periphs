package com.samsthenerd.duckyperiphs.hexcasting.lonelyPeripherals;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import at.petrak.hexcasting.common.blocks.akashic.BlockAkashicRecord;
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf;
import at.petrak.hexcasting.common.blocks.circles.BlockEntitySlate;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class DuckyCastingLonelyPeripheralProvider {

    @Nullable
    public static IPeripheral getPeripheral( @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Direction side ){
        BlockEntity be = world.getBlockEntity(pos);
        return getPeripheral(be, side);
    }

    @Nullable
    public static IPeripheral getPeripheral(BlockEntity be, Direction side){
        if(be instanceof BlockEntityAkashicBookshelf shelf){
            return new AkashicBookshelfPeripheral(shelf);
        }
        if(be instanceof BlockEntitySlate slate){
            return new SlatePeripheral(slate);
        }
        if(be != null && be.getWorld().getBlockState(be.getPos()).getBlock() instanceof BlockAkashicRecord){
            return new AkashicRecordPeripheral(be.getPos(), be.getWorld());
        }
        return null;
    }
}
