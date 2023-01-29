package net.fabricmc.duckyperiphs.ducks;

import net.fabricmc.duckyperiphs.DuckyPeriph;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class DuckItem extends BlockItem implements DyeableItem{
    public static final int DEFAULT_COLOR = 16701501;
    public DuckItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    public int getColor(ItemStack stack) {
        NbtCompound nbtCompound = stack.getSubNbt(DISPLAY_KEY);
        if (nbtCompound != null && nbtCompound.contains(COLOR_KEY, 99)) {
            return nbtCompound.getInt(COLOR_KEY);
        }
        return DEFAULT_COLOR;
    }

    public int getRepairCost(ItemStack stack){
        NbtCompound nbtCompound = stack.getNbt();
        if(nbtCompound != null && nbtCompound.contains("RepairCost", 99)){
            return nbtCompound.getInt("RepairCost");
        }
        return 1000000;
    }

    public Boolean hasRepairCost(ItemStack stack){
        NbtCompound nbtCompound = stack.getNbt();
        if(nbtCompound != null && nbtCompound.contains("RepairCost", 99)){
            return true;
        }
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if(!world.isClient){
            float pitch = (float) (Math.random() * 0.2 + 0.9);
            world.emitGameEvent(player, DuckyPeriph.QUACK_GAME_EVENT, player.getPos());
            world.playSound(null, player.getBlockPos(), DuckyPeriph.QUACK_SOUND_EVENT, SoundCategory.BLOCKS, 1f, pitch);
        }
        // return super.use(world, player, hand);
        return TypedActionResult.success(player.getStackInHand(hand)); // for squeeze 
    }

    // // static so we can get it from block
    // public static int getColor(ItemStack itemStack) {

    // }
}
