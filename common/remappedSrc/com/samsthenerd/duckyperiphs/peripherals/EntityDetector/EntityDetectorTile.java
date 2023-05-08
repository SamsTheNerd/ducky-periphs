package com.samsthenerd.duckyperiphs.peripherals.EntityDetector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.samsthenerd.duckyperiphs.DuckyPeriph;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class EntityDetectorTile extends TileGeneric implements IPeripheralTile {
    private EntityDetectorPeripheral edPeriph;

    // used to track changes so we can fire events when we get a new entity or an old one leaves the range
    Map<UUID, Entity> cachedEntities;


    public EntityDetectorTile(BlockEntityType<EntityDetectorTile> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        edPeriph = new EntityDetectorPeripheral(this);
        cachedEntities = null;
    }

    public EntityDetectorTile(BlockPos pos, BlockState state) {
        this(DuckyPeriph.ENTITY_DETECTOR_TILE, pos, state);
    }



    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull Direction side){
        if(edPeriph == null)
            edPeriph = new EntityDetectorPeripheral(this);
        return edPeriph;
    }

    public BlockPos getPos(){
        return this.pos;
    }

    public World getWorld(){
        return this.world;
    }

    
    // default to only getting living entities, but can get all entities if you wanted to?
    // well player maybe not, so if *we* developers wanted to, it's there for us. 
    public List<Entity> nearbyEntities(Box box){
        return nearbyEntities(box, true);
    }

    public List<Entity> nearbyEntities(Box box, Boolean livingOnly){
        return this.world.getOtherEntities(null, box, (entity) -> {
            // either get only living entities or get all entities
            return livingOnly ? entity instanceof LivingEntity : true;
        });
    }

    public Box makeBox(int range, int yRange){
        // will need to test this a good bit. 
        // Specifically at worldheight because *I* didn't add any protections and idk if mc has any built in
        return new Box(this.pos).expand((int)range, (int)yRange, (int)range);
    }

    public static void tick(World world, BlockPos pos, BlockState state, EntityDetectorTile ed_tile){
        if(ed_tile == null)
            return;
        // check entities and see what's changed since last time
        // maybe don't do *every* tick just to keep computing cost down ?
        ed_tile.checkEntityChange();
    }
    
    // I figure converting list to map and then checking between them is O(n) versus (n^2) for checking between the lists on their own?
    // not sure if this is actually faster but maybe? might be better for like a *lot* of entities.
    private Map<UUID, Entity> entListToMap(List<Entity> entList){
        Map<UUID, Entity> entMap = new HashMap<UUID, Entity>();
        for(Entity entity : entList){
            entMap.put(entity.getUuid(), entity);
        }
        return entMap;
    }

    public void checkEntityChange(){
        // check if there's been a change in entities
        // if so, fire an event
        List<Entity> new_ents_list = nearbyEntities(makeBox(EntityDetectorPeripheral.DEFAULT_RANGE, EntityDetectorPeripheral.DEFAULT_Y_RANGE));
        Map<UUID, Entity> new_ents = entListToMap(new_ents_list);
        if(cachedEntities == null){
            cachedEntities = new_ents;
            return;
        }
        // check for new entities first
        for(UUID uuid : new_ents.keySet()){
            if(!cachedEntities.containsKey(uuid)){
                // new entity
                edPeriph.newEntityEvent(new_ents.get(uuid));
            }
        }
        // then check for removed entities
        for(UUID uuid : cachedEntities.keySet()){
            if(!new_ents.containsKey(uuid)){
                // removed entity
                edPeriph.removedEntityEvent(cachedEntities.get(uuid));
            }
        }
        // update cached entities
        cachedEntities = new_ents;
    }
}
