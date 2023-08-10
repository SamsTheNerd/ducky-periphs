package com.samsthenerd.duckyperiphs.hexcasting.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.mojang.datafixers.util.Either;
import com.samsthenerd.duckyperiphs.hexcasting.utils.HexalObfMapState.GateData;
import com.samsthenerd.duckyperiphs.hexcasting.utils.HexalObfMapState.MoteData;

import at.petrak.hexcasting.api.casting.iota.GarbageIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.NullIota;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import kotlin.Pair;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import ram.talia.hexal.api.casting.iota.GateIota;
import ram.talia.hexal.api.casting.iota.MoteIota;
import ram.talia.hexal.api.mediafieditems.MediafiedItemManager;
import ram.talia.moreiotas.api.casting.iota.EntityTypeIota;
import ram.talia.moreiotas.api.casting.iota.IotaTypeIota;
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota;

public class HexalIotaLuaUtils {
    // just putting it in a separate function for neatness
    public static Iota getHexalIota(Object luaObject, ServerWorld world){
        if(luaObject instanceof Map){
            Map<String, Object> table = (Map<String, Object>) luaObject;

            if(table.containsKey("iotaType") && table.get("iotaType") instanceof String){
                String typeKey = (String)table.get("iotaType");
                if (!Identifier.isValid(typeKey)) {
                    return new GarbageIota();
                }
                var typeLoc = new Identifier(typeKey);
                IotaType<?> type = HexIotaTypes.REGISTRY.get(typeLoc);
                if(type == null){
                    return new GarbageIota();
                }
                return new IotaTypeIota(type);
            }
            
            if(table.containsKey("entityType") && table.get("entityType") instanceof String){
                String typeKey = (String)table.get("entityType");
                if (!Identifier.isValid(typeKey)) {
                    return new GarbageIota();
                }
                var typeLoc = new Identifier(typeKey);
                EntityType<?> type = Registries.ENTITY_TYPE.get(typeLoc);
                if(type == null){
                    return new GarbageIota();
                }
                return new EntityTypeIota(type);
            }

            // the isItem tag is for if it's an item or block
            if(table.containsKey("itemType") && table.get("itemType") instanceof String
                && table.containsKey("isItem") && table.get("isItem") instanceof Boolean){
                String typeKey = (String)table.get("itemType");
                Boolean isItem = (Boolean)table.get("isItem");
                if (!Identifier.isValid(typeKey)) {
                    return new GarbageIota();
                }
                var typeLoc = new Identifier(typeKey);
                if(isItem){
                    Item type = Registries.ITEM.get(typeLoc);
                    if(type == null){
                        return new GarbageIota();
                    }
                    return new ItemTypeIota(type);
                } else {
                    Block type = Registries.BLOCK.get(typeLoc);
                    if(type == null){
                        return new GarbageIota();
                    }
                    return new ItemTypeIota(type);
                }
            }

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
        if(iota instanceof IotaTypeIota){
            IotaType<?> type = ((IotaTypeIota)iota).getIotaType();
            Map<String, Object> typeTable = new HashMap<String, Object>();
            Optional<RegistryKey<IotaType<?>>> typeLoc = HexIotaTypes.REGISTRY.getKey(type);
            if(typeLoc.isPresent()){
                typeTable.put("iotaType", typeLoc.get().getValue().toString());
            } else {
                return null;
            }
            return typeTable;
        }

        if(iota instanceof EntityTypeIota){
            EntityType<?> type = ((EntityTypeIota)iota).getEntityType();
            Map<String, Object> typeTable = new HashMap<String, Object>();
            Optional<RegistryKey<EntityType<?>>> typeLoc = Registries.ENTITY_TYPE.getKey(type);
            if(typeLoc.isPresent()){
                typeTable.put("entityType", typeLoc.get().getValue().toString());
            } else {
                return null;
            }
            return typeTable;
        }

        if(iota instanceof ItemTypeIota){
            Either<Item,Block> type = ((ItemTypeIota)iota).getEither();
            Map<String, Object> typeTable = new HashMap<String, Object>();
            Optional<Item> item = type.left();
            if(item.isPresent()){
                Optional<RegistryKey<Item>> typeLoc = Registries.ITEM.getKey(item.get());
                if(typeLoc.isPresent()){
                    typeTable.put("itemType", typeLoc.get().getValue().toString());
                    typeTable.put("isItem", true);
                } else {
                    return null;
                }
            } else {
                Optional<Block> block = type.right();
                if(block.isPresent()){
                    Optional<RegistryKey<Block>> typeLoc = Registries.BLOCK.getKey(block.get());
                    if(typeLoc.isPresent()){
                        typeTable.put("itemType", typeLoc.get().getValue().toString());
                        typeTable.put("isItem", false);
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
            return typeTable;
        }

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
