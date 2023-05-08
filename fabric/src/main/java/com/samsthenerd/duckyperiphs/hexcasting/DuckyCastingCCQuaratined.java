package com.samsthenerd.duckyperiphs.hexcasting;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import at.petrak.hexcasting.api.spell.iota.PatternIota;
import at.petrak.hexcasting.api.spell.math.HexDir;
import at.petrak.hexcasting.api.spell.math.HexPattern;
import at.petrak.hexcasting.fabric.cc.HexCardinalComponents;
import at.petrak.hexcasting.fabric.cc.adimpl.CCItemIotaHolder;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;

// make sure that our CC init class won't try to load anything from hexcasting
public class DuckyCastingCCQuaratined {
    public static void quarantinedRegisterEntityComponentFactories(EntityComponentFactoryRegistry registry){
        registry.registerFor(FocalPortWrapperEntity.class, HexCardinalComponents.IOTA_HOLDER, e -> e);
    }

    public static void quarantinedRegisterItemsComponentFactories(ItemComponentFactoryRegistry registry){
        registry.register(DuckyPeriphs.DUCK_ITEM.get(), HexCardinalComponents.IOTA_HOLDER, stack -> new CCItemIotaHolder.Static(stack,
            s -> new PatternIota(HexPattern.fromAngles("aqadweeeede", HexDir.NORTH_EAST))));
    }
}
