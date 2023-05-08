package com.samsthenerd.duckyperiphs;

import net.fabricmc.api.ClientModInitializer;

public class DuckyPeriphsFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        DuckyPeriphsClientInit.initClient();
    }
}
