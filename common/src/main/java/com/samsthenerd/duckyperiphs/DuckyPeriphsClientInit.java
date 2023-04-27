package com.samsthenerd.duckyperiphs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.samsthenerd.duckyperiphs.hexcasting.DuckyCastingClient;
import com.samsthenerd.duckyperiphs.peripherals.keyboards.KeyCaps;
import com.samsthenerd.duckyperiphs.peripherals.keyboards.KeyboardScreen;

import dev.architectury.platform.Platform;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.DyeColor;

@Environment (EnvType.CLIENT)
public class DuckyPeriphsClientInit implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ducky-periphs");

    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(DuckyPeriphs.WEATHER_MACHINE_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(DuckyPeriphs.DUCK_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(DuckyPeriphs.KEYBOARD_BLOCK, RenderLayer.getTranslucent());

        HandledScreens.register(DuckyPeriphs.KEYBOARD_SCREEN_HANDLER, KeyboardScreen::new);

        registerColorProviders();

        if(Platform.isModLoaded("hexcasting")){
            DuckyCastingClient.init();
        }
    }

    private void registerColorProviders(){
        // weather machine
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
			if (view == null || pos == null) {
				return GrassColors.getColor(0.5, 1.0);
            }
            return BiomeColors.getGrassColor(view, pos);
		}, DuckyPeriphs.WEATHER_MACHINE_BLOCK);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			return GrassColors.getColor(0.5, 1.0);
		}, DuckyPeriphs.WEATHER_MACHINE_BLOCK.asItem());

        // keyboard 
        ColorProviderRegistry.BLOCK.register((state,view,pos,tintIndex)->{
			if(view == null || pos == null){
				return DyeColor.BLUE.getFireworkColor();
				// return KeyCaps.DEFAULT_COLOR;
			}
			return DuckyPeriphs.KEYBOARD_BLOCK.getKeyCaps(view, pos).getZoneColor(tintIndex);
		}, DuckyPeriphs.KEYBOARD_BLOCK);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			return KeyCaps.fromItemStack(stack).getZoneColor(tintIndex);
		}, DuckyPeriphs.KEYBOARD_ITEM);

        // duck color providers
		ColorProviderRegistry.BLOCK.register((state,view,pos, tintIndex) -> {
			return DuckyPeriphs.DUCK_BLOCK.getColor(view, pos);
		}, DuckyPeriphs.DUCK_BLOCK);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if(tintIndex != 0) {
				return 0xFFFFFF;
			}
			return DuckyPeriphs.DUCK_ITEM.getColor(stack);
		}, DuckyPeriphs.DUCK_ITEM);
    }
}