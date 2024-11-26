package com.samsthenerd.duckyperiphs.peripherals.keyboards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.core.ServerComputerRegistry;
import dan200.computercraft.shared.computer.core.ServerContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.core.jmx.Server;
import org.jetbrains.annotations.NotNull;

public class KeyboardPeripheral implements IPeripheral {
    private final KeyboardTile kbTile;

    public Set<IComputerAccess> computers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public KeyboardPeripheral(KeyboardTile kbTile) {
        this.kbTile = kbTile;
    }

    @Nonnull
    @Override
    public String getType() {
        return "keyboard";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return this == other;
    }

    @Override
    public void attach(@Nonnull IComputerAccess computer) {
        computers.add(computer);
    }

    @Override
    public void detach(@Nonnull IComputerAccess computer) {
        computers.remove(computer);
    }

    // returns if the computer can be used by the player
    private boolean guardCommandComputers(IComputerAccess access, @NotNull ServerPlayerEntity player){
        ServerComputer computer = ServerContext.get(player.getServer()).registry().get(access.getID());
        return computer != null && computer.checkUsable(player);
    }

    public void sendKey(int key, Boolean repeat, ServerPlayerEntity player) {
        for (IComputerAccess computer : computers) {
            // computer.queueEvent("key", key);
            if(!guardCommandComputers(computer, player)) continue;
            computer.queueEvent("key", key, repeat, computer.getAttachmentName());
        }
    }

    public void sendKeyUp(int key, ServerPlayerEntity player){
        for (IComputerAccess computer : computers) {
            // computer.queueEvent("key", key);
            if(!guardCommandComputers(computer, player)) continue;
            computer.queueEvent("key_up", key, computer.getAttachmentName());
        }
    }

    public void sendChar(char c, ServerPlayerEntity player){
        String stringyChar = Character.toString(c);
        for (IComputerAccess computer : computers) {
            if(!guardCommandComputers(computer, player)) continue;
            computer.queueEvent("char", stringyChar, computer.getAttachmentName());
        }
    }

    public void sendPaste(String text, ServerPlayerEntity player){
        for (IComputerAccess computer : computers) {
            if(!guardCommandComputers(computer, player)) continue;
            computer.queueEvent("paste", text, computer.getAttachmentName());
        }
    }

    public void sendTerminate(ServerPlayerEntity player){
        for (IComputerAccess computer : computers) {
            if(!guardCommandComputers(computer, player)) continue;
            computer.queueEvent("terminate", computer.getAttachmentName());
        }
    }

    public void sendShutdown(ServerPlayerEntity player){
        for (IComputerAccess computer : computers) {
            if(!guardCommandComputers(computer, player)) continue;
            getServerComputer(computer).shutdown();
            computer.queueEvent("shutdown", computer.getAttachmentName());
        }
    }

    public void sendReboot(ServerPlayerEntity player){
        for (IComputerAccess computer : computers) {
            if(!guardCommandComputers(computer, player)) continue;
            getServerComputer(computer).reboot();
            computer.queueEvent("reboot", computer.getAttachmentName());
        }
    }

    private ServerComputer getServerComputer(IComputerAccess computer){
        return ServerContext.get(kbTile.getWorld().getServer()).registry().get(computer.getID());
    }

}
