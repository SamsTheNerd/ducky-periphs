package com.samsthenerd.duckyperiphs;

import com.samsthenerd.duckyperiphs.hexcasting.DuckyPatternsFabric;

import dev.architectury.platform.Platform;
import net.fabricmc.api.ModInitializer;

public class DuckyPeriphsFabric implements ModInitializer {
    @Override
	public void onInitialize() {
        DuckyPeriphs.onInitialize();

        PeriphLookupFabric.registerPeripherals();
        if(Platform.isModLoaded("hexcasting")){
            DuckyPatternsFabric.init();
        }
    }
}
