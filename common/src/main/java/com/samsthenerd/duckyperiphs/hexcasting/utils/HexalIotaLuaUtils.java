package com.samsthenerd.duckyperiphs.hexcasting.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.mojang.datafixers.util.Either;
import com.samsthenerd.duckyperiphs.hexcasting.utils.HexalObfMapState.MoteData;

import at.petrak.hexcasting.api.spell.iota.GarbageIota;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.IotaType;
import at.petrak.hexcasting.api.spell.iota.NullIota;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import ram.talia.hexal.api.mediafieditems.MediafiedItemManager;
import ram.talia.hexal.api.spell.iota.EntityTypeIota;
import ram.talia.hexal.api.spell.iota.GateIota;
import ram.talia.hexal.api.spell.iota.IotaTypeIota;
import ram.talia.hexal.api.spell.iota.ItemIota;
import ram.talia.hexal.api.spell.iota.ItemTypeIota;

public class HexalIotaLuaUtils {
    // just putting it in a separate function for neatness
    public static Iota getHexalIota(Object luaObject, ServerWorld world){
        if(luaObject instanceof Map){
            Map<String, Object> table = (Map) luaObject;

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
                EntityType<?> type = Registry.ENTITY_TYPE.get(typeLoc);
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
                    Item type = Registry.ITEM.get(typeLoc);
                    if(type == null){
                        return new GarbageIota();
                    }
                    return new ItemTypeIota(type);
                } else {
                    Block type = Registry.BLOCK.get(typeLoc);
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
                    Integer gateInt = gateMap.getGateInt(gateUUID);
                    if(gateInt != null){
                        return new GateIota(gateInt);
                    }
                }
                return new NullIota();
            }

            if(table.containsKey("moteUuid") && table.get("moteUuid") instanceof String 
            && table.containsKey("itemID") && table.get("itemID") instanceof String){
                UUID moteUUID = UUID.fromString((String)table.get("moteUuid"));
                String itemID = (String)table.get("itemID");
                MoteData moteData = HexalObfMapState.getServerState(((ServerWorld)world).getServer()).getMoteData(moteUUID);
                if(moteData == null || moteData.itemID() != itemID){
                    return new GarbageIota();
                }
                UUID storageUUID = moteData.uuid();
                Integer index = moteData.index();
                MediafiedItemManager manager = MediafiedItemManager.INSTANCE;
                if(manager != null){
                    MediafiedItemManager.Index mediafiedIndex = new MediafiedItemManager.Index(storageUUID, index);
                    return new ItemIota(mediafiedIndex);
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
            Optional<RegistryKey<EntityType<?>>> typeLoc = Registry.ENTITY_TYPE.getKey(type);
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
                Optional<RegistryKey<Item>> typeLoc = Registry.ITEM.getKey(item.get());
                if(typeLoc.isPresent()){
                    typeTable.put("itemType", typeLoc.get().getValue().toString());
                    typeTable.put("isItem", true);
                } else {
                    return null;
                }
            } else {
                Optional<Block> block = type.right();
                if(block.isPresent()){
                    Optional<RegistryKey<Block>> typeLoc = Registry.BLOCK.getKey(block.get());
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
                int gateIndex = gateIota.getGateIndex();
                // check if we have the uuid
                Map<String, String> gateTable = new HashMap<String, String>();
                UUID thisGateUUID = HexalObfMapState.getServerState(((ServerWorld)world).getServer()).getOrCreateGateUUID(gateIndex);
                gateTable.put("gate", thisGateUUID.toString());
                return gateTable;
            }
            return null;
        }

        if(iota instanceof ItemIota){
            MediafiedItemManager.Index itemIndex = ((ItemIota)iota).getItemIndex();
            if(itemIndex == null){
                return null;
            }
            UUID uuid = itemIndex.getStorage();
            int index = itemIndex.getIndex();
            String itemID = ((ItemIota)iota).getItem().toString();
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
