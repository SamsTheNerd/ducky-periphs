package com.samsthenerd.duckyperiphs.peripherals.keyboards;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

// this will hold basically the same info as the keyboard NBT
// put some helpful functions for NBT serialization/deserialization here
// put tintindex functions here as well
public class KeyCaps {
    public int[] craftingValues; // holds 25 ints. Most will be 0 or nonsense but oh well
    public int craftingNulls;
    public static final String KEY_CAP_KEY = "key_caps";
    public static final String CRAFTING_VALUES_KEY = "crafting_values";
    public static final String CRAFTING_NULLS_KEY = "crafting_nulls";
    public static final int DEFAULT_COLOR = 0x1D1D21; // change later ?


    public KeyCaps(){
        craftingValues = new int[25];
        craftingNulls = 0;
    }

    public static KeyCaps fromNBT(NbtCompound nbt) {
        KeyCaps keyCaps = new KeyCaps();
        if(nbt != null && nbt.contains(CRAFTING_NULLS_KEY, 3)){
            keyCaps.craftingNulls = nbt.getInt(CRAFTING_NULLS_KEY);
            if(nbt.contains(CRAFTING_VALUES_KEY, 11)){
                keyCaps.craftingValues = nbt.getIntArray(CRAFTING_VALUES_KEY);
            }
        }
        return keyCaps;
    }

    public static KeyCaps fromItemStack(ItemStack itemStack) {
        NbtCompound nbt = itemStack.getSubNbt(KEY_CAP_KEY);
        return fromNBT(nbt);
    }

    
    public NbtCompound toSubNBT() {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt(CRAFTING_NULLS_KEY, craftingNulls);
        nbt.putIntArray(CRAFTING_VALUES_KEY, craftingValues);
        return nbt;
    }

    public static ItemStack changeColor(ItemStack itemStack, int color, int craftingIndex) {
        KeyCaps keyCaps = KeyCaps.fromItemStack(itemStack);
        keyCaps.changeColor(color, craftingIndex);
        NbtCompound nbt = itemStack.getOrCreateSubNbt(KEY_CAP_KEY);
        nbt.put(KEY_CAP_KEY, keyCaps.toSubNBT());
        return itemStack;
    }

    public void changeColor(int newColor, int craftingIndex){
        craftingValues[craftingIndex] = newColor;
        // DuckyPeriph.LOGGER.info("newColor: " + newColor + " on craftingIndex: " + craftingIndex + " with (1 >>> craftingIndex) = " + (1 >>> craftingIndex)); 
        craftingNulls = craftingNulls | (1 << craftingIndex);
        // logSelf();
    }

    // idk i grabbed and reformatted from DyeableItem
    public static int blendColors(int color1, int color2){
        int n;
        float h;
        int[] is = new int[3];
        int i = 0;
        int j = 0;
        float f = (float)(color1 >> 16 & 0xFF) / 255.0f;
        float g = (float)(color1 >> 8 & 0xFF) / 255.0f;
        h = (float)(color1 & 0xFF) / 255.0f;
        i += (int)(Math.max(f, Math.max(g, h)) * 255.0f);
        is[0] = is[0] + (int)(f * 255.0f);
        is[1] = is[1] + (int)(g * 255.0f);
        is[2] = is[2] + (int)(h * 255.0f);
        ++j;
        float[] fs =  new float[]{(float)((color2 & 0xFF0000) >> 16) / 255.0f, (float)((color2 & 0xFF00) >> 8) / 255.0f, (float)((color2 & 0xFF) >> 0) / 255.0f};
        int l = (int)(fs[0] * 255.0f);
        int m = (int)(fs[1] * 255.0f);
        n = (int)(fs[2] * 255.0f);
        i += Math.max(l, Math.max(m, n));
        is[0] = is[0] + l;
        is[1] = is[1] + m;
        is[2] = is[2] + n;
        ++j;
        int k = is[0] / j;
        int o = is[1] / j;
        int p = is[2] / j;
        h = (float)i / (float)j;
        float q = Math.max(k, Math.max(o, p));
        k = (int)((float)k * h / q);
        o = (int)((float)o * h / q);
        p = (int)((float)p * h / q);
        n = k;
        n = (n << 8) + o;
        n = (n << 8) + p;
        return n;
    }
    
    public void blendAndSetColor(int newColor, int craftingIndex){
        if(isDefault(craftingIndex)){
            changeColor(newColor, craftingIndex);
            return;
        }
        int oldColor = craftingValues[craftingIndex];
        int blendedColor = blendColors(oldColor, newColor);
        changeColor(blendedColor, craftingIndex);
    }

    public void removeColor(int craftingIndex){
        craftingValues[craftingIndex] = DEFAULT_COLOR;
        craftingNulls = craftingNulls & ~(1 << craftingIndex);
    }

    public void logSelf(){
        DuckyPeriphs.LOGGER.info("KeyCaps: " + craftingNulls);
    }


    public boolean isDefault(){
        return craftingNulls == 0; // might need to add to account for whatever implied stuff we add
    }

    public boolean isDefault(int cZone){
        return ((1 << cZone) & craftingNulls) == 0;
    }
    public boolean isDefault(KeyCraftingZone cZone){
        return ((1 << cZone.id) & craftingNulls) == 0;
    }


    public int getZoneColor(KeyZone zone){
        return getZoneColor(zone.tintIndex());
    }

    public static boolean isZoneType(int tintIndex, KeyZone[] zoneType){
        for(KeyZone zone : zoneType){
            if(zone.tintIndex() == tintIndex){
                return true;
            }
        }
        return false;
    }


    public Text getDisplayText(KeyZone keyZone){
        int keyColor = getZoneColor(keyZone);
        if(keyColor == DEFAULT_COLOR){
            return null;
        }
        return Text.translatable(keyZone.getName()).append(Text.of(": #")).append(Text.of(Integer.toHexString(keyColor)));
    }

    Text getDisplayText(KeyCraftingZone keyCraftingZone){
        if(isDefault(keyCraftingZone)){
            return null;
        }
        int keyColor = craftingValues[keyCraftingZone.id];
        Text colorIndicatorText = Text.literal("♥").setStyle(Style.EMPTY.withColor(keyColor));
        Text colorText = Text.literal(": #").append(Text.of(Integer.toHexString(keyColor))).formatted(Formatting.GRAY).append(colorIndicatorText);
        return Text.translatable(keyCraftingZone.getNameKey()).formatted(Formatting.GRAY).append(colorText);
    }


    // this might be messy, keep it towards the bottom
    public int getZoneColor(int tintIndex){
        if(isDefault()){ // blank keyboard
            return DEFAULT_COLOR;
			// return DyeColor.BLACK.getFireworkColor();
            
        }

        // deal with case stuff:
        if(isZoneType(tintIndex, KeyZone.CASE_ZONES)){
            boolean have_main = !isDefault(KeyCraftingZone.CASE_MAIN);
            boolean have_left = !isDefault(KeyCraftingZone.CASE_LEFT);
            boolean have_right = !isDefault(KeyCraftingZone.CASE_RIGHT);
            boolean is_main = isZoneType(tintIndex, KeyZone.CASE_ZONES_M);
            boolean is_left = isZoneType(tintIndex, KeyZone.CASE_ZONES_L);
            boolean is_right = isZoneType(tintIndex, KeyZone.CASE_ZONES_R);
            if(have_main && !have_left && !have_right){ //solid
                return craftingValues[KeyCraftingZone.CASE_MAIN.id];
            }
            if(!have_main && have_left && have_right){ //split
                return is_left ? craftingValues[KeyCraftingZone.CASE_LEFT.id] : craftingValues[KeyCraftingZone.CASE_RIGHT.id];
            }
            if(have_main && have_left && have_right){ //thirds
                if(is_main){
                    return craftingValues[KeyCraftingZone.CASE_MAIN.id];
                }
                if(is_left){
                    return craftingValues[KeyCraftingZone.CASE_LEFT.id];
                }
                if(is_right){
                    return craftingValues[KeyCraftingZone.CASE_RIGHT.id];
                }
            }
            if(!have_main && !have_left && !have_right){ // none 
                return DEFAULT_COLOR;
				// return DyeColor.RED.getFireworkColor();

            }
            if(have_main && !have_left && have_right){ // 60/30 split
                return is_left || is_main ? craftingValues[KeyCraftingZone.CASE_MAIN.id] : craftingValues[KeyCraftingZone.CASE_RIGHT.id];
            }
            if(have_main && have_left && !have_right){ // 30/60 split
                return is_right || is_main ? craftingValues[KeyCraftingZone.CASE_MAIN.id] : craftingValues[KeyCraftingZone.CASE_LEFT.id];
            }
            // only cases left are if we only have left or only have right (no main)
            if(have_left && is_left){
                return craftingValues[KeyCraftingZone.CASE_LEFT.id];
            }
            if(have_right && is_right){
                return craftingValues[KeyCraftingZone.CASE_RIGHT.id];
            }
            return DEFAULT_COLOR;
				// return DyeColor.YELLOW.getFireworkColor();

        }

        // deal with FN keys:
        if(isZoneType(tintIndex, KeyZone.FN_ZONES)){
            if(!isDefault(KeyCraftingZone.FN_LEFT) && KeyZone.LEFT_FN_KEYS.equals(tintIndex)){
                return craftingValues[KeyCraftingZone.FN_LEFT.id];
            }
            if(!isDefault(KeyCraftingZone.FN_RIGHT) && KeyZone.RIGHT_FN_KEYS.equals(tintIndex)){
                return craftingValues[KeyCraftingZone.FN_RIGHT.id];
            }
            if(!isDefault(KeyCraftingZone.FN_MAIN)){
                return craftingValues[KeyCraftingZone.FN_MAIN.id];
            }
        }


        // variety of keys:
        // tab 
        if(!isDefault(KeyCraftingZone.TAB) && KeyZone.TAB_KEY.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.TAB.id];
        }
        // esc 
        if(!isDefault(KeyCraftingZone.ESC) && KeyZone.ESC_KEY.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.ESC.id];
        }
        // caps
        if(!isDefault(KeyCraftingZone.CAPS) && KeyZone.CAPS_KEY.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.CAPS.id];
        }
        // lshift
        if(!isDefault(KeyCraftingZone.LSHIFT) && KeyZone.LSHIFT.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.LSHIFT.id];
        }
        // rshift
        if(!isDefault(KeyCraftingZone.RSHIFT) && KeyZone.RSHIFT.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.RSHIFT.id];
        }
        // space
        if(!isDefault(KeyCraftingZone.SPACE) && KeyZone.SPACE_KEY.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.SPACE.id];
        }
        // enter
        if(!isDefault(KeyCraftingZone.ENTER) && KeyZone.ENTER_KEY.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.ENTER.id];
        }
        // backspace
        if(!isDefault(KeyCraftingZone.BACKSPACE) && KeyZone.BACK_KEY.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.BACKSPACE.id];
        }
        // lmodifiers
        if(!isDefault(KeyCraftingZone.LEFT_MODIFIERS) && KeyZone.MODIFIER_KEYS_LEFT.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.LEFT_MODIFIERS.id];
        }
        // rmodifiers
        if(!isDefault(KeyCraftingZone.RIGHT_MODIFIERS) && KeyZone.MODIFIER_KEYS_RIGHT.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.RIGHT_MODIFIERS.id];
        }

        // num row:
        if(!isDefault(KeyCraftingZone.NUM_ROW) && KeyZone.NUM_ROW.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.NUM_ROW.id];
        }

        // wasd
        if(!isDefault(KeyCraftingZone.WASD) && KeyZone.WASD_KEYS.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.WASD.id];
        }

        // arrow keys
        if(!isDefault(KeyCraftingZone.ARROWS) && KeyZone.ARROW_KEYS.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.ARROWS.id];
        }

        // screen keys
        if(!isDefault(KeyCraftingZone.SCREEN_KEYS) && KeyZone.SCREEN_KEYS.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.SCREEN_KEYS.id];
        }
        // side top
        if(!isDefault(KeyCraftingZone.SIDE_TOP) && KeyZone.TOP_SIDE_KEYS.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.SIDE_TOP.id];
        }
        // mid side
        if(!isDefault(KeyCraftingZone.SIDE_MID) && KeyZone.MID_SIDE_KEYS.equals(tintIndex)){
            return craftingValues[KeyCraftingZone.SIDE_MID.id];
        }

        // need to do grave/backslash stuff at some point - not really sure how I want to do that with extra control available though?


        
        
        // default for stuff around the edges
        if(!isDefault(KeyCraftingZone.UTIL_KEYS) && isZoneType(tintIndex, KeyZone.UTIL_ZONES)){
            return craftingValues[KeyCraftingZone.UTIL_KEYS.id];
        }

        if(!isDefault(KeyCraftingZone.MAIN.zone())){ // we atleast have a main color
            return craftingValues[KeyCraftingZone.MAIN.zone()];
        }
        return DEFAULT_COLOR; // if nothing else hits
        // return DyeColor.MAGENTA.getFireworkColor();

    }



}
