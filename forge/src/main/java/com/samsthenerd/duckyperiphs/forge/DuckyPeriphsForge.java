package com.samsthenerd.duckyperiphs.forge;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;
import com.samsthenerd.duckyperiphs.DuckyPeriphsClientInit;

import dan200.computercraft.api.ForgeComputerCraftAPI;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("duckyperiphs")
public class DuckyPeriphsForge {
    public DuckyPeriphsForge(){
        // so that we can register properly with architectury
        EventBuses.registerModEventBus(DuckyPeriphs.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        DuckyPeriphs.onInitialize();

        ForgeComputerCraftAPI.registerPeripheralProvider(new DuckyPeriphsProviderForge());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
    }

    private void onClientSetup(FMLClientSetupEvent event) { 
        DuckyPeriphsClientInit.initClient();
    }
}