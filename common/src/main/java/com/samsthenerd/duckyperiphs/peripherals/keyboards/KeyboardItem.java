package com.samsthenerd.duckyperiphs.peripherals.keyboards;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class KeyboardItem extends BlockItem {
    public KeyboardItem (Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        KeyCaps keyCaps = KeyCaps.fromItemStack(stack);
        for(KeyCraftingZone kz : KeyCraftingZone.values()){
            Text keyText = keyCaps.getDisplayText(kz);
            if(keyText != null)
                tooltip.add(keyText);
        }
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
