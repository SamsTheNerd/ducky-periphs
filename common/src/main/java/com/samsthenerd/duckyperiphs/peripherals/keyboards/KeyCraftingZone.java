package com.samsthenerd.duckyperiphs.peripherals.keyboards;

public enum KeyCraftingZone {
    ESC(0, "ducky-periphs.keycraftingzone.esc"),
    FN_LEFT(1, "ducky-periphs.keycraftingzone.fn_left"),
    FN_MAIN(2, "ducky-periphs.keycraftingzone.fn_main"),
    FN_RIGHT(3, "ducky-periphs.keycraftingzone.fn_right"),
    SCREEN_KEYS(4, "ducky-periphs.keycraftingzone.screen_keys"),
    TAB(5, "ducky-periphs.keycraftingzone.tab"),
    WASD(6, "ducky-periphs.keycraftingzone.wasd"),
    NUM_ROW(7, "ducky-periphs.keycraftingzone.num_row"),
    BACKSPACE(8, "ducky-periphs.keycraftingzone.backspace"),
    SIDE_TOP(9, "ducky-periphs.keycraftingzone.side_top"),
    CAPS(10, "ducky-periphs.keycraftingzone.caps"),
    MAIN(11, "ducky-periphs.keycraftingzone.main"),
    KEYBOARD(12, "ducky-periphs.keycraftingzone.keyboard"),
    ENTER(13, "ducky-periphs.keycraftingzone.enter"),
    SIDE_MID(14, "ducky-periphs.keycraftingzone.side_mid"),
    LSHIFT(15, "ducky-periphs.keycraftingzone.lshift"),
    UTIL_KEYS(16, "ducky-periphs.keycraftingzone.util_keys"),
    SPACE(17, "ducky-periphs.keycraftingzone.space"),
    ARROWS(18, "ducky-periphs.keycraftingzone.arrows"),
    RSHIFT(19, "ducky-periphs.keycraftingzone.rshift"),
    LEFT_MODIFIERS(20, "ducky-periphs.keycraftingzone.left_modifiers"),
    CASE_LEFT(21, "ducky-periphs.keycraftingzone.case_left"),
    CASE_MAIN(22, "ducky-periphs.keycraftingzone.case_main"),
    CASE_RIGHT(23, "ducky-periphs.keycraftingzone.case_right"),
    RIGHT_MODIFIERS(24, "ducky-periphs.keycraftingzone.right_modifiers");
    int id;
    String nameKey;
    private KeyCraftingZone(int id, String nameKey){
        this.id = id;
        this.nameKey = nameKey;
    }
    public int zone(){return id;}
    public String getNameKey(){return nameKey;}
    public boolean equals(int i){return id == i;}
}
