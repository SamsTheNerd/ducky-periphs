package com.samsthenerd.duckyperiphs.hexcasting;

import javax.annotation.Nonnull;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;
import com.samsthenerd.duckyperiphs.ducks.DuckBlock;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
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
    public @Nonnull BlockRenderType getRenderType(BlockState state) {
        if(state.get(VISIBLE)){
            return BlockRenderType.MODEL;
        } else {
            return BlockRenderType.INVISIBLE;
        }
    }

    // might not be right ? - was isTranslucent but i can't find that
    @Override
    public boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getHorizontalPlayerFacing().getOpposite();
        if(direction == Direction.DOWN || direction == Direction.UP)
            direction = Direction.NORTH;
        return (BlockState)this.getDefaultState().with(FACING, direction);
    }

    // taken from BlockConjured
    public static void setColor(WorldAccess pLevel, BlockPos pPos, FrozenPigment colorizer) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        if (blockentity instanceof ConjuredDuckyBlockEntity tile) {
            tile.setColorizer(colorizer);
        }
    }

    public static void quack(World world, BlockPos pos){
        if(!world.isClient){
            float pitch = (float) (Math.random() * 0.2 + 0.9);
            world.emitGameEvent(null, DuckyPeriphs.HEXXY_QUACK_GAME_EVENT.get(), pos);
            world.playSound(null, pos, DuckyPeriphs.HEXXY_QUACK_SOUND_EVENT.get(), SoundCategory.BLOCKS, 1f, pitch);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        quack(world, pos);
        return ActionResult.CONSUME;
    }
}