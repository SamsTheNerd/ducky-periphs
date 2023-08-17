package com.samsthenerd.duckyperiphs.peripherals.keyboards;

import com.samsthenerd.duckyperiphs.DPRecipeSerializer;
import com.samsthenerd.duckyperiphs.DuckyPeriphs;
import com.samsthenerd.duckyperiphs.compat.gloopy.GloopyUtils;

import dev.architectury.platform.Platform;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class KeyboardRecipe extends SpecialCraftingRecipe {
    private static final Ingredient KEYBOARD = Ingredient.ofItems(DuckyPeriphs.KEYBOARD_ITEM.get());
    private static final Ingredient SOLVENTS = Ingredient.ofItems(Items.WET_SPONGE, Items.WATER_BUCKET); // maybe add more here? idk
    
    public KeyboardRecipe(Identifier id, CraftingRecipeCategory category){
        super(id, category);
        // DuckyPeriph.logPrint("entered contructor for KeyboardRecipe");
    }

    @Override
    public boolean matches(RecipeInputInventory craftingInventory, World world) {
        // DuckyPeriph.logPrint("starting keyboard recipe match check");
        // first find where the keyboard is + make sure we only have dyes/solvents?
        // then make sure everything is within 
        int width = craftingInventory.getWidth();
        int height = craftingInventory.getHeight();

        boolean foundChanger = false;
        int minChangerX = 0;
        int minChangerY = 0;
        int maxChangerX = 0;
        int maxChangerY = 0;

        int keyboardCount = 0;
        int keyboardX = 0;
        int keyboardY = 0;

        for(int s = 0; s < width*height; s++){
            int x = s % width;
            int y = s / width;
            ItemStack stack = craftingInventory.getStack(s);
            if(KEYBOARD.test(stack)){
                keyboardCount++;
                keyboardX = x;
                keyboardY = y;
                if(keyboardCount > 1){
                    // DuckyPeriph.logPrint("too many keyboards");
                    return false;
                }
            } else if(SOLVENTS.test(stack) || stack.getItem() instanceof DyeItem || (Platform.isModLoaded("hexgloop") && GloopyUtils.isGloopDye(stack))){
                if(!foundChanger){
                    foundChanger = true;
                    minChangerX = x;
                    minChangerY = y;
                    maxChangerX = x;
                    maxChangerY = y;
                } else {
                    minChangerX = Math.min(minChangerX, x);
                    minChangerY = Math.min(minChangerY, y);
                    maxChangerX = Math.max(maxChangerX, x);
                    maxChangerY = Math.max(maxChangerY, y);
                }
            }
        }
        if(keyboardCount == 0){
            // DuckyPeriph.logPrint("no keyboards");
            return false;
        }
        if(!foundChanger){
            // DuckyPeriph.logPrint("no dyes/solvents");
            return false;
        }
        // too far out
        if(Math.abs(keyboardX - minChangerX) > 2 || Math.abs(keyboardY - minChangerY) > 2 
        || Math.abs(keyboardX - maxChangerX) > 2 || Math.abs(keyboardY - maxChangerY) > 2){
            // DuckyPeriph.logPrint("too far out");
            return false;
        }
        // passes our check ?
        // DuckyPeriph.logPrint("Keyboard recipe passed check");
        return true;
    }


    @Override
    public ItemStack craft(RecipeInputInventory craftingInventory, DynamicRegistryManager dynRegManager) {
        int width = craftingInventory.getWidth();
        int height = craftingInventory.getHeight();
        int keyboardX = 0;
        int keyboardY = 0;
        ItemStack kbStack = ItemStack.EMPTY;
        // find keyboard coordinates first
        for(int s = 0; s < width*height; s++){
            int x = s % width;
            int y = s / width;
            ItemStack stack = craftingInventory.getStack(s);
            if(KEYBOARD.test(stack)){
                keyboardX = x;
                keyboardY = y;
                kbStack = stack.copy();
                break;
            }
        }
        // make a KeyCaps object to make changes to
        KeyCaps keyCaps = KeyCaps.fromItemStack(kbStack);
        for(int s = 0; s < width * height; s++){
            int x = s % width;
            int y = s / width;
            ItemStack stack = craftingInventory.getStack(s);
            if(SOLVENTS.test(stack)){
                // remove dye
                int craftingGridIndex = gridIndex(x, y, keyboardX, keyboardY);
                keyCaps.removeColor(craftingGridIndex);
            } else if(stack.getItem() instanceof DyeItem){
                // add dye
                int craftingGridIndex = gridIndex(x, y, keyboardX, keyboardY);
                float[] thisColorComps = ((DyeItem)stack.getItem()).getColor().getColorComponents();
                int thisColor = (int)(thisColorComps[0] * 255.0f) << 16 | (int)(thisColorComps[1] * 255.0f) << 8 | (int)(thisColorComps[2] * 255.0f);
                keyCaps.blendAndSetColor(thisColor, craftingGridIndex);
            } else if(Platform.isModLoaded("hexgloop") && GloopyUtils.isGloopDye(stack)){
                // add dye
                int craftingGridIndex = gridIndex(x, y, keyboardX, keyboardY);
                int thisColor = GloopyUtils.getGloopDyeColor(stack);
                keyCaps.blendAndSetColor(thisColor, craftingGridIndex);
            }
        }

        // now go along and find the dye/solvent
        kbStack.setSubNbt(KeyCaps.KEY_CAP_KEY, keyCaps.toSubNBT());
        return kbStack;
    }

    public DefaultedList<ItemStack> getRemainder(CraftingInventory inventory) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
        for (int i = 0; i < defaultedList.size(); ++i) {
            Item item = inventory.getStack(i).getItem();
            if(item == Items.WET_SPONGE){
                defaultedList.set(i, inventory.getStack(i));
            } else if(item == Items.WATER_BUCKET){
                defaultedList.set(i, new ItemStack(Items.BUCKET));
            } else if(Platform.isModLoaded("hexgloop") && GloopyUtils.isGloopDye(inventory.getStack(i))){
                ItemStack stack = inventory.getStack(i).copy();
                GloopyUtils.useGloopMedia(stack);
                defaultedList.set(i, stack);
            }
            if (!item.hasRecipeRemainder()) continue;
            defaultedList.set(i, new ItemStack(item.getRecipeRemainder()));
        }
        return defaultedList;
    }

    private int gridIndex(int c_x, int c_y, int kb_x, int kb_y){
        int x = c_x - kb_x + 2;
        int y = c_y - kb_y + 2;
        return x + 5*y;
    }

    // @Override
    // public ItemStack getOutput() {
    //     return new ItemStack(DuckyPeriph.KEYBOARD_ITEM);
    // }

    // can't reach much from 2x2 but eh
    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DPRecipeSerializer.KEYBOARD_RECIPE.get();
    }

    @Override
    public boolean isIgnoredInRecipeBook() {
        return true;
    }
}
