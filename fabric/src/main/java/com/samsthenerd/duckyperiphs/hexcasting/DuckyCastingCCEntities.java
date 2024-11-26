package com.samsthenerd.duckyperiphs.hexcasting;

import at.petrak.hexcasting.fabric.cc.HexCardinalComponents;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.util.function.Supplier;

public class DuckyCastingCCEntities implements EntityComponentInitializer {
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        if(FabricLoader.getInstance().isModLoaded("hexcasting")){
            Supplier<Runnable> safeReg = () -> () -> {
                registry.registerFor(FocalPortWrapperEntity.class, HexCardinalComponents.IOTA_HOLDER,
                DuckyCCEBEIotaWrapper::new);
            };
            safeReg.get().run();
        }
    }
}
