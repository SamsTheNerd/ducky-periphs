package net.fabricmc.duckyperiphs.peripherals.keyboards;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.duckyperiphs.DuckyPeriph;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;

public class KeyboardScreenHandler extends ScreenHandler{
	public static final Logger LOGGER = LoggerFactory.getLogger("ducky-periphs");


    public BlockPos pos;

    public KeyboardScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(DuckyPeriph.KEYBOARD_SCREEN_HANDLER, syncId);
        pos = buf.readBlockPos();
    }


    public KeyboardScreenHandler(int syncId, PlayerInventory playerInventory){
        super(DuckyPeriph.KEYBOARD_SCREEN_HANDLER, syncId);
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
    //     LOGGER.info("kbScreen pushed " + key + " to kbsHandler. context: " + (this.context != ScreenHandlerContext.EMPTY ? "yep" : "null"));
    //     this.context.run((world, pos) -> {
    //         LOGGER.info("running with context");
    //         KeyboardTile kbTile = world.getBlockEntity(pos, DuckyPeriph.KEYBOARD_TILE).orElse(null);
    //         if(kbTile != null){
    //             kbTile.kbPeriph.sendKey(key);
    //         }
            
    //     });
    // }


}
