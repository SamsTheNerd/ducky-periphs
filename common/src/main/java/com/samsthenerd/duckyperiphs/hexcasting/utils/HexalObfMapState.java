package com.samsthenerd.duckyperiphs.hexcasting.utils;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.util.Either;
import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import ram.talia.hexal.api.spell.iota.GateIota;


public class HexalObfMapState extends PersistentState{
    private BiMap<UUID, Integer> gateMap; // for indices
    private BiMap<UUID, GateData> typedGateMap; // for data
    private BiMap<UUID, MoteData> moteMap; //uuid is obfuscation uuid - not nexus uuid
    // possible optimization is to clear motes from the map once they're no longer valid.

    public HexalObfMapState() {
        super();
        gateMap = HashBiMap.create();
        typedGateMap = HashBiMap.create();
        moteMap = HashBiMap.create();
    }

    @Nullable
    public Integer getGateInt(UUID uuid){
        return gateMap.get(uuid);
    }

    @Nullable
    public GateData getGateData(UUID uuid){
        return typedGateMap.get(uuid);
    }

    // will not create a new gate if it doesn't exist in the mapping
    @Nullable
    public UUID getGateUUID(int gateInt){
        return gateMap.inverse().get(gateInt);
    }

    // gets a UUID for a gate or creates a new one if it doesn't exist
    public UUID getOrCreateGateUUID(GateData gData){
        UUID uuid = getGateUUID(gData.index());
        if(uuid == null){
            uuid = newGate(gData);
        }
        return uuid;
    }

    private void addGateToMap(UUID uuid, GateData gData){
        gateMap.put(uuid, gData.index());
        typedGateMap.put(uuid, gData);
        markDirty();
    }

    public UUID newGate(GateData gData){
        UUID uuid = UUID.randomUUID();
        while(gateMap.containsKey(uuid) || typedGateMap.containsKey(uuid)){
            uuid = UUID.randomUUID();
        }
        addGateToMap(uuid, gData);
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
        for(UUID uuid : typedGateMap.keySet()){
            NbtCompound gateDataNbt = typedGateMap.get(uuid).toNbt();
            gateMapNbt.put(uuid.toString(), gateDataNbt);
        }
        nbt.put("hexalTypedGateMap", gateMapNbt);
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
        NbtCompound typedGateMapNbt = tag.getCompound("hexalTypedGateMap");
        if(typedGateMapNbt == null || typedGateMapNbt.isEmpty()){ // check if the new format is empty and try to read in old format
            NbtCompound gateMapNbt = tag.getCompound("hexalGateMap");
            for(String uuidString : gateMapNbt.getKeys()){
                GateData simpleData = new GateData(gateMapNbt.getInt(uuidString), 0, null, null);
                serverState.addGateToMap(UUID.fromString(uuidString), simpleData);
            }
        } else {
            for(String uuidString : typedGateMapNbt.getKeys()){
                GateData gData = GateData.fromNbt(typedGateMapNbt.getCompound(uuidString));
                serverState.addGateToMap(UUID.fromString(uuidString), gData);
            }
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


    public static GateData GateDataFromIota(GateIota iota){
        Either<Vec3d, FakeEntityAnchor> target = (Either<Vec3d, FakeEntityAnchor>) ((Object)iota.getTarget());
        if(iota.isDrifting() || target == null){
            return new GateData(iota.getGateIndex(), 0, null, null);
        }
        if(target.left().isPresent()){ // anchored 
            return new GateData(iota.getGateIndex(), 1, target.left().get(), null);
        }
        if(target.right().isPresent()){ // entity
            FakeEntityAnchor anchor = target.right().get();
            return new GateData(iota.getGateIndex(), 2, anchor.offset(), anchor.uuid());
        }
        return new GateData(iota.getGateIndex(), 0, null, null);
    }
    
    public record FakeEntityAnchor(UUID uuid, String name, Vec3d offset) { }

    public record GateData(int index, int type, Vec3d tVec, UUID entUuid){
        public GateData(int index, int type, Vec3d tVec, UUID entUuid){
            this.index = index;
            this.type = type;
            this.tVec = tVec;
            this.entUuid = entUuid;
        }

        public NbtCompound toNbt(){
            NbtCompound nbt = new NbtCompound();
            nbt.putInt("index", index);
            nbt.putInt("type", type);
            if(type == 1 || type == 2){
                nbt.putDouble("tVecX", tVec.x);
                nbt.putDouble("tVecY", tVec.y);
                nbt.putDouble("tVecZ", tVec.z);
            }
            if(type == 2){
                nbt.putUuid("entUuid", entUuid);
            }
            return nbt;
        }

        public static GateData fromNbt(NbtCompound nbt){
            int index = nbt.getInt("index");
            int type = nbt.getInt("type");
            Vec3d tVec = null;
            UUID entUuid = null;
            if(type == 1 || type == 2){
                tVec = new Vec3d(nbt.getDouble("tVecX"), nbt.getDouble("tVecY"), nbt.getDouble("tVecZ"));
            }
            if(type == 2){
                entUuid = nbt.getUuid("entUuid");
            }
            return new GateData(index, type, tVec, entUuid);
        }
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
