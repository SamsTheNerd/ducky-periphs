package com.samsthenerd.duckyperiphs;

import dan200.computercraft.api.ComputerCraftAPI;
import net.fabricmc.api.ModInitializer;

public class DuckyPeriphsFabric implements ModInitializer {
    @Override
	public void onInitialize() {
        DuckyPeriphs.onInitialize();

        ComputerCraftAPI.registerPeripheralProvider(new DuckyPeriphsProviderFabric());
    }
}
