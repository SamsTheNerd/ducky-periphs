package net.fabricmc.duckyperiphs.peripherals.sculkophone;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.world.event.GameEvent;

public class SculkophonePeripheral  implements IPeripheral {
    public static int DEFAULT_RANGE = 8;
    
    private final SculkophoneBlockEntity sBEntity; 

    public List<IComputerAccess> computers = new ArrayList<IComputerAccess>();
    
    SculkophonePeripheral(SculkophoneBlockEntity SBE){
        sBEntity = SBE;
    }
    
    @Nonnull
    @Override
    public String getType() {
        return "sculkophone";
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

    public void vibrationEvent(GameEvent vibrationEvent, double distance){
        for(IComputerAccess computer : computers){
            computer.queueEvent("vibration", vibrationEvent.getId(), distance);
        }
    }

}

