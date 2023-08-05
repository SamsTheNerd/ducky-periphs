package com.samsthenerd.duckyperiphs.peripherals.keyboards;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class KeyboardScreenHandler extends ScreenHandler{


    public BlockPos pos;

    public KeyboardScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(DuckyPeriphs.KEYBOARD_SCREEN_HANDLER, syncId);
        DuckyPeriphs.logPrint("kbScreenHandler created with buf: " + buf);
        pos = buf.readBlockPos();
    }

    public KeyboardScreenHandler(int syncId, PlayerInventory inv, BlockPos pos){
        super(DuckyPeriphs.KEYBOARD_SCREEN_HANDLER, syncId);
        DuckyPeriphs.logPrint("kbScreenHandler created with pos: " + pos);
        this.pos = pos;
    }


    public KeyboardScreenHandler(int syncId, PlayerInventory playerInventory){
        super(DuckyPeriphs.KEYBOARD_SCREEN_HANDLER, syncId);
        DuckyPeriphs.logPrint("kbScreenHandler created with none");
        pos = BlockPos.ORIGIN;
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        // maybe have a check in our blockentity for if anyone is currently using it?
        // ooh or could add a lock feature to it?
        return true;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        return ItemStack.EMPTY;
    }

    // // should be able to call through KeyboardScreen 
    // public void pushKeyPressThroughContext(int key){
    //     logPrint("kbScreen pushed " + key + " to kbsHandler. context: " + (this.context != ScreenHandlerContext.EMPTY ? "yep" : "null"));
    //     this.context.run((world, pos) -> {
    //         logPrint("running with context");
    //         KeyboardTile kbTile = world.getBlockEntity(pos, DuckyPeriph.KEYBOARD_TILE).orElse(null);
    //         if(kbTile != null){
    //             kbTile.kbPeriph.sendKey(key);
    //         }
            
    //     });
    // }

    public static class KeyboardScreenHandlerFactory implements NamedScreenHandlerFactory {
        private final KeyboardTile tile;

        public KeyboardScreenHandlerFactory(KeyboardTile tile) {
            this.tile = tile;
        }

        @Override
        public Text getDisplayName(){
            return tile.getDisplayName();
        }

        @Override
        public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
            return new KeyboardScreenHandler(syncId, inv, tile.getPos());
        }
    }

}
