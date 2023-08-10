package com.samsthenerd.duckyperiphs.compat.gloopy;

import at.petrak.hexcasting.api.casting.iota.Iota;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

// doesn't really matter what the functions do, since we should wrap in a isGloopy check anyways
public class FakeGloopyUtils implements IGloopyUtils{
    public boolean isGloopy(){
        return false;
    }

    public boolean goesInFocalPort(ItemStack stack){
        return false;
    }

    public NbtCompound getIotaNbt(ItemStack stack){
        return null;
    }

    public boolean writeIota(ItemStack stack, Iota iota, boolean simulate){
        return false;
    }

    public int pageCount(ItemStack stack){
        return 0;
    }

    // -1 if not handled by this
    public int getPage(ItemStack stack){
        return 0;
    }

    // returns new page
    public int setPage(ItemStack stack, int goal){
        return 0;
    }

    // really is it *good* gloop dye
    public boolean isGloopDye(ItemStack stack){
        return false;
    }

    public void useGloopMedia(ItemStack stack){
    }

    public int getGloopDyeColor(ItemStack stack){
        return 0;
    }
}
