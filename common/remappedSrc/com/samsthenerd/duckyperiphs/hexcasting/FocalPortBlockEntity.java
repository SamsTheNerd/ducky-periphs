package com.samsthenerd.duckyperiphs.hexcasting;

import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.samsthenerd.duckyperiphs.DuckyPeriph;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.NullIota;
import at.petrak.hexcasting.common.lib.HexItems;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachmentBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class FocalPortBlockEntity extends TileGeneric implements IPeripheralTile, ADIotaHolder, RenderAttachmentBlockEntity, Inventory{
    private FocalPortPeripheral fpPeriph;
    private ItemStack innerFocusStack;
    private int iotaColor;
    private FocalPortWrapperEntity wrapperEntity;
    private UUID wrapperEntityUUID;

    public FocalPortBlockEntity(BlockPos pos, BlockState state) {
        super(DuckyCasting.FOCAL_PORT_BLOCK_ENTITY, pos, state);
        DuckyPeriph.LOGGER.info("FocalPortBlockEntity created at " + pos.toString());
        fpPeriph = new FocalPortPeripheral(this);
        iotaColor = NullIota.TYPE.color();
        innerFocusStack = ItemStack.EMPTY;
    }

    // not sure if this is quite right
    public void spawnWrapperEntity(BlockPos pos){
        // check that we have a server world and that we don't already have a wrapper entity
        if(world instanceof ServerWorld == false){
            return;
        }
        if(wrapperEntityUUID != null){
            wrapperEntity = (FocalPortWrapperEntity)(((ServerWorld)world).getEntity(wrapperEntityUUID));
        }
        if(wrapperEntity != null){
            return;
        }
        wrapperEntity = new FocalPortWrapperEntity(DuckyCasting.FOCAL_PORT_WRAPPER_ENTITY, world);
        wrapperEntity = DuckyCasting.FOCAL_PORT_WRAPPER_ENTITY.spawn((ServerWorld)world, null, null, null, pos.subtract(new Vec3i(0, 1, 0)), SpawnReason.TRIGGERED, true, false);
        wrapperEntityUUID = wrapperEntity.getUuid();
        this.markDirty();
    }

    public void despawnWrapperEntity(){
        if(wrapperEntityUUID != null && world instanceof ServerWorld){
            wrapperEntity = (FocalPortWrapperEntity)(((ServerWorld)world).getEntity(wrapperEntityUUID));
        }
        if(wrapperEntity != null){
            wrapperEntity.discard();
            wrapperEntity = null;
        }
    }

    public void resetWrapperEntity(){
        if(wrapperEntity != null){
            BlockPos goalPos = getPos().subtract(new Vec3i(0, 1, 0));
            wrapperEntity.setPosition(goalPos.getX()+0.5, goalPos.getY(), goalPos.getZ()+0.5);
            DuckyPeriph.LOGGER.info("FocalPortBlockEntity: resetWrapperEntity: " + wrapperEntity.getPos().toString());
        }
    }

    @Override
    public void destroy(){
        ItemScatterer.spawn(world, pos, (Inventory)((Object)this));
        despawnWrapperEntity();
        super.destroy();
    }

    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull Direction side){
        if(fpPeriph == null)
            fpPeriph = new FocalPortPeripheral(this);
        return fpPeriph;
    }


    public BlockPos getPos(){
        return this.pos;
    }

    public World getWorld(){
        return this.world;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if(nbt.contains("wrapperEntityUUID")){
            wrapperEntityUUID = nbt.getUuid("wrapperEntityUUID");
        }
        if (nbt != null && nbt.contains("innerFocusStack", 10)) {
            this.innerFocusStack = ItemStack.fromNbt(nbt.getCompound("innerFocusStack"));
        }
        if(nbt != null && nbt.contains("iotaColor", 3)){
            setColor(nbt.getInt("iotaColor"));
            markDirty();
            World world = getWorld();
            if(world != null){
                DuckyPeriph.LOGGER.info("FocalPortBlockEntity: readNbt: iotaColor = " + iotaColor + " on " + (world.isClient ? "client" : "server"));
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if(wrapperEntityUUID != null){
            nbt.putUuid("wrapperEntityUUID", wrapperEntityUUID);
        }
        if((Integer)this.iotaColor != null){
            nbt.putInt("iotaColor", iotaColor);
        }
        if(this.innerFocusStack != null){
            nbt.put("innerFocusStack", innerFocusStack.writeNbt(new NbtCompound()));
        }
    }

    @Override
    public NbtCompound readIotaTag() {
        return HexItems.FOCUS.readIotaTag(innerFocusStack);
    }

    // fromHex is true if we're writing from hex casting, false if we're writing from CC
    public boolean writeIota(@Nullable Iota iota, boolean simulate, boolean fromHex){
        boolean result = writeIota(iota, simulate);
        if(!simulate && result){
            if(fromHex){
                fpPeriph.updateIota();
            }
        }
        return result;
    }

    @Override
    public boolean writeIota(@Nullable Iota iota, boolean simulate){
        if(iota == null || innerFocusStack.isEmpty()
        || !HexItems.FOCUS.canWrite(innerFocusStack, iota)){
            return false;
        }
        if(!simulate){
            HexItems.FOCUS.writeDatum(innerFocusStack, iota);
            setColor(iota.getType().color());
            this.markDirty();
            
        }
        return true;
    }

    public Iota getIota(){
        Iota iota = new NullIota();
        if(!innerFocusStack.isEmpty() && getWorld() instanceof ServerWorld){
            return HexIotaTypes.deserialize(readIotaTag(),(ServerWorld)getWorld());
        }
        setColor(iota.getType().color());
        return iota;
    }

    public boolean hasFocus(){
        return !innerFocusStack.isEmpty();
    }

    private void setColor(int color){
        this.iotaColor = color;
        World world = getWorld();
        if(world != null && !world.isClient){
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    public void updateColor(){
        setColor(getIota().getType().color());
    }

    public int getColor(){
        if(innerFocusStack.isEmpty()){
            return 0xFF808080; // like a really dark gray/light black
        }
        return this.iotaColor;
    }

    @Override
    public Object getRenderAttachmentData(){
        return getColor();
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    // inspired by https://github.com/TibiNonEst/cauldron-dyeing/blob/b2ec18685f6d26aaa755bc4675d27e3e0722d69d/src/main/java/me/tibinonest/mods/cauldron_dyeing/block/WaterCauldronBlockEntity.java
    @Override
    public void markDirty() {
        if (world != null) {
            if (world.isClient()) {
                MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
            } else if (world instanceof ServerWorld) {
                ((ServerWorld) world).getChunkManager().markForUpdate(pos);
            }
            super.markDirty();
        }
    }


    // inventory methods:
    @Override public int size() { return 1; }

    @Override public boolean isEmpty() { return innerFocusStack.isEmpty(); }

    @Override public ItemStack getStack(int slot) { 
        if(slot == 0)
        return innerFocusStack; 
        // else
        return ItemStack.EMPTY;
    }

    @Override public ItemStack removeStack(int slot, int amount) {
        if(slot != 0 || amount != 1){
            return ItemStack.EMPTY;
        }
        return removeStack(slot);
    }

    @Override public ItemStack removeStack(int slot) {
        ItemStack result = innerFocusStack;
        innerFocusStack = ItemStack.EMPTY;
        fpPeriph.detachFocus();
        updateColor();
        markDirty();
        return result;
    }

    @Override public void setStack(int slot, ItemStack stack) {
        if(slot != 0){
            return;
        }
        innerFocusStack = stack;
        fpPeriph.attachFocus();
        updateColor();
        markDirty();
    }

    @Override public void clear() {
        innerFocusStack = ItemStack.EMPTY;
        updateColor();
        markDirty();
    }

    @Override public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }


    @Override
    public ActionResult onActivate(PlayerEntity player, Hand hand, BlockHitResult hit) {
        Optional<FocalPortBlockEntity> beOpt = world.getBlockEntity(pos, DuckyCasting.FOCAL_PORT_BLOCK_ENTITY);
        if(beOpt.isPresent()){
            FocalPortBlockEntity be = beOpt.get();
            if(be.hasFocus()){
                ItemStack oldFocusStack = be.removeStack(0);
                ItemStack newFocusStack = player.getStackInHand(hand);
                if(newFocusStack.getItem() == HexItems.FOCUS && !newFocusStack.isEmpty()){
                    be.setStack(0, newFocusStack);
                    player.setStackInHand(hand, ItemStack.EMPTY);
                }
                if (!player.getInventory().insertStack(oldFocusStack)) {
                    player.dropItem(oldFocusStack, false);
                }
                ActionResult.success(world.isClient);
            } else {
                ItemStack focusStack = player.getStackInHand(hand);
                if(focusStack.getItem() != HexItems.FOCUS){
                    return ActionResult.PASS;
                }
                if(!focusStack.isEmpty()){
                    be.setStack(0, focusStack);
                    player.setStackInHand(hand, ItemStack.EMPTY);
                }
                ActionResult.success(world.isClient);
            }
        }
        return ActionResult.CONSUME;
    }
}
