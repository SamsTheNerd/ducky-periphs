package net.fabricmc.duckyperiphs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.duckyperiphs.hexcasting.DuckyCastingClient;
import net.fabricmc.duckyperiphs.peripherals.keyboards.KeyCaps;
import net.fabricmc.duckyperiphs.peripherals.keyboards.KeyboardScreen;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.DyeColor;

@Environment (EnvType.CLIENT)
public class DuckyPeriphClientInit implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ducky-periphs");

    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(DuckyPeriph.WEATHER_MACHINE_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(DuckyPeriph.DUCK_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(DuckyPeriph.KEYBOARD_BLOCK, RenderLayer.getTranslucent());

        HandledScreens.register(DuckyPeriph.KEYBOARD_SCREEN_HANDLER, KeyboardScreen::new);

        registerColorProviders();

        if(FabricLoader.getInstance().isModLoaded("hexcasting")){
            DuckyCastingClient.init();
        }

        // register our packet handlers
        // ServerPlayNetworking.registerGlobalReceiver(new Identifier(DuckyPeriph.MOD_ID, "key_press_packet"), 
        // // MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender
        // (server, player, handler, buf, responseSender) -> {
        //     int key = buf.readInt();
        //     BlockPos pos = buf.readBlockPos();
        //     World world = player.getEntityWorld();
        //     LOGGER.info("recieved key press packet: " + key);
        //     KeyboardTile.keyPress(pos, world, key);

        // });
    }

    private void registerColorProviders(){
        // weather machine
        ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
			if (view == null || pos == null) {
				return GrassColors.getColor(0.5, 1.0);
            }
            return BiomeColors.getGrassColor(view, pos);
		}, DuckyPeriph.WEATHER_MACHINE_BLOCK);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			return GrassColors.getColor(0.5, 1.0);
		}, DuckyPeriph.WEATHER_MACHINE_ITEM);

        // keyboard 
        ColorProviderRegistry.BLOCK.register((state,view,pos,tintIndex)->{
			if(view == null || pos == null){
				return DyeColor.BLUE.getFireworkColor();
				// return KeyCaps.DEFAULT_COLOR;
			}
			return DuckyPeriph.KEYBOARD_BLOCK.getKeyCaps(view, pos).getZoneColor(tintIndex);
		}, DuckyPeriph.KEYBOARD_BLOCK);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			return KeyCaps.fromItemStack(stack).getZoneColor(tintIndex);
		}, DuckyPeriph.KEYBOARD_ITEM);

        // duck color providers
		ColorProviderRegistry.BLOCK.register((state,view,pos, tintIndex) -> {
			return DuckyPeriph.DUCK_BLOCK.getColor(view, pos);
		}, DuckyPeriph.DUCK_BLOCK);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if(tintIndex != 0) {
				return 0xFFFFFF;
			}
			return DuckyPeriph.DUCK_ITEM.getColor(stack);
		}, DuckyPeriph.DUCK_ITEM);
    }
}