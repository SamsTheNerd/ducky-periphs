package com.samsthenerd.duckyperiphs.hexcasting;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

public class HexalGateMapState extends PersistentState{
    private BiMap<UUID, Integer> gateMap; //bimap for easy access either way


    public HexalGateMapState() {
        super();
        gateMap = HashBiMap.create();
    }

    @Nullable
    public Integer getGateInt(UUID uuid){
        return gateMap.get(uuid);
    }

    // will not create a new gate if it doesn't exist in the mapping
    @Nullable
    public UUID getGateUUID(int gateInt){
        return gateMap.inverse().get(gateInt);
    }

    // gets a UUID for a gate or creates a new one if it doesn't exist
    public UUID getOrCreateGateUUID(int gateInt){
        UUID uuid = getGateUUID(gateInt);
        if(uuid == null){
            uuid = newGate(gateInt);
        }
        return uuid;
    }

    private void addGateToMap(UUID uuid, int gateInt){
        gateMap.put(uuid, gateInt);
        markDirty();
    }

    public UUID newGate(int gateInt){
        UUID uuid = UUID.randomUUID();
        addGateToMap(uuid, gateInt);
        return uuid;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound gateMapNbt = new NbtCompound();
        for(UUID uuid : gateMap.keySet()){
            gateMapNbt.putInt(uuid.toString(), gateMap.get(uuid));
        }
        nbt.put("hexalGateMap", gateMapNbt);
        return nbt;
    }
 
    public static HexalGateMapState createFromNbt(NbtCompound tag) {
        HexalGateMapState serverState = new HexalGateMapState();
        NbtCompound gateMapNbt = tag.getCompound("hexalGateMap");
        for(String uuidString : gateMapNbt.getKeys()){
            serverState.addGateToMap(UUID.fromString(uuidString), gateMapNbt.getInt(uuidString));
        }
        return serverState;
    }

    public static HexalGateMapState getServerState(MinecraftServer server) {
        // First we get the persistentStateManager for the OVERWORLD
        PersistentStateManager persistentStateManager = server
                .getWorld(World.OVERWORLD).getPersistentStateManager();
 
        // Calling this reads the file from the disk if it exists, or creates a new one and saves it to the disk
        HexalGateMapState serverState = persistentStateManager.getOrCreate(
                HexalGateMapState::createFromNbt,
                HexalGateMapState::new,
                DuckyPeriphs.MOD_ID + ":hexal_gate_map"); 
 
        serverState.markDirty(); // YOU MUST DO THIS!!!! Or data wont be saved correctly.
 
        return serverState;
    }
}
