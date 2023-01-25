package net.fabricmc.duckyperiphs.ducks;


import net.fabricmc.duckyperiphs.DuckyPeriph;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class DuckBlock extends Block implements BlockEntityProvider {
    public static final DirectionProperty FACING = FacingBlock.FACING;
    // just so we can know when to quack
    public static final BooleanProperty POWERED = Properties.POWERED;


    
    public DuckBlock(AbstractBlock.Settings settings) {
        super(settings.nonOpaque());
        this.setDefaultState((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH).with(POWERED, false));
    }
    
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new DuckBlockEntity(pos, state);
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(POWERED);
    }


    // used for color provider
    public int getColor(BlockView view, BlockPos pos) {
        BlockEntity duck_ent;
        if((duck_ent = view.getBlockEntity(pos)) instanceof DuckBlockEntity){
            return ((DuckBlockEntity)duck_ent).getBlockColor();
        }
        return 1000; // change to default color later ?
        
    }

    // used to put color into block entity
    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        BlockEntity blockEntity;
        if (itemStack.hasCustomName() && (blockEntity = world.getBlockEntity(pos)) instanceof DuckBlockEntity) {
            ((DuckBlockEntity)blockEntity).setCustomName(itemStack.getName());
        }
        if((blockEntity = world.getBlockEntity(pos)) instanceof DuckBlockEntity){
            ((DuckBlockEntity)blockEntity).setColor(((DuckItem)itemStack.getItem()).getColor(itemStack));
            if(((DuckItem)itemStack.getItem()).hasRepairCost(itemStack)){
                ((DuckBlockEntity)blockEntity).setRepairCost(((DuckItem)itemStack.getItem()).getRepairCost(itemStack));
            } else {
                ((DuckBlockEntity)blockEntity).setRepairCost(1000000);
            }
            
        }
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getPlayerFacing().getOpposite();
        if(direction == Direction.DOWN || direction == Direction.UP)
            direction = Direction.NORTH;
        return (BlockState)this.getDefaultState().with(FACING, direction).with(POWERED, false);
    }

    // shape time
    // protected static final double[][] SHAPE_COORDS = {{9D, 3D, 6D, 13D, 7D, 10D}, {4D, 0D, 5D, 12D, 4D, 11D},{5D, 1D, 4D, 11D, 4D, 5D},
    //     {5D, 1D, 11D, 11D, 4D, 12D},{13D, 4D, 7D, 14D, 5D, 9D}};
    protected static final VoxelShape head_shape = Block.createCuboidShape(9D, 3D, 6D, 13D, 7D, 10D);
    protected static final VoxelShape body_shape = Block.createCuboidShape(4D, 0D, 5D, 12D, 4D, 11D);
    protected static final VoxelShape rw_shape = Block.createCuboidShape(5D, 1D, 4D, 11D, 4D, 5D);
    protected static final VoxelShape lw_shape = Block.createCuboidShape(5D, 1D, 11D, 11D, 4D, 12D);
    protected static final VoxelShape nose_shape = Block.createCuboidShape(13D, 4D, 7D, 14D, 5D, 9D);
    protected static final VoxelShape SHAPE = VoxelShapes.union(head_shape, body_shape, rw_shape, lw_shape, nose_shape);

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

    public void quack(World world, BlockPos pos){
        if(!world.isClient){
            float pitch = (float) (Math.random() * 0.2 + 0.9);
            world.playSound(null, pos, DuckyPeriph.QUACK_SOUND_EVENT, SoundCategory.BLOCKS, 1f, pitch);
        }
    }

    // mostly just stolen from NoteBlock lol
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (world.isClient) {
            return;
        }
        boolean bl = world.isReceivingRedstonePower(pos);
        if (bl != state.get(POWERED)) {
            if (bl) {
                this.quack(world, pos);
            } else {
                world.setBlockState(pos, (BlockState)state.with(POWERED, bl), Block.NOTIFY_ALL);
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (state.get(POWERED).booleanValue() && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, (BlockState)state.cycle(POWERED), Block.NOTIFY_LISTENERS);
        }
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        this.quack(world, pos);
        return ActionResult.CONSUME;
    }


}
