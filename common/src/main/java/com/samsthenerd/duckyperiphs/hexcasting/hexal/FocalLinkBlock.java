package com.samsthenerd.duckyperiphs.hexcasting.hexal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
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

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type){
        return checkType(type, DuckyHexal.FOCAL_LINK_BLOCK_ENTITY.get(), (worldin, pos, statein, be) -> FocalLinkBlockEntity.tick(worldin, pos, statein, be));
    }


    // yoinked from block relay
    @Override
    @NotNull
    public ActionResult onUse(@NotNull BlockState state, @NotNull World level, @NotNull BlockPos pos, @NotNull PlayerEntity player, @NotNull Hand hand, @NotNull BlockHitResult hit) {
        if (!(state.getBlock() instanceof FocalLinkBlock)) {
            return ActionResult.PASS;
        } else {
            BlockEntity be = level.getBlockEntity(pos);
            if ((be instanceof FocalLinkBlockEntity ? (FocalLinkBlockEntity)be : null) == null) {
                return ActionResult.PASS;
            } else {
                FocalLinkBlockEntity fl = (FocalLinkBlockEntity)be;
                ItemStack stack = player.getStackInHand(hand).copy();
                if (!IXplatAbstractions.INSTANCE.isPigment(stack)) {
                    return ActionResult.PASS;
                } else {
                    if(stack != null && player.getStackInHand(hand).getCount() > 0) {
                        player.getStackInHand(hand).decrement(1);
                        fl.setColorizer(new FrozenPigment(stack, player.getUuid()));
                        return ActionResult.SUCCESS;
                    } else {
                        return ActionResult.FAIL;
                    }
                }
            }
        }
    }
}
