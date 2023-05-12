package com.samsthenerd.duckyperiphs.mixin.HexMixins;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.samsthenerd.duckyperiphs.peripherals.IPeripheralTileDucky;

import at.petrak.hexcasting.api.block.circle.BlockEntityAbstractImpetus;
import net.minecraft.util.math.BlockPos;

@Mixin(BlockEntityAbstractImpetus.class)
public interface BEImpetusPeripheralMixin extends IPeripheralTileDucky{
    @Accessor("activator")
    public UUID getActivator();

    @Accessor("nextBlock")
    public BlockPos getNextBlock();
}