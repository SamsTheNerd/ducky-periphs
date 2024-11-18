package com.samsthenerd.duckyperiphs.peripherals.keyboards;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import dev.architectury.networking.NetworkManager.PacketContext;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// mostly just stuffing our handling functions in here because they're a bit messy for the main init file
public class KeyboardUtils {
    public static void keyPressHandler(PacketByteBuf buf, PacketContext context){
        int key = buf.readInt();
        int scancode = buf.readInt();
        int modifiers = buf.readInt();
        Boolean repeat = buf.readBoolean();
        String pasteText= buf.readString(); // for if we have a paste event, otherwise empty but just pass it anyways
        // BlockPos pos = buf.readBlockPos();
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
        BlockPos pos;
        if(player.currentScreenHandler instanceof KeyboardScreenHandler){
            pos = ((KeyboardScreenHandler) player.currentScreenHandler).pos;
        } else {
            return;
        }
        MinecraftServer server = player.getServer();
        World world = player.getEntityWorld();
//        DuckyPeriphs.logPrint("recieved key press packet: " + key + " on worldtype: " + (world.isClient ? "client" : "server"));
        server.execute(()->{
            KeyboardTile.keyPress(pos, world, key, scancode, modifiers, repeat, pasteText);
        });
    }

    public static void keyUpHandler(PacketByteBuf buf, PacketContext context){
        int key = buf.readInt();
        int scancode = buf.readInt();
        int modifiers = buf.readInt();
        // BlockPos pos = buf.readBlockPos();
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
        BlockPos pos;
        if(player.currentScreenHandler instanceof KeyboardScreenHandler){
            pos = ((KeyboardScreenHandler) player.currentScreenHandler).pos;
        } else {
            return;
        }
        MinecraftServer server = player.getServer();
        World world = player.getEntityWorld();
//        DuckyPeriphs.logPrint("recieved key up packet: " + key + " on worldtype: " + (world.isClient ? "client" : "server"));
        server.execute(()->{
            KeyboardTile.keyUp(pos, world, key, scancode, modifiers);
        });
    }

    

    public static void charTypedHandler(PacketByteBuf buf, PacketContext context){
        char char_sent = buf.readChar();
        int modifiers = buf.readInt();
        // BlockPos pos = buf.readBlockPos();
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
        BlockPos pos;
        if(player.currentScreenHandler instanceof KeyboardScreenHandler){
            pos = ((KeyboardScreenHandler) player.currentScreenHandler).pos;
        } else {
            return;
        }
        MinecraftServer server = player.getServer();
        World world = player.getEntityWorld();
//        DuckyPeriphs.logPrint("recieved char " + char_sent + " on worldtype: " + (world.isClient ? "client" : "server"));
        server.execute(()->{
            KeyboardTile.charTyped(pos, world, char_sent, modifiers);
        });
    }

    public static void eventShortcutHandler(PacketByteBuf buf, PacketContext context){
        int eventCode = buf.readInt();
        // BlockPos pos = buf.readBlockPos();
        ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
        BlockPos pos;
        if(player.currentScreenHandler instanceof KeyboardScreenHandler){
            pos = ((KeyboardScreenHandler) player.currentScreenHandler).pos;
        } else {
            return;
        }
        MinecraftServer server = player.getServer();
        World world = player.getEntityWorld();
//        DuckyPeriphs.logPrint("recieved event " + eventCode + " on worldtype: " + (world.isClient ? "client" : "server"));
        server.execute(()->{
            KeyboardTile.handleEvents(pos, world, eventCode);
        });
    }


}
