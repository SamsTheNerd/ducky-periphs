package com.samsthenerd.duckyperiphs.hexcasting;

import at.petrak.hexcasting.api.casting.iota.PatternIota;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import at.petrak.hexcasting.fabric.cc.HexCardinalComponents;
import at.petrak.hexcasting.fabric.cc.adimpl.CCItemIotaHolder;
import com.samsthenerd.duckyperiphs.DuckyPeriphs;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.util.function.Supplier;

public class DuckyCastingCCItems implements ItemComponentInitializer {

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        if(FabricLoader.getInstance().isModLoaded("hexcasting")){
            Supplier<Runnable> safeReg = () -> () -> {
                registry.register(DuckyPeriphs.DUCK_ITEM.get(), HexCardinalComponents.IOTA_HOLDER,
                    stack -> new CCItemIotaHolder.Static(stack,
                    s -> new PatternIota(HexPattern.fromAngles("aqadweeeede", HexDir.NORTH_EAST))));
            };
            safeReg.get().run();
        }

    }
}
