package net.fabricmc.duckyperiphs.hexcasting;

import at.petrak.hexcasting.fabric.cc.HexCardinalComponents;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.fabricmc.duckyperiphs.DuckyPeriph;
import net.fabricmc.loader.api.FabricLoader;

/*
 * An entrypoint class that registers cardinal components stuff for hex casting
 * -- Keep Hex Casting imports out of this file
 */
public class DuckyCastingCC implements EntityComponentInitializer{
    // keeping this here incase it's needed but i don't think it is
	// public static final Supplier<@Nullable ComponentKey<?>> IOTA_HOLDER = Suppliers.memoize(() -> ComponentRegistry.get(new Identifier("hexcasting:iota_holder")));

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        if(FabricLoader.getInstance().isModLoaded("hexcasting")){
            DuckyPeriph.LOGGER.info("Registering FocalPortWrapperEntity as CC");
            registry.registerFor(FocalPortWrapperEntity.class, HexCardinalComponents.IOTA_HOLDER, e -> e);
        }
    }

    
}
