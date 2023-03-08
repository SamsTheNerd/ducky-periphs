package net.fabricmc.duckyperiphs.hexcasting;

import at.petrak.hexcasting.fabric.cc.HexCardinalComponents;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;

// make sure that our CC init class won't try to load anything from hexcasting
public class DuckyCastingCCQuaratined {
    public static void quarantinedRegisterEntityComponentFactories(EntityComponentFactoryRegistry registry){
        registry.registerFor(FocalPortWrapperEntity.class, HexCardinalComponents.IOTA_HOLDER, e -> e);
    }
}
