package com.samsthenerd.duckyperiphs.peripherals.EntityDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class EntityDetectorPeripheral implements IPeripheral {
    public static int DEFAULT_RANGE = 8;
    public static int DEFAULT_Y_RANGE = 16;
    
    private final EntityDetectorTile edTile;

    public List<IComputerAccess> computers = new ArrayList<IComputerAccess>();
    
    EntityDetectorPeripheral(EntityDetectorTile edPTile){
        edTile = edPTile;
    }
    
    @Nonnull
    @Override
    public String getType() {
        return "entity_detector";
    }
    
    @Override
    public boolean equals(IPeripheral other) {
        return this == other;
    }


    @LuaFunction
    public final Map<String, Object>[] nearbyEntities(){
        Box rangeBox = edTile.makeBox(DEFAULT_RANGE, DEFAULT_Y_RANGE);
        List<Entity> entities = edTile.nearbyEntities(rangeBox); // *should* be just living entities
        // need to decide what data we actually want to return from here
        
        @SuppressWarnings("unchecked") // make vscode shut up about unchecked typecast juice.
        Map<String, Object>[] entityMaps = (Map<String, Object>[]) new Map[entities.size()];

        BlockPos edPos = edTile.getPos();

        // for each entity, make a map, add relevant values, add it to array
        for(int e = 0; e < entities.size(); e++){
            Entity entity = entities.get(e);
            Map<String, Object> entityMap = new HashMap<String, Object>();
            entityMap.put("uuid", entity.getUuid().toString());
            entityMap.put("name", entity.getName().getString());
            boolean isPlayer = entity.isPlayer();
            entityMap.put("isPlayer", isPlayer);
            entityMap.put("type", entity.getType().getName().getString());
            entityMap.put("x", entity.getX() - edPos.getX());
            entityMap.put("y", entity.getY() - edPos.getY());
            entityMap.put("z", entity.getZ() - edPos.getZ());
            // feel free to add more here I suppose. I didn't think anything else was super relevant for most use cases?
            entityMaps[e] = entityMap;
        }


        return entityMaps;
    }

    @Override
    public void attach(IComputerAccess computer) {
        computers.add(computer);
    }

    @Override
    public void detach(IComputerAccess computer) {
        computers.remove(computer);
    }

    public void newEntityEvent(Entity entity){
        BlockPos edPos = edTile.getPos();
        Map<String, Object> entityMap = new HashMap<String, Object>();
        entityMap.put("uuid", entity.getUuid().toString());
        entityMap.put("name", entity.getName().getString());
        boolean isPlayer = entity.isPlayer();
        entityMap.put("isPlayer", isPlayer);
        entityMap.put("type", entity.getType().getName().getString());
        entityMap.put("x", entity.getX() - edPos.getX());
        entityMap.put("y", entity.getY() - edPos.getY());
        entityMap.put("z", entity.getZ() - edPos.getZ());
        for(IComputerAccess computer : computers){
            computer.queueEvent("new_entity", computer.getAttachmentName(), entityMap);
        }
    }

    public void removedEntityEvent(Entity entity){
        BlockPos edPos = edTile.getPos();
        Map<String, Object> entityMap = new HashMap<String, Object>();
        entityMap.put("uuid", entity.getUuid().toString());
        entityMap.put("name", entity.getName().getString());
        boolean isPlayer = entity.isPlayer();
        entityMap.put("isPlayer", isPlayer);
        entityMap.put("type", entity.getType().getName().getString());
        entityMap.put("x", entity.getX() - edPos.getX());
        entityMap.put("y", entity.getY() - edPos.getY());
        entityMap.put("z", entity.getZ() - edPos.getZ());
        for(IComputerAccess computer : computers){
            computer.queueEvent("removed_entity", computer.getAttachmentName(), entityMap);
        }
    }

}
