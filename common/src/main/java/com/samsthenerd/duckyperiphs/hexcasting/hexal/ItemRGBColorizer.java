package com.samsthenerd.duckyperiphs.hexcasting.hexal;

import java.util.UUID;

import at.petrak.hexcasting.api.item.PigmentItem;
import at.petrak.hexcasting.api.pigment.ColorProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.math.Vec3d;

public class ItemRGBColorizer extends Item implements PigmentItem{
    public ItemRGBColorizer(Item.Settings settings){
        super(settings);
    }

    // public ItemStack getDefaultStack() {
    //     ItemStack stack = new ItemStack(this);
    //     NbtCompound tag = new NbtCompound();
    //     tag.putLong("argb", 0xFF_FFFFFF);
    //     stack.setNbt(tag);
    //     return stack;
    // }

    public ItemStack stackFromRGB(int argb){
        ItemStack stack = new ItemStack(this);
        NbtCompound tag = new NbtCompound();
        tag.putInt("argb", argb);
        stack.setNbt(tag);
        return stack;
    }

    @Override
    public ColorProvider provideColor(ItemStack stack, UUID owner){
        return new RGBColorProvider(stack, owner);
    }

    public static int getRGB(ItemStack stack){
        NbtCompound tag = stack.getNbt();
        if(tag != null && tag.contains("argb", NbtElement.INT_TYPE)){
            return tag.getInt("argb");
        }
        return -1;
    }

    public static class RGBColorProvider extends ColorProvider{
        private ItemStack stack;
        private UUID owner;

        public RGBColorProvider(ItemStack stack, UUID owner){
            this.stack = stack;
            this.owner = owner;
        }

        @Override
        public int getRawColor(float time, Vec3d position){
            NbtCompound tag = stack.getNbt();
            if(tag != null && tag.contains("argb", NbtElement.INT_TYPE)){
                return tag.getInt("argb");
            }
            return 0xFF_FFFFFF;
        }
    }
}
