package com.samsthenerd.duckyperiphs.peripherals.InventoryManager;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

// ported from Advanced Peripherals InventoryManager (Apache-2.0)
// https://github.com/Seniorendi/AdvancedPeripherals
// Making this Peripheral Cross Mod Loader Available
public class InventoryManagerScreenHandler extends ScreenHandler {

    private final Inventory cardInventory;

    public InventoryManagerScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf){
        this(syncId, playerInventory, new SimpleInventory(1));
    }

    public InventoryManagerScreenHandler(int syncId, PlayerInventory playerInventory, Inventory cardInventory){
        super(DuckyPeriphs.INVENTORY_MANAGER_SCREEN_HANDLER, syncId);
        this.cardInventory = cardInventory;
        cardInventory.onOpen(playerInventory.player);

        this.addSlot(new Slot(cardInventory, 0, 79, 29));
        for(int row = 0; row < 3; row++){
            for(int col = 0; col < 9; col++){
                this.addSlot(new Slot(playerInventory, 9 + col + row * 9, 7 + col * 18, 84 + row * 18));
            }
        }
        for(int col = 0; col < 9; col++){
            this.addSlot(new Slot(playerInventory, col, 7 + col * 18, 142));
        }
    }

    @Override
    public boolean canUse(PlayerEntity player){
        return this.cardInventory.canPlayerUse(player);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slotIndex){
        Slot slot = this.slots.get(slotIndex);
        if(slot == null || !slot.hasStack())
            return ItemStack.EMPTY;
        ItemStack original = slot.getStack();
        ItemStack copy = original.copy();
        if(slotIndex == 0){
            if(!this.insertItem(original, 1, this.slots.size(), true))
                return ItemStack.EMPTY;
        } else {
            if(!this.insertItem(original, 0, 1, false))
                return ItemStack.EMPTY;
        }
        if(original.getCount() == copy.getCount())
            return ItemStack.EMPTY;
        slot.onTakeItem(player, original);
        return copy;
    }

    @Override
    public void onClosed(PlayerEntity player){
        super.onClosed(player);
        this.cardInventory.onClose(player);
    }
}
