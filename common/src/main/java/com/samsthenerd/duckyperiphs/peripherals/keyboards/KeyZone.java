package com.samsthenerd.duckyperiphs.peripherals.keyboards;

// just maps keyzones to their tint indexes
public enum KeyZone {
    CASE_RIGHT(0, "ducky-periphs.keyzone.case_right"),
    CASE_LEFT(1, "ducky-periphs.keyzone.case_left"),
    CASE_MID_LEFT(2, "ducky-periphs.keyzone.case_mid_left"),
    CASE_MID_RIGHT(3, "ducky-periphs.keyzone.case_mid_right"),
    FLAT_CASE_RIGHT(4, "ducky-periphs.keyzone.flat_case_right"),
    FLAT_CASE_LEFT(5, "ducky-periphs.keyzone.flat_case_left"),
    FLAT_CASE_FRONT_FR(6, "ducky-periphs.keyzone.flat_case_front_fr"),
    FLAT_CASE_FRONT_FL(7, "ducky-periphs.keyzone.flat_case_front_fl"),
    FLAT_CASE_FRONT_ML(8, "ducky-periphs.keyzone.flat_case_front_ml"),
    FLAT_CASE_FRONT_MR(9, "ducky-periphs.keyzone.flat_case_front_mr"),
    FLAT_CASE_BACK_FR(10, "ducky-periphs.keyzone.flat_case_back_fr"),
    FLAT_CASE_BACK_FL(11, "ducky-periphs.keyzone.flat_case_back_fl"),
    FLAT_CASE_BACK_ML(12, "ducky-periphs.keyzone.flat_case_back_ml"),
    FLAT_CASE_BACK_MR(13, "ducky-periphs.keyzone.flat_case_back_mr"),
    ARROW_KEYS(14, "ducky-periphs.keyzone.arrow_keys"),
    BACK_KEY(15, "ducky-periphs.keyzone.back_key"),
    BACKSLASH_KEY(16, "ducky-periphs.keyzone.backslash_key"),
    CAPS_KEY(17, "ducky-periphs.keyzone.caps_key"),
    ENTER_KEY(18, "ducky-periphs.keyzone.enter_key"),
    ESC_KEY(19, "ducky-periphs.keyzone.esc_key"),
    GRAVE_KEY(20, "ducky-periphs.keyzone.grave_key"),
    LEFT_FN_KEYS(21, "ducky-periphs.keyzone.left_fn_keys"),
    RIGHT_FN_KEYS(22, "ducky-periphs.keyzone.right_fn_keys"),
    LSHIFT(23, "ducky-periphs.keyzone.lshift"),
    RSHIFT(24, "ducky-periphs.keyzone.rshift"),
    MID_FN_KEYS(25, "ducky-periphs.keyzone.mid_fn_keys"),
    MID_SIDE_KEYS(26, "ducky-periphs.keyzone.mid_side_keys"),
    MODIFIER_KEYS_LEFT(27, "ducky-periphs.keyzone.modifier_keys_left"),
    MODIFIER_KEYS_RIGHT(28, "ducky-periphs.keyzone.modifier_keys_right"),
    MOST_KEYS(29, "ducky-periphs.keyzone.most_keys"),
    NUM_ROW(30, "ducky-periphs.keyzone.num_row"),
    SCREEN_KEYS(31, "ducky-periphs.keyzone.screen_keys"),
    SPACE_KEY(32, "ducky-periphs.keyzone.space_key"),
    TAB_KEY(33, "ducky-periphs.keyzone.tab_key"),
    TOP_SIDE_KEYS(34, "ducky-periphs.keyzone.top_side_keys"),
    WASD_KEYS(35, "ducky-periphs.keyzone.wasd_keys");
    short tI;
    String name;
    private KeyZone(int tintIndex, String name){
        this.tI = (short)tintIndex;
        this.name = name;
    }
    public int tintIndex(){return tI;}
    public String getName(){return name;}
    public boolean equals(int i){return tI == i;}

    public static final KeyZone[] CASE_ZONES = {CASE_RIGHT, CASE_LEFT, CASE_MID_LEFT, CASE_MID_RIGHT, FLAT_CASE_RIGHT, FLAT_CASE_LEFT, FLAT_CASE_FRONT_FR, FLAT_CASE_FRONT_FL, FLAT_CASE_FRONT_ML, FLAT_CASE_FRONT_MR, FLAT_CASE_BACK_FR, FLAT_CASE_BACK_FL, FLAT_CASE_BACK_ML, FLAT_CASE_BACK_MR};
    public static final KeyZone[] CASE_ZONES_R = {CASE_RIGHT, CASE_MID_RIGHT, FLAT_CASE_RIGHT, FLAT_CASE_FRONT_FR, FLAT_CASE_FRONT_MR, FLAT_CASE_BACK_FR, FLAT_CASE_BACK_MR};
    public static final KeyZone[] CASE_ZONES_L = {CASE_LEFT, CASE_MID_LEFT, FLAT_CASE_LEFT, FLAT_CASE_FRONT_FL, FLAT_CASE_FRONT_ML, FLAT_CASE_BACK_FL, FLAT_CASE_BACK_ML};
    public static final KeyZone[] CASE_ZONES_M = {CASE_MID_LEFT, CASE_MID_RIGHT, FLAT_CASE_FRONT_ML, FLAT_CASE_FRONT_MR, FLAT_CASE_BACK_ML, FLAT_CASE_BACK_MR};
    public static final KeyZone[] UTIL_ZONES = {ARROW_KEYS, BACK_KEY, CAPS_KEY, ENTER_KEY, ESC_KEY, LSHIFT, RSHIFT, MID_SIDE_KEYS, MODIFIER_KEYS_LEFT, MODIFIER_KEYS_RIGHT, SCREEN_KEYS, TAB_KEY, TOP_SIDE_KEYS};
    public static final KeyZone[] FN_ZONES = {LEFT_FN_KEYS, RIGHT_FN_KEYS, MID_FN_KEYS};
}
