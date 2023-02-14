package net.fabricmc.duckyperiphs.hexcasting;

import javax.annotation.Nullable;

import dan200.computercraft.shared.common.BlockGeneric;
import net.fabricmc.duckyperiphs.DuckyPeriph;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FocalPortBlock extends BlockGeneric{
    public FocalPortBlock(FabricBlockSettings settings) {
        super(settings.nonOpaque(), () -> DuckyCasting.FOCAL_PORT_BLOCK_ENTITY);
        setDefaultState( getStateManager().getDefaultState());
    }

    // use to spawn the wrapper entity
    // check FocalPortBlockEntity for despawn
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if(world instanceof ServerWorld){
            FocalPortBlockEntity be = (FocalPortBlockEntity)world.getBlockEntity(pos);
            if(be != null){
                be.spawnWrapperEntity(pos);
            } else {
                DuckyPeriph.LOGGER.info("FocalPortBlockEntity is null");
            }
        }
    }
}
