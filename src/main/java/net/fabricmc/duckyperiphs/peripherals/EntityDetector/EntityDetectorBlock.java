package net.fabricmc.duckyperiphs.peripherals.EntityDetector;

import dan200.computercraft.shared.common.BlockGeneric;
import net.fabricmc.duckyperiphs.DuckyPeriph;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.world.World;

public class EntityDetectorBlock extends BlockGeneric{
    
    public EntityDetectorBlock(FabricBlockSettings settings) {
        super(settings, () -> DuckyPeriph.ENTITY_DETECTOR_TILE);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type){
        return checkType(type, DuckyPeriph.ENTITY_DETECTOR_TILE, (worldin, pos, statein, be) -> EntityDetectorTile.tick(worldin, pos, statein, be));
    }
    
}
