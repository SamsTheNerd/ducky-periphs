package net.fabricmc.duckyperiphs.peripherals.keyboards;

import net.fabricmc.duckyperiphs.DuckyPeriph;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// mostly just stuffing our handling functions in here because they're a bit messy for the main init file
public class KeyboardUtils {
    public static void keyPressHandler(MinecraftServer server, ServerPlayerEntity player, 
        ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
            
        int key = buf.readInt();
        int scancode = buf.readInt();
        int modifiers = buf.readInt();
        Boolean repeat = buf.readBoolean();
        String pasteText= buf.readString(); // for if we have a paste event, otherwise empty but just pass it anyways
        BlockPos pos = buf.readBlockPos();
        World world = player.getEntityWorld();
        DuckyPeriph.LOGGER.info("recieved key press packet: " + key + " on worldtype: " + (world.isClient ? "client" : "server"));
        server.execute(()->{
            KeyboardTile.keyPress(pos, world, key, scancode, modifiers, repeat, pasteText);
        });
    }

    public static void keyUpHandler(MinecraftServer server, ServerPlayerEntity player, 
    ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
        int key = buf.readInt();
        int scancode = buf.readInt();
        int modifiers = buf.readInt();
        BlockPos pos = buf.readBlockPos();
        World world = player.getEntityWorld();
        DuckyPeriph.LOGGER.info("recieved key up packet: " + key + " on worldtype: " + (world.isClient ? "client" : "server"));
        server.execute(()->{
            KeyboardTile.keyUp(pos, world, key, scancode, modifiers);
        });
    }

    

    public static void charTypedHandler(MinecraftServer server, ServerPlayerEntity player, 
    ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
        char char_sent = buf.readChar();
        int modifiers = buf.readInt();
        BlockPos pos = buf.readBlockPos();
        World world = player.getEntityWorld();
        DuckyPeriph.LOGGER.info("recieved char " + char_sent + " on worldtype: " + (world.isClient ? "client" : "server"));
        server.execute(()->{
            KeyboardTile.charTyped(pos, world, char_sent, modifiers);
        });
    }

    public static void eventShortcutHandler(MinecraftServer server, ServerPlayerEntity player, 
    ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender){
        int eventCode = buf.readInt();
        BlockPos pos = buf.readBlockPos();
        World world = player.getEntityWorld();
        DuckyPeriph.LOGGER.info("recieved event " + eventCode + " on worldtype: " + (world.isClient ? "client" : "server"));
        server.execute(()->{
            KeyboardTile.handleEvents(pos, world, eventCode);
        });
    }


}
