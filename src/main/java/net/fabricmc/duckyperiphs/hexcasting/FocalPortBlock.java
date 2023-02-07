package net.fabricmc.duckyperiphs.hexcasting;

import dan200.computercraft.shared.common.BlockGeneric;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;

public class FocalPortBlock extends BlockGeneric{
    public FocalPortBlock(FabricBlockSettings settings) {
        super(settings.nonOpaque(), () -> DuckyCasting.FOCAL_PORT_BLOCK_ENTITY);
        setDefaultState( getStateManager().getDefaultState());
    }
}
