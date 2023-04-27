package com.samsthenerd.duckyperiphs.peripherals.EntityDetector;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import dan200.computercraft.shared.common.BlockGeneric;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.world.World;

public class EntityDetectorBlock extends BlockGeneric{
    
    public EntityDetectorBlock(Block.Settings settings) {
        super(settings, () -> DuckyPeriphs.ENTITY_DETECTOR_TILE);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type){
        return checkType(type, DuckyPeriphs.ENTITY_DETECTOR_TILE, (worldin, pos, statein, be) -> EntityDetectorTile.tick(worldin, pos, statein, be));
    }
    
}
