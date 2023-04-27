// package com.samsthenerd.duckyperiphs.peripherals.strongModem;

// import static dan200.computercraft.shared.peripheral.modem.wired.BlockWiredModemFull.MODEM_ON;
// import static dan200.computercraft.shared.peripheral.modem.wired.BlockWiredModemFull.PERIPHERAL_ON;

// import java.util.Collections;
// import java.util.HashMap;
// import java.util.Map;

// import javax.annotation.Nonnull;
// import javax.annotation.Nullable;

// import com.google.common.base.Objects;
// import com.samsthenerd.duckyperiphs.DuckyPeriphs;

// import dan200.computercraft.api.ComputerCraftAPI;
// import dan200.computercraft.api.network.wired.IWiredElement;
// import dan200.computercraft.api.network.wired.IWiredNode;
// import dan200.computercraft.api.peripheral.IPeripheral;
// import dan200.computercraft.api.peripheral.IPeripheralTile;
// import dan200.computercraft.shared.command.text.ChatHelpers;
// import dan200.computercraft.shared.common.TileGeneric;
// import dan200.computercraft.shared.peripheral.modem.ModemState;
// import dan200.computercraft.shared.peripheral.modem.wired.WiredModemElement;
// import dan200.computercraft.shared.peripheral.modem.wired.WiredModemLocalPeripheral;
// import dan200.computercraft.shared.peripheral.modem.wired.WiredModemPeripheral;
// import dan200.computercraft.shared.util.DirectionUtil;
// import dan200.computercraft.shared.util.TickScheduler;
// import net.minecraft.block.BlockState;
// import net.minecraft.block.entity.BlockEntityType;
// import net.minecraft.entity.player.PlayerEntity;
// import net.minecraft.nbt.NbtCompound;
// import net.minecraft.text.MutableText;
// import net.minecraft.text.Text;
// import net.minecraft.util.ActionResult;
// import net.minecraft.util.Hand;
// import net.minecraft.util.hit.BlockHitResult;
// import net.minecraft.util.math.BlockPos;
// import net.minecraft.util.math.Direction;
// import net.minecraft.util.math.Vec3d;
// import net.minecraft.world.World;

// // copied from TileWiredModemFull. would just extend but need to change some stuff like constructor
// public class StrongModemBlockEntity extends TileGeneric implements IPeripheralTile
// {
//     private static final String NBT_PERIPHERAL_ENABLED = "PeripheralAccess";

//     private static final class FullElement extends WiredModemElement
//     {
//         private final StrongModemBlockEntity entity;

//         private FullElement( StrongModemBlockEntity entity )
//         {
//             this.entity = entity;
//         }

//         @Override
//         protected void attachPeripheral( String name, IPeripheral peripheral )
//         {
//             if( entity.modem != null ) entity.modem.attachPeripheral( name, peripheral );
//         }

//         @Override
//         protected void detachPeripheral( String name )
//         {
//             if( entity.modem != null ) entity.modem.detachPeripheral( name );
//         }

//         @Nonnull
//         @Override
//         public World getLevel()
//         {
//             return entity.getWorld();
//         }

//         @Nonnull
//         @Override
//         public Vec3d getPosition()
//         {
//             return Vec3d.ofCenter( entity.getPos() );
//         }
//     }

//     private WiredModemPeripheral modem = null;

//     private boolean peripheralAccessAllowed = false;
//     private WiredModemLocalPeripheral peripheral;

//     private boolean destroyed = false;
//     private boolean connectionsFormed = false;

//     private final ModemState modemState = new ModemState( () -> TickScheduler.schedule( this ) );
//     private final WiredModemElement element = new FullElement( this );
//     private final IWiredNode node = element.getNode();

//     private int invalidSides = 0;

//     public StrongModemBlockEntity( BlockEntityType<StrongModemBlockEntity> type, BlockPos pos, BlockState state )
//     {
//         super( type, pos, state );
//         Direction facing = state.get(StrongModemBlock.FACING);
//         if(facing == null) {
//             peripheral = new WiredModemLocalPeripheral( () -> queueRefreshPeripheral( Direction.NORTH ) );
//         } else {
//             peripheral = new WiredModemLocalPeripheral( () -> queueRefreshPeripheral( facing ) );
//         }
//     }

//     public StrongModemBlockEntity(BlockPos pos, BlockState state){
//         this(DuckyPeriphs.STRONG_MODEM_BLOCK_ENTITY, pos, state);
//     }

//     @Nonnull
//     public Direction getFacing(BlockPos pos){
//         if(world == null) return Direction.NORTH;
//         Direction facing = world.getBlockState(pos).get(StrongModemBlock.FACING);
//         if(facing == null) return Direction.NORTH;
//         return facing;
//     }

//     private void doRemove()
//     {
//         if( world == null || !world.isClient )
//         {
//             node.remove();
//             connectionsFormed = false;
//         }
//     }

//     @Override
//     public void destroy()
//     {
//         if( !destroyed )
//         {
//             destroyed = true;
//             doRemove();
//         }
//         super.destroy();
//     }

//     @Override
//     public void onChunkUnloaded()
//     {
//         super.onChunkUnloaded();
//         doRemove();
//     }

//     @Override
//     public void markRemoved()
//     {
//         super.markRemoved();
//         doRemove();
//     }

//     @Override
//     public void onNeighbourChange( @Nonnull BlockPos neighbour )
//     {
//         onNeighbourTileEntityChange( neighbour );
//     }

//     @Override
//     public void onNeighbourTileEntityChange( @Nonnull BlockPos neighbour )
//     {
//         if( !world.isClient && peripheralAccessAllowed )
//         {
//             Direction facing = getFacing(getPos());
//             if( getPos().offset( facing ).equals( neighbour ) ) queueRefreshPeripheral( facing );
            
//         }
//     }

//     private void queueRefreshPeripheral( @Nonnull Direction facing )
//     {
//         if( invalidSides == 0 ) TickScheduler.schedule( this );
//         invalidSides |= 1 << facing.ordinal();
//     }

//     private void refreshPeripheral( @Nonnull Direction facing )
//     {
//         invalidSides &= ~(1 << facing.ordinal());
//         if( world != null && !isRemoved() && peripheral.attach( world, getPos().offset(facing), facing ) )
//         {
//             updateConnectedPeripherals();
//         }
//     }

//     @Nonnull
//     @Override
//     public ActionResult onActivate( PlayerEntity player, Hand hand, BlockHitResult hit )
//     {
//         if( player.isInSneakingPose() || !player.canModifyBlocks() ) return ActionResult.PASS;
//         if( getWorld().isClient ) return ActionResult.SUCCESS;

//         // On server, we interacted if a peripheral was found
//         String oldPeriphName = getConnectedPeripheralName();
//         togglePeripheralAccess();
//         String periphName = getConnectedPeripheralName();

//         if( !Objects.equal( periphName, oldPeriphName ) )
//         {
//             sendPeripheralChanges( player, "chat.computercraft.wired_modem.peripheral_disconnected", oldPeriphName );
//             sendPeripheralChanges( player, "chat.computercraft.wired_modem.peripheral_connected", periphName );
//         }

//         return ActionResult.SUCCESS;
//     }

//     private static void sendPeripheralChanges( PlayerEntity player, String kind, String peripheralName )
//     {
//         if( peripheralName == null ) return;

//         MutableText base = Text.literal( "" );
//         base.append( ChatHelpers.copy( peripheralName ) );

//         player.sendMessage( Text.translatable( kind, base ), false );
//     }

//     @Override
//     public void readNbt( @Nonnull NbtCompound nbt )
//     {
//         super.readNbt( nbt );
//         peripheralAccessAllowed = nbt.getBoolean( NBT_PERIPHERAL_ENABLED );
//         peripheral.read( nbt, Integer.toString( 0 ) );
//     }

//     @Override
//     public void writeNbt( NbtCompound nbt )
//     {
//         nbt.putBoolean( NBT_PERIPHERAL_ENABLED, peripheralAccessAllowed );
//         peripheral.write( nbt, Integer.toString( 0 ) );
//         super.writeNbt( nbt );
//     }

//     private void updateBlockState()
//     {
//         BlockState state = getCachedState();
//         boolean modemOn = modemState.isOpen(), peripheralOn = peripheralAccessAllowed;
//         if( state.get( MODEM_ON ) == modemOn && state.get( PERIPHERAL_ON ) == peripheralOn ) return;

//         getWorld().setBlockState( getPos(), state.with( MODEM_ON, modemOn ).with( PERIPHERAL_ON, peripheralOn ) );
//     }

//     @Override
//     public void cancelRemoval()
//     {
//         super.cancelRemoval(); // TODO: Replace with onLoad
//         TickScheduler.schedule( this );
//     }

//     @Override
//     public void blockTick()
//     {
//         if( getWorld().isClient ) return;

//         if( invalidSides != 0 )
//         {
//             for( Direction direction : DirectionUtil.FACINGS )
//             {
//                 if( (invalidSides & (1 << direction.ordinal())) != 0 ) refreshPeripheral( direction );
//             }
//         }

//         if( modemState.pollChanged() ) updateBlockState();

//         if( !connectionsFormed )
//         {
//             connectionsFormed = true;

//             connectionsChanged();
//             if( peripheralAccessAllowed )
//             {
//                 peripheral.attach( world, getPos(), getFacing(getPos()) );
//                 updateConnectedPeripherals();
//             }
//         }
//     }

//     private void connectionsChanged()
//     {
//         if( getWorld().isClient ) return;

//         World world = getWorld();
//         BlockPos current = getPos();
//         Direction facing = getFacing(current);
//         BlockPos offset = current.offset( facing, 2);
//         if( !world.canSetBlock( offset ) ) return;

//         IWiredElement element = ComputerCraftAPI.getWiredElementAt( world, offset, facing.getOpposite() );
//         if( element == null ) return;

//         node.connectTo( element.getNode() );
//     }

//     private void togglePeripheralAccess()
//     {
//         if( !peripheralAccessAllowed )
//         {
//             boolean hasAny = false;
//             peripheral.attach( world, getPos(), getFacing(getPos()) );
//             hasAny |= peripheral.hasPeripheral();

//             if( !hasAny ) return;

//             peripheralAccessAllowed = true;
//             node.updatePeripherals( getConnectedPeripherals() );
//         }
//         else
//         {
//             peripheralAccessAllowed = false;

//             peripheral.detach();
//             node.updatePeripherals( Collections.emptyMap() );
//         }

//         updateBlockState();
//     }

//     private String getConnectedPeripheralName()
//     {
//         if( !peripheralAccessAllowed ) return null;
//         String name = peripheral.getConnectedName();
//         if(name != null) return name;
//         return null;
//     }

//     private Map<String, IPeripheral> getConnectedPeripherals()
//     {
//         if( !peripheralAccessAllowed )
//         {
//             return null;
//         }

//         Map<String, IPeripheral> peripherals = new HashMap<>( 6 );
//         peripheral.extendMap( peripherals );
//         return peripherals;
//     }

//     private void updateConnectedPeripherals()
//     {
//         Map<String, IPeripheral> peripherals = getConnectedPeripherals();
//         if( peripherals.isEmpty() )
//         {
//             // If there are no peripherals then disable access and update the display state.
//             peripheralAccessAllowed = false;
//             updateBlockState();
//         }

//         node.updatePeripherals( peripherals );
//     }

//     public IWiredElement getElement()
//     {
//         return element;
//     }

//     @Nullable
//     @Override
//     public IPeripheral getPeripheral( @Nonnull Direction side )
//     {
//         if( modem != null ) return modem;

//         WiredModemLocalPeripheral localPeripheral = peripheral;
//         return modem = new WiredModemPeripheral( modemState, element )
//         {
//             @Nonnull
//             @Override
//             protected WiredModemLocalPeripheral getLocalPeripheral()
//             {
//                 return localPeripheral;
//             }

//             @Nonnull
//             @Override
//             public Vec3d getPosition()
//             {
//                 return Vec3d.ofCenter( getPos().offset( side, 2) );
//             }

//             @Nonnull
//             @Override
//             public Object getTarget()
//             {
//                 return StrongModemBlockEntity.this;
//             }
//         };
//     }
// }
