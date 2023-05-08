package com.samsthenerd.duckyperiphs.hexcasting;

import org.jetbrains.annotations.NotNull;

import com.samsthenerd.duckyperiphs.ducks.DuckBlock;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class ConjuredDuckyBlock extends Block implements BlockEntityProvider{
    public static final DirectionProperty FACING = FacingBlock.FACING;
    public static final BooleanProperty VISIBLE = BooleanProperty.of("visible");

    public ConjuredDuckyBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH).with(VISIBLE, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(VISIBLE);
    }

    @Override
    public ConjuredDuckyBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ConjuredDuckyBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return DuckBlock.rotateShape(Direction.EAST, direction, DuckBlock.SHAPE);
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public @Nonnull BlockRenderType getRenderType(@Nonnull BlockState state) {
        if(state.get(VISIBLE)){
            return BlockRenderType.MODEL;
        } else {
            return BlockRenderType.INVISIBLE;
        }
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getPlayerFacing().getOpposite();
        if(direction == Direction.DOWN || direction == Direction.UP)
            direction = Direction.NORTH;
        return (BlockState)this.getDefaultState().with(FACING, direction);
    }

    // taken from BlockConjured
    public static void setColor(WorldAccess pLevel, BlockPos pPos, FrozenColorizer colorizer) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof ConjuredDuckyBlockEntity tile) {
            tile.setColorizer(colorizer);
        }
    }
}