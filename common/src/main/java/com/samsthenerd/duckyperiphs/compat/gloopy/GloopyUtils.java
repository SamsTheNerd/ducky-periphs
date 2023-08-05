package com.samsthenerd.duckyperiphs.compat.gloopy;

import com.samsthenerd.hexgloop.items.ItemMultiFocus;

import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.common.items.ItemFocus;
import at.petrak.hexcasting.common.items.ItemSpellbook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class GloopyUtils {
    public static boolean goesInFocalPort(ItemStack stack){
        return stack.getItem() instanceof ItemMultiFocus || stack.getItem() instanceof ItemFocus;
    }

    public static NbtCompound getIotaNbt(ItemStack stack){
        if(stack.getItem() instanceof ItemMultiFocus){
            return ((ItemMultiFocus)stack.getItem()).readIotaTag(stack);
        } else if(stack.getItem() instanceof ItemFocus){
            return ((ItemFocus)stack.getItem()).readIotaTag(stack);
        } else {
            return null;
        }
    }

    public static boolean writeIota(ItemStack stack, Iota iota, boolean simulate){
        if(stack.getItem() instanceof ItemMultiFocus){
            boolean canWrite = ((ItemMultiFocus)stack.getItem()).canWrite(stack, iota);
            if(!simulate){
                ((ItemMultiFocus)stack.getItem()).writeDatum(stack, iota);
            } 
            return canWrite;
        } else if(stack.getItem() instanceof ItemFocus){
            boolean canWrite = ((ItemFocus)stack.getItem()).canWrite(stack, iota);
            if(!simulate){
                ((ItemFocus)stack.getItem()).writeDatum(stack, iota);
            } 
            return canWrite;
        } else {
            return false;
        }
    }

    public static int pageCount(ItemStack stack){
        if(stack.getItem() instanceof ItemMultiFocus){
            return ItemMultiFocus.MAX_FOCI_SLOTS;
        } else {
            return 1;
        }
    }

    // -1 if not handled by this
    public static int getPage(ItemStack stack){
        if(stack.getItem() instanceof ItemMultiFocus){
            return ItemSpellbook.getPage(stack, 0);
        } else {
            return -1;
        }
    }

    // returns new page
    public static int setPage(ItemStack stack, int goal){
        if(stack.getItem() instanceof ItemMultiFocus){
            if(goal > ItemMultiFocus.MAX_FOCI_SLOTS){
                goal = ItemMultiFocus.MAX_FOCI_SLOTS;
            } else if(goal < 1){
                goal = 1;
            }
            int diff = goal - ItemSpellbook.getPage(stack, 0);
            if (diff == goal) {
                // empty 
                return 1;
            } else {
                
                boolean forward = diff > 0;
                for(int i = 0; i < Math.abs(diff); i++){
                    ItemSpellbook.rotatePageIdx(stack, forward);
                }
                return getPage(stack);
            }
        } else {
            return 1;
        }
    }
}
