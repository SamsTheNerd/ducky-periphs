package com.samsthenerd.duckyperiphs.hexcasting;

import java.util.function.BiConsumer;

import at.petrak.hexcasting.common.lib.hex.HexActions;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DuckyPatternsFabric {

    public static void init(){
        DuckyCasting.registerSpells(bind(HexActions.REGISTRY));
    }

    private static <T> BiConsumer<T, Identifier> bind(Registry<T> registry){
        return (t, id) -> { Registry.register(registry, id, t); };

    }
}
