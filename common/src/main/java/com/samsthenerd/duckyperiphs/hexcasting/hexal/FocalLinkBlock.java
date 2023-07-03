package com.samsthenerd.duckyperiphs.hexcasting.hexal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

public class FocalLinkBlock extends BlockWithEntity{
    public FocalLinkBlock(Block.Settings settings) {
        super(settings.nonOpaque());
        setDefaultState( getStateManager().getDefaultState());
    }

    public static int getColor(BlockRenderView world, BlockPos pos){
        // Optional<FocalPortBlockEntity> be = world.getBlockEntity(pos, DuckyCasting.FOCAL_PORT_BLOCK_ENTITY.get());
        // if(be.isPresent()){
        //     return be.get().getColor();
        // } else {
        //     return 0;
        // }
        return 0;
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
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state )
    {
        return new FocalLinkBlockEntity(pos, state);
    }

    @Nonnull
    @Override
    public BlockRenderType getRenderType( @Nonnull BlockState state )
    {
        return BlockRenderType.MODEL;
    }
}
