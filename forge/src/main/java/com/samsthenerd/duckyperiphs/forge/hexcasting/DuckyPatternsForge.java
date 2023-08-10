package com.samsthenerd.duckyperiphs.forge.hexcasting;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.samsthenerd.duckyperiphs.hexcasting.DuckyCasting;

import at.petrak.hexcasting.common.lib.HexRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

public class DuckyPatternsForge {

    public static void init(){
        bind(HexRegistries.ACTION, DuckyCasting::registerSpells);
    }
    
    private static <T> void bind (RegistryKey<Registry<T>> registry, Consumer<BiConsumer<T, Identifier>> source) {
		FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterEvent event) -> {
			if (registry.equals(event.getRegistryKey())) {
				source.accept((t, rl) -> event.register(registry, rl, () -> t));
			}
		});
	}
}
