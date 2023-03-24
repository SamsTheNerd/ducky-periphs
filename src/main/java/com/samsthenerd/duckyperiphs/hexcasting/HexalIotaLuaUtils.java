package com.samsthenerd.duckyperiphs.hexcasting;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.mojang.datafixers.util.Either;

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
import ram.talia.hexal.api.spell.iota.EntityTypeIota;
import ram.talia.hexal.api.spell.iota.GateIota;
import ram.talia.hexal.api.spell.iota.IotaTypeIota;
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
                    HexalGateMapState gateMap = HexalGateMapState.getServerState(((ServerWorld)world).getServer());
                    UUID gateUUID = UUID.fromString((String)table.get("gate"));
                    Integer gateInt = gateMap.getGateInt(gateUUID);
                    if(gateInt != null){
                        return new GateIota(gateInt);
                    }
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
                UUID thisGateUUID = HexalGateMapState.getServerState(((ServerWorld)world).getServer()).getOrCreateGateUUID(gateIndex);
                gateTable.put("gate", thisGateUUID.toString());
                return gateTable;
            }
            return null;
        }

        return null;
    }
}
