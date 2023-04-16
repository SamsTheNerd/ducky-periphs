package com.samsthenerd.duckyperiphs.hexcasting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import at.petrak.hexcasting.api.spell.SpellList;
import at.petrak.hexcasting.api.spell.iota.BooleanIota;
import at.petrak.hexcasting.api.spell.iota.DoubleIota;
import at.petrak.hexcasting.api.spell.iota.EntityIota;
import at.petrak.hexcasting.api.spell.iota.GarbageIota;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.ListIota;
import at.petrak.hexcasting.api.spell.iota.NullIota;
import at.petrak.hexcasting.api.spell.iota.PatternIota;
import at.petrak.hexcasting.api.spell.iota.Vec3Iota;
import at.petrak.hexcasting.api.spell.math.HexAngle;
import at.petrak.hexcasting.api.spell.math.HexDir;
import at.petrak.hexcasting.api.spell.math.HexPattern;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

/*
 * Functions to help with translating between Lua and Iotas
 */
public class IotaLuaUtils {
    // Convert a Lua object to an Iota
    // needs world for entity and gate. If you're absolutely sure you're not passing an entity or gate, you can pass null
    public static Iota getIota(Object LuaObject, ServerWorld world){
        if(LuaObject == null){
            return new NullIota();
        }

        if(LuaObject instanceof Number){
            return new DoubleIota(((Number)LuaObject).doubleValue());
        }

        if(LuaObject instanceof Boolean){
            return new BooleanIota((Boolean)LuaObject);
        }

        if(LuaObject instanceof Map){
            @SuppressWarnings("unchecked")
            Map<?, Object> rawTable = (Map<?, Object>) LuaObject;

            if(rawTable.isEmpty()){
                return new ListIota(new ArrayList<Iota>()); // sure ?
            }

            // check what type the keys are - assuming it doesn't mix types ? seems reasonable?
            Class<?> keyClass = rawTable.keySet().iterator().next().getClass();

            // is an array 
            if(keyClass == Double.class){
                @SuppressWarnings("unchecked")
                Map<Double, Object> tableArray = (Map<Double, Object>) rawTable;
                ListIota list = tryMakeListIota(tableArray, world);
                if(list != null){
                    return list;
                }
            }

            // is a string keyed table
            if(keyClass == String.class){
                @SuppressWarnings("unchecked")
                Map<String, Object> table = (Map<String, Object>) rawTable;

                // return an empty list if we get handed an empty map
                if(table.size() == 0){
                    return new ListIota(new ArrayList<Iota>());
                }

                // handle vec3
                if(table.size() == 3 && table.containsKey("x") && table.containsKey("y") && table.containsKey("z")){
                    double x;
                    double y;
                    double z;
                    if(table.get("x") instanceof Number && table.get("y") instanceof Number && table.get("z") instanceof Number){
                        x = ((Number)table.get("x")).doubleValue();
                        y = ((Number)table.get("y")).doubleValue();
                        z = ((Number)table.get("z")).doubleValue();
                        return new Vec3Iota(new Vec3d(x,y,z));
                    }
                    return new GarbageIota();
                }

                
                // handle spell pattern
                if(table.size() == 2 && table.containsKey("startDir") && table.containsKey("angles")){
                    String startDirRaw;
                    String anglesSeqRaw;
                    if(table.get("startDir") instanceof String && table.get("angles") instanceof String){
                        startDirRaw = (String)table.get("startDir");
                        anglesSeqRaw = (String)table.get("angles");
                        HexDir startDir = HexDir.fromString(startDirRaw);
                        List<HexAngle> anglesSeq = stringToAngleSeq(anglesSeqRaw);
                        if(startDir == null || anglesSeq == null){
                            return new GarbageIota();
                        }
                        HexPattern pat = new HexPattern(startDir, anglesSeq);
                        return new PatternIota(pat);
                    }
                    return new GarbageIota();
                }

                // handle non player entities
                if(table.containsKey("uuid")){
                    String uuidString;
                    if(table.get("uuid") instanceof String){
                        uuidString = (String)table.get("uuid");
                        if(uuidString == null){
                            return new GarbageIota();
                        }
                        Entity ent = world.getEntity(UUID.fromString(uuidString));
                        if(ent == null){
                            return new GarbageIota();
                        }
                        if(ent.isPlayer()){
                            return new NullIota();
                        }
                        return new EntityIota(ent);
                    }
                    return new GarbageIota();
                }
            }
        }

        if(FabricLoader.getInstance().isModLoaded("hexal")){
            Iota hexalIota = HexalIotaLuaUtils.getHexalIota(LuaObject, world);
            if(hexalIota != null){
                return hexalIota;
            }
        }

        if(FabricLoader.getInstance().isModLoaded("moreiotas")){
            Iota moreIotasIota = MoreIotasIotaLuaUtils.getMoreIotasIota(LuaObject);
            if(moreIotasIota != null){
                return moreIotasIota;
            }
        }


        // couldn't find anything useful from it (this also handles purposeful garbage)
        return new GarbageIota();
    }

    // server world is only used for gates
    public static Object getLuaObject(Iota iota, ServerWorld world){
        if(iota instanceof NullIota){
            return null;
        }

        if(iota instanceof GarbageIota){
            return makeGarbageTable();
        }

        if(iota instanceof DoubleIota){
            return ((DoubleIota)iota).getDouble();
        }

        if(iota instanceof BooleanIota){
            return ((BooleanIota)iota).getBool();
        }

        if(iota instanceof Vec3Iota){
            Vec3d vec = ((Vec3Iota)iota).getVec3();
            Map<String, Object> vecTable = new HashMap<String, Object>();
            vecTable.put("x", vec.x);
            vecTable.put("y", vec.y);
            vecTable.put("z", vec.z);
            return vecTable;
        }

        if(iota instanceof ListIota){
            SpellList list = ((ListIota)iota).getList();
            List<Object> luaList = new ArrayList<Object>();
            for(Iota i : list){
                luaList.add(getLuaObject(i, world));
            }
            return luaList;
        }


        if(iota instanceof EntityIota){
            // need to add in player handling here later
            Entity ent = ((EntityIota)iota).getEntity();
            if(ent.isPlayer()){
                return null;
            }
            Map<String, Object> entTable = new HashMap<String, Object>();
            entTable.put("uuid", ent.getUuidAsString());
            entTable.put("name", ent.getName().getString());
            return entTable;
        }

        if(iota instanceof PatternIota){
            HexPattern pat = ((PatternIota)iota).getPattern();
            Map<String, Object> patTable = new HashMap<String, Object>();
            patTable.put("startDir", pat.getStartDir().toString()); // not sure this will be quite right but we'll see
            patTable.put("angles", pat.anglesSignature());
            return patTable;
        }

        if(FabricLoader.getInstance().isModLoaded("hexal")){
            Object hexalIota = HexalIotaLuaUtils.getHexalObject(iota, world);
            if(hexalIota != null){
                return hexalIota;
            }
        }

        if(FabricLoader.getInstance().isModLoaded("moreiotas")){
            Object moreIotasObject = MoreIotasIotaLuaUtils.getMoreIotasObject(iota);
            if(moreIotasObject != null){
                return moreIotasObject;
            }
        }

        // couldn't find anything useful from it
        return null;
    }

    private static Map<String, Object> makeGarbageTable(){
        Map<String,Object> garbageTable = new HashMap<String, Object>();
        garbageTable.put("garbage", true);
        return garbageTable;
    }

    private static List<HexAngle> stringToAngleSeq(String anglesSeqRaw){
        List<HexAngle> anglesSeq = new ArrayList<HexAngle>();
        for(int i = 0; i < anglesSeqRaw.length(); i++){
            HexAngle angle = angleFromChar(anglesSeqRaw.substring(i, i+1));
            if(angle == null){
                return null;
            }
            anglesSeq.add(angle);
        }
        return anglesSeq;
    }

    private static HexAngle angleFromChar(String _c){
        switch(_c){
            case("w"):
                return HexAngle.FORWARD;
            case("e"):
                return HexAngle.RIGHT;
            case("d"):
                return HexAngle.RIGHT_BACK;
            case("s"):
                return HexAngle.BACK;
            case("a"):
                return HexAngle.LEFT_BACK;
            case("q"):
                return HexAngle.LEFT;
            default:
                return null;
        }
    }

    private static ListIota tryMakeListIota(Map<Double, Object> luaTable, ServerWorld world){
        int keyCount = luaTable.size();
        List<Iota> iotaList = new ArrayList<Iota>(keyCount);
        for(int i = 1; i <= keyCount; i++){
            if(!luaTable.containsKey((double) i)){
                return null;
            }
            iotaList.add(i-1, getIota(luaTable.get((double) i), world));
        }
        return new ListIota(iotaList);
    }

}
