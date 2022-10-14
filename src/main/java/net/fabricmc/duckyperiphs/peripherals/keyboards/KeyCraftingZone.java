package net.fabricmc.duckyperiphs.peripherals.keyboards;

public enum KeyCraftingZone {
    ESC(0),
    FN_LEFT(1),
    FN_MAIN(2),
    FN_RIGHT(3),
    SCREEN_KEYS(4),
    TAB(5),
    WASD(6),
    NUM_ROW(7),
    BACKSPACE(8),
    SIDE_TOP(9),
    CAPS(10),
    MAIN(11),
    KEYBOARD(12),
    ENTER(13),
    SIDE_MID(14),
    LSHIFT(15),
    UTIL_KEYS(16),
    SPACE(17),
    ARROWS(18),
    RSHIFT(19),
    LEFT_MODIFIERS(20),
    CASE_LEFT(21),
    CASE_MAIN(22),
    CASE_RIGHT(23),
    RIGHT_MODIFIERS(24);
    int id;
    private KeyCraftingZone(int id){
        this.id = id;
    }
    public int zone(){return id;}
    public boolean equals(int i){return id == i;}
}
