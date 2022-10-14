package net.fabricmc.duckyperiphs.peripherals.keyboards;

public class KeyboardPresets {
    private static final int TRANS_BLUE = 6016762;
    private static final int TRANS_PINK = 16099768;
    private static final int TRANS_WHITE = 16777215;

    public static KeyCaps transKeyboard(){
        KeyCaps keyCaps = new KeyCaps();
        keyCaps.changeColor(TRANS_WHITE, KeyCraftingZone.MAIN.id);
        keyCaps.changeColor(TRANS_WHITE, KeyCraftingZone.CASE_MAIN.id);
        keyCaps.changeColor(TRANS_BLUE, KeyCraftingZone.ESC.id);
        keyCaps.changeColor(TRANS_BLUE, KeyCraftingZone.FN_MAIN.id);
        keyCaps.changeColor(TRANS_BLUE, KeyCraftingZone.SCREEN_KEYS.id);
        keyCaps.changeColor(TRANS_BLUE, KeyCraftingZone.SPACE.id);
        keyCaps.changeColor(TRANS_BLUE, KeyCraftingZone.LEFT_MODIFIERS.id);
        keyCaps.changeColor(TRANS_BLUE, KeyCraftingZone.RIGHT_MODIFIERS.id);
        keyCaps.changeColor(TRANS_BLUE, KeyCraftingZone.ARROWS.id);
        keyCaps.changeColor(TRANS_PINK, KeyCraftingZone.NUM_ROW.id);
        keyCaps.changeColor(TRANS_PINK, KeyCraftingZone.WASD.id);
        keyCaps.changeColor(TRANS_PINK, KeyCraftingZone.LSHIFT.id);
        keyCaps.changeColor(TRANS_PINK, KeyCraftingZone.RSHIFT.id);
        keyCaps.changeColor(TRANS_PINK, KeyCraftingZone.ENTER.id);
        keyCaps.changeColor(TRANS_PINK, KeyCraftingZone.BACKSPACE.id);
        keyCaps.changeColor(TRANS_PINK, KeyCraftingZone.TAB.id);
        keyCaps.changeColor(TRANS_PINK, KeyCraftingZone.SIDE_TOP.id);
        keyCaps.changeColor(TRANS_PINK, KeyCraftingZone.SIDE_MID.id);
        return keyCaps;
    }
}
