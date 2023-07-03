package com.samsthenerd.duckyperiphs.hexcasting.hexal;

import com.samsthenerd.duckyperiphs.peripherals.IPeripheralTileDucky;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import ram.talia.hexal.api.linkable.ILinkable;
import ram.talia.hexal.api.linkable.ILinkable.IRenderCentre;

public class FocalLinkBlockEntity extends BlockEntity implements IPeripheralTileDucky, ILinkable, IRenderCentre{
    private FocalLinkPeripheral flPeriph;

    public FocalLinkBlockEntity(BlockPos pos, BlockState state) {
        super(DuckyHexal.FOCAL_LINK_BLOCK_ENTITY.get(), pos, state);
        flPeriph = new FocalLinkPeripheral(this);
    }
}
