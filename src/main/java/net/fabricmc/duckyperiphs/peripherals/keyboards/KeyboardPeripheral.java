package net.fabricmc.duckyperiphs.peripherals.keyboards;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

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
    public boolean equals(IPeripheral other) {
        return this == other;
    }

    @Override
    public void attach(IComputerAccess computer) {
        computers.add(computer);
    }

    @Override
    public void detach(IComputerAccess computer) {
        computers.remove(computer);
    }


    public void sendKey(int key, Boolean repeat) {
        for (IComputerAccess computer : computers) {
            // computer.queueEvent("key", key);
            computer.queueEvent("key", new Object[] { key, repeat});
        }
    }

    public void sendKeyUp(int key){
        for (IComputerAccess computer : computers) {
            // computer.queueEvent("key", key);
            computer.queueEvent("key_up", new Object[] { key});
        }
    }

    public void sendChar(char c){
        String stringyChar = Character.toString(c);
        for (IComputerAccess computer : computers) {
            computer.queueEvent("char", stringyChar);
        }
    }

    public void sendPaste(String text){
        for (IComputerAccess computer : computers) {
            computer.queueEvent("paste", text);
        }
    }

    public void sendTerminate(){
        for (IComputerAccess computer : computers) {
            computer.queueEvent("terminate");
        }
    }

    public void sendShutdown(){
        for (IComputerAccess computer : computers) {
            ComputerCraft.serverComputerRegistry.lookup(computer.getID()).shutdown();;
            computer.queueEvent("shutdown");
        }
    }

    public void sendReboot(){
        for (IComputerAccess computer : computers) {
            ComputerCraft.serverComputerRegistry.lookup(computer.getID()).reboot();;
            computer.queueEvent("reboot");
        }
    }


}
