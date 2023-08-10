package com.samsthenerd.duckyperiphs.hexcasting.utils;

import java.util.HashMap;
import java.util.Map;

import org.jblas.DoubleMatrix;
import org.jetbrains.annotations.Nullable;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import at.petrak.hexcasting.api.casting.iota.GarbageIota;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota;
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
