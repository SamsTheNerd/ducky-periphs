package com.samsthenerd.duckyperiphs.peripherals.InventoryManager;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;
import com.samsthenerd.duckyperiphs.peripherals.IPeripheralTileDucky;

import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

// ported from Advanced Peripherals InventoryManager (Apache-2.0)
// https://github.com/Seniorendi/AdvancedPeripherals
// Making this Peripheral Cross Mod Loader Available
public class InventoryManagerTile extends BlockEntity implements IPeripheralTileDucky, SidedInventory, NamedScreenHandlerFactory {

    private final DefaultedList<ItemStack> cardSlot = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private UUID ownerId = null;
    private InventoryManagerPeripheral peripheral = null;

    public InventoryManagerTile(BlockPos pos, BlockState state){
        super(DuckyPeriphs.INVENTORY_MANAGER_TILE.get(), pos, state);
    }

    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull Direction side){
        if(peripheral == null)
            peripheral = new InventoryManagerPeripheral(this);
        return peripheral;
    }

    @Nullable
    public PlayerEntity getOwnerPlayer(){
        if(ownerId == null || world == null || world.getServer() == null)
            return null;
        return world.getServer().getPlayerManager().getPlayer(ownerId);
    }

    @Override
    public void readNbt(NbtCompound nbt){
        super.readNbt(nbt);
        Inventories.readNbt(nbt, cardSlot);
        ownerId = nbt.containsUuid("ownerId") ? nbt.getUuid("ownerId") : null;
    }

    @Override
    protected void writeNbt(NbtCompound nbt){
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, cardSlot);
        if(ownerId != null)
            nbt.putUuid("ownerId", ownerId);
    }


    @Override
    public int size(){
        return 1;
    }

    @Override
    public boolean isEmpty(){
        return cardSlot.get(0).isEmpty();
    }

    @Override
    public ItemStack getStack(int slot){
        return cardSlot.get(0);
    }

    @Override
    public ItemStack removeStack(int slot, int amount){
        ItemStack removed = Inventories.splitStack(cardSlot, slot, amount);
        if(!removed.isEmpty()){
            markDirty();
        }
        return removed;
    }

    @Override
    public ItemStack removeStack(int slot){
        return Inventories.removeStack(cardSlot, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack){
        if(stack.getItem() instanceof MemoryCardItem){
            NbtCompound nbt = stack.getNbt();
            if(nbt != null && nbt.containsUuid(MemoryCardItem.OWNER_ID_KEY)){
                ownerId = nbt.getUuid(MemoryCardItem.OWNER_ID_KEY);
                // the manager consumes the binding off of the card, like AP does with its data component
                nbt.remove(MemoryCardItem.OWNER_ID_KEY);
                nbt.remove(MemoryCardItem.OWNER_NAME_KEY);
            } else if(!ItemStack.areEqual(stack, cardSlot.get(0))){
                ownerId = null;
            }
        } else {
            ownerId = null;
        }
        cardSlot.set(slot, stack);
        markDirty();
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player){
        return canPlayerUse(player, 4.5);
    }

    private boolean canPlayerUse(PlayerEntity player, double range){
        if(world.getBlockEntity(pos) != this) return false;
        return player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= range * range;
    }

    @Override
    public void clear(){
        cardSlot.clear();
        ownerId = null;
        markDirty();
    }

    @Override
    public int[] getAvailableSlots(Direction side){
        return new int[]{0};
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir){
        return stack.getItem() instanceof MemoryCardItem;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir){
        return true;
    }


    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player){
        return new InventoryManagerScreenHandler(syncId, playerInventory, this);
    }

    @Override
    public Text getDisplayName(){
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }
}
