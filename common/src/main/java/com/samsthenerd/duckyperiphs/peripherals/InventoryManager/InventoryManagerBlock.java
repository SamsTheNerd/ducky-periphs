package com.samsthenerd.duckyperiphs.peripherals.InventoryManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

// ported from Advanced Peripherals InventoryManager (Apache-2.0)
// https://github.com/Seniorendi/AdvancedPeripherals
// Making this Peripheral Cross Mod Loader Available
public class InventoryManagerBlock extends BlockWithEntity {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public InventoryManagerBlock(Block.Settings settings){
        super(settings);
        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder){
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx){
        return getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
    }

    @Nonnull
    @Override
    public ActionResult onUse(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockHitResult hit){
        if(!world.isClient){
            NamedScreenHandlerFactory factory = state.createScreenHandlerFactory(world, pos);
            if(factory != null){
                MenuRegistry.openExtendedMenu((ServerPlayerEntity) player, factory, buf -> {});
            }
        }
        return ActionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state){
        return new InventoryManagerTile(pos, state);
    }

    @Nonnull
    @Override
    public BlockRenderType getRenderType(@Nonnull BlockState state){
        return BlockRenderType.MODEL;
    }
}
