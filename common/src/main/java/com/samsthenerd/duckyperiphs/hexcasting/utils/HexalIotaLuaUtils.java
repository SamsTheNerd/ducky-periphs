package com.samsthenerd.duckyperiphs.hexcasting.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mojang.datafixers.util.Either;
import com.samsthenerd.duckyperiphs.hexcasting.utils.HexalObfMapState.GateData;
import com.samsthenerd.duckyperiphs.hexcasting.utils.HexalObfMapState.MoteData;

import at.petrak.hexcasting.api.casting.iota.GarbageIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import kotlin.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import ram.talia.hexal.api.casting.iota.GateIota;
import ram.talia.hexal.api.casting.iota.MoteIota;
import ram.talia.hexal.api.mediafieditems.MediafiedItemManager;

public class HexalIotaLuaUtils {
    // just putting it in a separate function for neatness
    public static Iota getHexalIota(Object luaObject, ServerWorld world){
        if(luaObject instanceof Map){
            Map<String, Object> table = (Map<String, Object>) luaObject;

            if(table.containsKey("gate") && table.get("gate") instanceof String){
                if(world != null){
                    HexalObfMapState gateMap = HexalObfMapState.getServerState(((ServerWorld)world).getServer());
                    UUID gateUUID = UUID.fromString((String)table.get("gate"));
                    GateData gData = gateMap.getGateData(gateUUID);
                    if(gData != null){
                        if(gData.type() == 0){ //drifting
                            return new GateIota(gData.index(), null);
                        }
                        if(gData.type() == 1){ // location anchored
                            return new GateIota(gData.index(), Either.left(gData.tVec()));
                        }
                        if(gData.type() == 2){ // entity anchored
                            Pair<Entity, Vec3d> gatePair = new Pair<Entity, Vec3d>(world.getEntity(gData.entUuid()), gData.tVec());
                            return new GateIota(gData.index(), Either.right(gatePair));
                        }
                    }
                }
                return new NullIota();
            }

            if(table.containsKey("moteUuid") && table.get("moteUuid") instanceof String 
            && table.containsKey("itemID") && table.get("itemID") instanceof String){
                UUID moteUUID = UUID.fromString((String)table.get("moteUuid"));
                String itemID = (String)table.get("itemID");
                MoteData moteData = HexalObfMapState.getServerState(((ServerWorld)world).getServer()).getMoteData(moteUUID);

                if(moteData == null || !itemID.equals(moteData.itemID())){
                    return new GarbageIota();
                }
                UUID storageUUID = moteData.uuid();
                Integer index = moteData.index();
                MediafiedItemManager manager = MediafiedItemManager.INSTANCE;
                if(manager != null){
                    MediafiedItemManager.Index mediafiedIndex = new MediafiedItemManager.Index(storageUUID, index);
                    return new MoteIota(mediafiedIndex);
                }
                return new NullIota();
            }
        }
        
        return null;
    }

    public static Object getHexalObject(Iota iota, ServerWorld world){

        if(iota instanceof GateIota){
            if(world != null){
                GateIota gateIota = (GateIota)iota;
                GateData gData = HexalObfMapState.GateDataFromIota(gateIota);
                // check if we have the uuid
                UUID thisGateUUID = HexalObfMapState.getServerState(((ServerWorld)world).getServer()).getOrCreateGateUUID(gData);
                Map<String, Object> gateTable = new HashMap<String, Object>();
                gateTable.put("gate", thisGateUUID.toString());
                if(gData.type() == 0){
                    gateTable.put("gateType", "drifting");
                } else {
                    Map<String, Double> locationTable = new HashMap<String, Double>();
                    locationTable.put("x", gData.tVec().x);
                    locationTable.put("y", gData.tVec().y);
                    locationTable.put("z", gData.tVec().z);
                    if(gData.type() == 1){
                        gateTable.put("gateType", "location");
                        gateTable.put("location", locationTable);
                    } else if(gData.type() == 2){
                        gateTable.put("gateType", "entity");
                        gateTable.put("entity", gData.entUuid().toString());
                        gateTable.put("offset", locationTable);
                    }
                }
                return gateTable;
            }
            return null;
        }

        if(iota instanceof MoteIota){
            MediafiedItemManager.Index itemIndex = ((MoteIota)iota).getItemIndex();
            if(itemIndex == null){
                return null;
            }
            UUID uuid = itemIndex.getStorage();
            int index = itemIndex.getIndex();
            String itemID = ((MoteIota)iota).getItem().toString();
            MoteData moteData = new MoteData(uuid, index, itemID);
            UUID thisMoteUUID = HexalObfMapState.getServerState(((ServerWorld)world).getServer()).getOrCreateMoteObfUUID(moteData);
            Map<String, String> itemTable = new HashMap<String, String>();
            itemTable.put("moteUuid", thisMoteUUID.toString());
            itemTable.put("itemID", itemID);
            itemTable.put("nexusUuid", uuid.toString());
            return itemTable;
        }

        return null;
    }
}
