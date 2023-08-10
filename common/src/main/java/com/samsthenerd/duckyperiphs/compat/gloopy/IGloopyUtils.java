package com.samsthenerd.duckyperiphs.compat.gloopy;

import at.petrak.hexcasting.api.casting.iota.Iota;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public interface IGloopyUtils {
    public boolean isGloopy();

    public boolean goesInFocalPort(ItemStack stack);

    public NbtCompound getIotaNbt(ItemStack stack);

    public boolean writeIota(ItemStack stack, Iota iota, boolean simulate);

    public int pageCount(ItemStack stack);

    // -1 if not handled by this
    public int getPage(ItemStack stack);

    // returns new page
    public int setPage(ItemStack stack, int goal);

    // really is it *good* gloop dye
    public boolean isGloopDye(ItemStack stack);

    public void useGloopMedia(ItemStack stack);

    public int getGloopDyeColor(ItemStack stack);
}
