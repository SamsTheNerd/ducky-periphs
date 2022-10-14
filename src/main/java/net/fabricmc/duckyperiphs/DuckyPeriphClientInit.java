package net.fabricmc.duckyperiphs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.duckyperiphs.peripherals.keyboards.KeyboardScreen;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

@Environment (EnvType.CLIENT)
public class DuckyPeriphClientInit implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("ducky-periphs");

    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(DuckyPeriph.WEATHER_MACHINE_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(DuckyPeriph.DUCK_BLOCK, RenderLayer.getTranslucent());
        BlockRenderLayerMap.INSTANCE.putBlock(DuckyPeriph.KEYBOARD_BLOCK, RenderLayer.getTranslucent());

        HandledScreens.register(DuckyPeriph.KEYBOARD_SCREEN_HANDLER, KeyboardScreen::new);

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
}