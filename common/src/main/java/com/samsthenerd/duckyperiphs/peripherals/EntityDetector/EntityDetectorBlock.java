package com.samsthenerd.duckyperiphs.peripherals.EntityDetector;

import javax.annotation.Nullable;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityDetectorBlock extends BlockWithEntity{
    
    public EntityDetectorBlock(Block.Settings settings) {
        super(settings);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type){
        return checkType(type, DuckyPeriphs.ENTITY_DETECTOR_TILE.get(), (worldin, pos, statein, be) -> EntityDetectorTile.tick(worldin, pos, statein, be));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state )
    {
        return new EntityDetectorTile(pos, state);
    }
}
