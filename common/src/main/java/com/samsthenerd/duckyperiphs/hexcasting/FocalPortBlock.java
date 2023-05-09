package com.samsthenerd.duckyperiphs.hexcasting;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

public class FocalPortBlock extends BlockWithEntity{
    public FocalPortBlock(Block.Settings settings) {
        super(settings.nonOpaque());
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
                DuckyPeriphs.LOGGER.info("FocalPortBlockEntity is null");
            }
        }
    }

    @Nonnull
    @Override
    public final ActionResult onUse( @Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockHitResult hit ){
        BlockEntity tile = world.getBlockEntity( pos );
        return tile instanceof FocalPortBlockEntity fpTile ? fpTile.onActivate( player, hand, hit ) : ActionResult.PASS;
    }

    public static int getColor(BlockRenderView world, BlockPos pos){
        Optional<FocalPortBlockEntity> be = world.getBlockEntity(pos, DuckyCasting.FOCAL_PORT_BLOCK_ENTITY.get());
        if(be.isPresent()){
            return be.get().getColor();
        } else {
            return 0;
        }
    }

    // copied from BlockGeneric
    @Override
    @Deprecated
    public final void onStateReplaced( @Nonnull BlockState block, @Nonnull World world, @Nonnull BlockPos pos, BlockState replace, boolean bool )
    {
        if( block.getBlock() == replace.getBlock() ) return;

        BlockEntity tile = world.getBlockEntity( pos );
        super.onStateReplaced( block, world, pos, replace, bool );
        world.removeBlockEntity( pos );
        if( tile instanceof FocalPortBlockEntity fpTile ) fpTile.destroy();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state )
    {
        return new FocalPortBlockEntity(pos, state);
    }

    @Nonnull
    @Override
    public BlockRenderType getRenderType( @Nonnull BlockState state )
    {
        return BlockRenderType.MODEL;
    }
}
