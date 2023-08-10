package com.samsthenerd.duckyperiphs.hexcasting.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jblas.DoubleMatrix;
import org.jetbrains.annotations.Nullable;

import com.mojang.datafixers.util.Either;
import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import at.petrak.hexcasting.api.casting.iota.GarbageIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import ram.talia.moreiotas.api.casting.iota.EntityTypeIota;
import ram.talia.moreiotas.api.casting.iota.IotaTypeIota;
import ram.talia.moreiotas.api.casting.iota.ItemTypeIota;
import ram.talia.moreiotas.api.casting.iota.MatrixIota;
import ram.talia.moreiotas.api.casting.iota.StringIota;

public class MoreIotasIotaLuaUtils {
    @Nullable
    public static Iota getMoreIotasIota(Object luaObject){
        if(luaObject instanceof String){
            String string = (String)luaObject;
            if(string != null){
                try{
                    return StringIota.make(string);
                } catch (MishapInvalidIota e){
                    DuckyPeriphs.logPrint("Invalid string iota (too long): " + string);
                    return new GarbageIota();
                }
            }
        }
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

            if(table.containsKey("col") && table.get("col") instanceof Number
                && table.containsKey("row") && table.get("row") instanceof Number
                && table.containsKey("matrix") && table.get("matrix") instanceof Map){
                int col = ((Number)table.get("col")).intValue();
                int row = ((Number)table.get("row")).intValue();
                // should just default to full of 0?
                DoubleMatrix matrix = new DoubleMatrix(row, col);
                Map<Double, Object> matrixTable = (Map)table.get("matrix");
                // just check for each, ignore extra and keep zero if 
                for(int i = 1; i <= col*row; i++){
                    if(matrixTable.containsKey((double) i) && matrixTable.get((double) i) instanceof Number){
                        matrix.put(i-1, ((Number)matrixTable.get((double) i)).doubleValue());
                    }
                }
                try{
                    return new MatrixIota(matrix);
                } catch (MishapInvalidIota e){
                    DuckyPeriphs.logPrint("Invalid matrix iota (too big?): " + matrix);
                    return new GarbageIota();
                }
            }
        }
        return null; // not a moreIotas object
    }

    @Nullable
    public static Object getMoreIotasObject(Iota iota){
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
        if(iota instanceof StringIota){
            return ((StringIota)iota).getString();
        }
        if(iota instanceof MatrixIota){
            DoubleMatrix matrix = ((MatrixIota)iota).getMatrix();
            Map<String, Object> matrixTable = new HashMap<String, Object>();
            matrixTable.put("col", matrix.columns);
            matrixTable.put("row", matrix.rows);
            Map<Double, Object> matrixData = new HashMap<Double, Object>();
            for(int i = 1; i <= matrix.columns*matrix.rows; i++){
                matrixData.put((double) i , matrix.get(i-1));
            }
            matrixTable.put("matrix", matrixData);
            return matrixTable;
        }
        return null;
    }
}
