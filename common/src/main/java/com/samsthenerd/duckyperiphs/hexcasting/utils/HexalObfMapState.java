package com.samsthenerd.duckyperiphs.hexcasting.utils;

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


public class HexalObfMapState extends PersistentState{
    private BiMap<UUID, Integer> gateMap; //bimap for easy access either way
    private BiMap<UUID, MoteData> moteMap; //uuid is obfuscation uuid - not nexus uuid
    // possible optimization is to clear motes from the map once they're no longer valid.

    public HexalObfMapState() {
        super();
        gateMap = HashBiMap.create();
        moteMap = HashBiMap.create();
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
        while(gateMap.containsKey(uuid)){
            uuid = UUID.randomUUID();
        }
        addGateToMap(uuid, gateInt);
        return uuid;
    }

    @Nullable
    public MoteData getMoteData(UUID uuid){
        return moteMap.get(uuid);
    }

    // require exact item id match i guess?
    @Nullable
    public UUID getMoteObfUUID(MoteData moteData){
        return moteMap.inverse().get(moteData);
    }
    
    public UUID getOrCreateMoteObfUUID(MoteData moteData){
        UUID uuid = getMoteObfUUID(moteData);
        if(uuid == null){
            uuid = newMote(moteData);
        }
        return uuid;
    }

    private void addMoteToMap(UUID uuid, MoteData moteData){
        moteMap.put(uuid, moteData);
        markDirty();
    }

    public UUID newMote(MoteData moteData){
        UUID uuid = UUID.randomUUID();
        while(moteMap.containsKey(uuid)){
            uuid = UUID.randomUUID();
        }
        addMoteToMap(uuid, moteData);
        return uuid;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound gateMapNbt = new NbtCompound();
        for(UUID uuid : gateMap.keySet()){
            gateMapNbt.putInt(uuid.toString(), gateMap.get(uuid));
        }
        nbt.put("hexalGateMap", gateMapNbt);
        NbtCompound moteMapNbt = new NbtCompound();
        for(UUID uuid : moteMap.keySet()){
            MoteData moteData = moteMap.get(uuid);
            NbtCompound moteDataNbt = new NbtCompound();
            moteDataNbt.putUuid("storageUuid", moteData.uuid);
            moteDataNbt.putInt("index", moteData.index);
            moteDataNbt.putString("itemID", moteData.itemID);
            moteMapNbt.put(uuid.toString(), moteDataNbt);
        }
        nbt.put("hexalMoteMap", moteMapNbt);
        return nbt;
    }
 
    public static HexalObfMapState createFromNbt(NbtCompound tag) {
        HexalObfMapState serverState = new HexalObfMapState();
        NbtCompound gateMapNbt = tag.getCompound("hexalGateMap");
        for(String uuidString : gateMapNbt.getKeys()){
            serverState.addGateToMap(UUID.fromString(uuidString), gateMapNbt.getInt(uuidString));
        }
        NbtCompound moteMapNbt = tag.getCompound("hexalMoteMap");
        for(String uuidString : moteMapNbt.getKeys()){
            NbtCompound moteDataNbt = moteMapNbt.getCompound(uuidString);
            MoteData moteData = new MoteData(moteDataNbt.getUuid("storageUuid"), moteDataNbt.getInt("index"), moteDataNbt.getString("itemID"));
            serverState.addMoteToMap(UUID.fromString(uuidString), moteData);
        }
        return serverState;
    }

    public static HexalObfMapState getServerState(MinecraftServer server) {
        // First we get the persistentStateManager for the OVERWORLD
        PersistentStateManager persistentStateManager = server
                .getWorld(World.OVERWORLD).getPersistentStateManager();
 
        // Calling this reads the file from the disk if it exists, or creates a new one and saves it to the disk
        HexalObfMapState serverState = persistentStateManager.getOrCreate(
                HexalObfMapState::createFromNbt,
                HexalObfMapState::new,
                DuckyPeriphs.MOD_ID + ":hexal_gate_map");  // keeping this as 'gate' so we don't break existing data. maybe change it on a version boundary

        serverState.markDirty(); // YOU MUST DO THIS!!!! Or data wont be saved correctly.
 
        return serverState;
    }

    
    // this uuid is the nexus uuid
    public record MoteData(UUID uuid, int index, String itemID){
        public MoteData(UUID uuid, int index, String itemID){
            this.uuid = uuid;
            this.index = index;
            this.itemID = itemID;
        }
    }
}
