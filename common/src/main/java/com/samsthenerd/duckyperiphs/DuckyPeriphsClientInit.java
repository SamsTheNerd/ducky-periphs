package com.samsthenerd.duckyperiphs;
import com.samsthenerd.duckyperiphs.hexcasting.DuckyCastingClient;
import com.samsthenerd.duckyperiphs.hexcasting.DummyNoHex;
import com.samsthenerd.duckyperiphs.peripherals.keyboards.KeyCaps;
import com.samsthenerd.duckyperiphs.peripherals.keyboards.KeyboardScreen;

import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.DyeColor;

@Environment (EnvType.CLIENT)
public class DuckyPeriphsClientInit{
    public static void initClient() {
		RenderTypeRegistry.register(RenderLayer.getTranslucent(), DuckyPeriphs.WEATHER_MACHINE_BLOCK.get(),
					DuckyPeriphs.KEYBOARD_BLOCK.get(), DuckyPeriphs.DUCK_BLOCK.get());

        HandledScreens.register(DuckyPeriphs.KEYBOARD_SCREEN_HANDLER, KeyboardScreen::new);

        registerColorProviders();

        if(Platform.isModLoaded("hexcasting")){
            DuckyCastingClient.init();
        } else {
			RenderTypeRegistry.register(RenderLayer.getTranslucent(), DummyNoHex.FOCAL_PORT_BLOCK.get(), DummyNoHex.CONJURED_DUCKY_BLOCK.get());
		}
    }

    private static void registerColorProviders(){
		
        // weather machine
        ColorHandlerRegistry.registerBlockColors((state, view, pos, tintIndex) -> {
			if (view == null || pos == null) {
				return GrassColors.getColor(0.5, 1.0);
            }
            return BiomeColors.getGrassColor(view, pos);
		}, DuckyPeriphs.WEATHER_MACHINE_BLOCK.get());

		ColorHandlerRegistry.registerItemColors((stack, tintIndex) -> {
			return GrassColors.getColor(0.5, 1.0);
		}, DuckyPeriphs.WEATHER_MACHINE_BLOCK.get().asItem());

        // keyboard 
        ColorHandlerRegistry.registerBlockColors((state,view,pos,tintIndex)->{
			if(view == null || pos == null){
				return DyeColor.BLUE.getFireworkColor();
				// return KeyCaps.DEFAULT_COLOR;
			}
			// DuckyPeriphs.logPrint("got key caps:" + DuckyPeriphs.KEYBOARD_BLOCK.get().getKeyCaps(view, pos).craftingNulls);
			return DuckyPeriphs.KEYBOARD_BLOCK.get().getKeyCaps(view, pos).getZoneColor(tintIndex);
		}, DuckyPeriphs.KEYBOARD_BLOCK.get());

		ColorHandlerRegistry.registerItemColors((stack, tintIndex) -> {
			return KeyCaps.fromItemStack(stack).getZoneColor(tintIndex);
		}, DuckyPeriphs.KEYBOARD_ITEM.get());

        // duck color providers
		ColorHandlerRegistry.registerBlockColors((state,view,pos, tintIndex) -> {
			return DuckyPeriphs.DUCK_BLOCK.get().getColor(view, pos);
		}, DuckyPeriphs.DUCK_BLOCK.get());

		ColorHandlerRegistry.registerItemColors((stack, tintIndex) -> {
			if(tintIndex != 0) {
				return 0xFFFFFF;
			}
			return DuckyPeriphs.DUCK_ITEM.get().getColor(stack);
		}, DuckyPeriphs.DUCK_ITEM.get());
    }
}