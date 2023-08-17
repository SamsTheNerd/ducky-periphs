package com.samsthenerd.duckyperiphs.ducks;

import java.util.List;

import javax.annotation.Nullable;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import dev.architectury.platform.Platform;
import net.minecraft.block.Block;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;


public class DuckItem extends BlockItem implements DyeableItem{
    public static final int DEFAULT_COLOR = 16701501;
    public DuckItem(Block block, Settings settings) {
        super(block, settings);
		CauldronBehavior.WATER_CAULDRON_BEHAVIOR.put(this, CauldronBehavior.CLEAN_DYEABLE_ITEM);
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
            world.emitGameEvent(player, DuckyPeriphs.QUACK_GAME_EVENT.get(), player.getPos());
            world.playSound(null, player.getBlockPos(), DuckyPeriphs.QUACK_SOUND_EVENT.get(), SoundCategory.BLOCKS, 1f, pitch);
        }
        // return super.use(world, player, hand);
        return TypedActionResult.success(player.getStackInHand(hand)); // for squeeze 
    }

    public static ItemStack getDuckItemStack(int color){
        ItemStack stack = new ItemStack(DuckyPeriphs.DUCK_ITEM.get());
        NbtCompound nbtCompound = stack.getOrCreateSubNbt(DISPLAY_KEY);
        nbtCompound.putInt(COLOR_KEY, color);
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(Platform.isModLoaded("hexcasting")){
            if(Platform.isModLoaded("hexgloop")){
                tooltip.add(((MutableText)Text.of("<ne,aqadweeeede>")).formatted(Formatting.ITALIC, Formatting.GRAY));
            } else {
                tooltip.add(((MutableText)Text.of("I have the strangest urge to cast scribe's reflection?")).formatted(Formatting.ITALIC, Formatting.GRAY));
            }
        }
    }

    // // static so we can get it from block
    // public static int getColor(ItemStack itemStack) {

    // }
}
