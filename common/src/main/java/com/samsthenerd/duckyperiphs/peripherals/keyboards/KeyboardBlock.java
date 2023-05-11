package com.samsthenerd.duckyperiphs.peripherals.keyboards;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/*
 * This file may be changed around a lot. 
 * Not 100% sure how we should handle having different types of keyboards
 * Might become a more generic abstract superclass with other classes being the final keyboards
 */

public class KeyboardBlock extends BlockWithEntity {
    public static final DirectionProperty FACING = FacingBlock.FACING;

    public KeyboardBlock(Block.Settings settings) {
        super(settings.nonOpaque());
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH));
    }

    // not sure it can open from blockEntity?
    public void openScreen(BlockState state, World world, BlockPos pos, PlayerEntity player){
        if(!world.isClient){
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
            if(screenHandlerFactory != null){
                player.openHandledScreen(screenHandlerFactory);
            }
        }
    }

    @Nonnull
    @Override
    public final ActionResult onUse( @Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockHitResult hit ){
        BlockEntity tile = world.getBlockEntity( pos );
        return tile instanceof KeyboardTile kbTile ? kbTile.onActivate( player, hand, hit ) : ActionResult.PASS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getPlayerFacing().getOpposite();
        if(direction == Direction.DOWN || direction == Direction.UP)
            direction = Direction.NORTH;
        return (BlockState)this.getDefaultState().with(FACING, direction);
    }
    

    public KeyboardTile getKBTile(World world, BlockPos pos){
        return (KeyboardTile)world.getBlockEntity(pos);
    }

    public KeyCaps getKeyCaps(BlockView view, BlockPos pos){
        BlockEntity kb_tile;
        KeyCaps keyCapsToReturn;
        if((kb_tile = view.getBlockEntity(pos)) instanceof KeyboardTile){
            keyCapsToReturn = ((KeyboardTile)kb_tile).keyCaps;
            if(keyCapsToReturn != null){
                return keyCapsToReturn;
            }
        }
        return new KeyCaps();
    }

    // used to put color into block entity
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity;
        if (itemStack.hasCustomName() && (blockEntity = world.getBlockEntity(pos)) instanceof KeyboardTile) {
            ((KeyboardTile)blockEntity).setCustomName(itemStack.getName());
        }
        if((blockEntity = world.getBlockEntity(pos)) instanceof KeyboardTile){
            KeyCaps itemKeyCaps = KeyCaps.fromItemStack(itemStack);
            if(itemKeyCaps != null){
                ((KeyboardTile)blockEntity).keyCaps = itemKeyCaps;
            }
        }
    }

    @Override
    @Deprecated
    public final void neighborUpdate( @Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull Block neighbourBlock, @Nonnull BlockPos neighbourPos, boolean isMoving )
    {
        BlockEntity tile = world.getBlockEntity( pos );
        if( tile instanceof TileGeneric generic ) generic.onNeighbourChange( neighbourPos );
    }

    protected static final VoxelShape SHAPE = Block.createCuboidShape(5, 1, 1, 11, 2, 15);

    // thanks SkylorBeck#7700 on fabric discord for this one (also your videos are cool <3)
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{ shape, VoxelShapes.empty() };

        int times = (to.getHorizontal() - from.getHorizontal() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.combine(buffer[1], VoxelShapes.cuboid(1-maxZ, minY, minX, 1-minZ, maxY, maxX), BooleanBiFunction.OR));
            buffer[0] = buffer[1];
            buffer[1] = VoxelShapes.empty();
        }
        return buffer[0];
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction direction = state.get(FACING);
        return rotateShape(Direction.EAST, direction, SHAPE);
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state )
    {
        return new KeyboardTile(pos, state);
    }

    @Nonnull
    @Override
    public BlockRenderType getRenderType( @Nonnull BlockState state )
    {
        return BlockRenderType.MODEL;
    }
}
