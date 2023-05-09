package com.samsthenerd.duckyperiphs.peripherals.keyboards;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;

public class KeyboardPeripheral implements IPeripheral {
    private final KeyboardTile kbTile;

    public List<IComputerAccess> computers = new ArrayList<IComputerAccess>();

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


    public void sendKey(int key, Boolean repeat) {
        for (IComputerAccess computer : computers) {
            // computer.queueEvent("key", key);
            computer.queueEvent("key", key, repeat, computer.getAttachmentName());
        }
    }

    public void sendKeyUp(int key){
        for (IComputerAccess computer : computers) {
            // computer.queueEvent("key", key);
            computer.queueEvent("key_up", key, computer.getAttachmentName());
        }
    }

    public void sendChar(char c){
        String stringyChar = Character.toString(c);
        for (IComputerAccess computer : computers) {
            computer.queueEvent("char", stringyChar, computer.getAttachmentName());
        }
    }

    public void sendPaste(String text){
        for (IComputerAccess computer : computers) {
            computer.queueEvent("paste", text, computer.getAttachmentName());
        }
    }

    public void sendTerminate(){
        for (IComputerAccess computer : computers) {
            computer.queueEvent("terminate", computer.getAttachmentName());
        }
    }

    public void sendShutdown(){
        for (IComputerAccess computer : computers) {
            ComputerCraft.serverComputerRegistry.lookup(computer.getID()).shutdown();;
            computer.queueEvent("shutdown", computer.getAttachmentName());
        }
    }

    public void sendReboot(){
        for (IComputerAccess computer : computers) {
            ComputerCraft.serverComputerRegistry.lookup(computer.getID()).reboot();;
            computer.queueEvent("reboot", computer.getAttachmentName());
        }
    }


}
