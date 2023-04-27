// package com.samsthenerd.duckyperiphs.peripherals.strongModem;

// import com.samsthenerd.duckyperiphs.DuckyPeriphs;

// import dan200.computercraft.shared.common.BlockGeneric;
// import net.minecraft.block.Block;
// import net.minecraft.block.BlockState;
// import net.minecraft.block.FacingBlock;
// import net.minecraft.item.ItemPlacementContext;
// import net.minecraft.state.StateManager;
// import net.minecraft.state.property.BooleanProperty;
// import net.minecraft.state.property.DirectionProperty;
// import net.minecraft.util.math.Direction;

// public class StrongModemBlock extends BlockGeneric
// {
//     public static final BooleanProperty MODEM_ON = BooleanProperty.of( "modem" );
//     public static final BooleanProperty PERIPHERAL_ON = BooleanProperty.of( "peripheral" );
//     public static final DirectionProperty FACING = FacingBlock.FACING;

//     public StrongModemBlock( Settings settings )
//     {
//         super( settings, () -> DuckyPeriphs.STRONG_MODEM_BLOCK_ENTITY );
//         setDefaultState( getStateManager().getDefaultState()
//             .with( MODEM_ON, false )
//             .with( PERIPHERAL_ON, false )
//             .with(FACING, Direction.NORTH)
//         );
//     }

//     @Override
//     protected void appendProperties( StateManager.Builder<Block, BlockState> builder )
//     {
//         builder.add( MODEM_ON, PERIPHERAL_ON, FACING);
//     }

//     @Override
//     public BlockState getPlacementState(ItemPlacementContext ctx) {
//         Direction direction = ctx.getPlayerFacing().getOpposite();
//         return (BlockState)this.getDefaultState().with(FACING, direction);
//     }
// }
