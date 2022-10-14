package net.fabricmc.duckyperiphs.peripherals.keyboards;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;

public class KeyboardItem extends BlockItem {
    public KeyboardItem (Block block, Settings settings) {
        super(block, settings);
    }

    // @Override
    // public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
    //     if(!world.isClient){
    //         KeyCaps transKeyboard = KeyboardPresets.transKeyboard();
    //         ItemStack transStack = player.getStackInHand(hand);
    //         transStack.setSubNbt(KeyCaps.KEY_CAP_KEY, transKeyboard.toSubNBT());
    //         player.setStackInHand(hand, transStack);
    //     }
    //     // return super.use(world, player, hand);
    //     return TypedActionResult.success(player.getStackInHand(hand)); // for squeeze 
    // }
}
