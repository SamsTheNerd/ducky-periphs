package net.fabricmc.duckyperiphs.utils;

import net.fabricmc.duckyperiphs.DuckyPeriph;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/*
 * This class is used for an entity where the entity is only active/alive when the block entity is loaded.
 */
public class EntityFromBlockEntity extends Entity{
    protected BlockEntity parentBlockEntity; // it should stay up to date but check before using it - can be null
    private Class<? extends BlockEntity> parentBlockEntityType; // so we can check that the block entity is the right type

    public EntityFromBlockEntity(EntityType<? extends EntityFromBlockEntity> entityType, World world, Class<? extends BlockEntity> pbeType) {
        super(entityType, world);
        this.parentBlockEntity = world.getBlockEntity(getBlockPos());
        this.parentBlockEntityType = pbeType;
        this.noClip = true;
    }

    public EntityFromBlockEntity(EntityType<? extends EntityFromBlockEntity> entityType, World world) {
        this(entityType, world, BlockEntity.class);
    }


    @Override
    public boolean shouldRender(double distance) {
        return false;
    }


    // just does checks to make sure we can actually access it - also refreshes it based on blockpos
    public boolean hasBlockEntity(){
        if(parentBlockEntity == null){
            BlockPos pos = getBlockPos();
            DuckyPeriph.LOGGER.info("hasBlockEntity: parentBlockEntity was null, getting from pos" + pos.toString());
            parentBlockEntity = world.getBlockEntity(pos);
        }
        DuckyPeriph.LOGGER.info("hasBlockEntity: parentBlockEntity is type " + parentBlockEntity.getClass().getName());
        return parentBlockEntityType.isInstance(parentBlockEntity);
    }



    @Override
    public void writeCustomDataToNbt(NbtCompound nbt){
        // nbt.put("parentBlockEntityPos", NbtHelper.fromBlockPos(getBlockPos()));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt){
        // parentBlockEntityPos = NbtHelper.toBlockPos(nbt.getCompound("parentBlockEntityPos"));
    }

    // don't think we need anything here? maybe could track BlockEntity or Pos, might be more efficient than checking for the entity every tick ?
    @Override
    protected void initDataTracker() {
    }

    // deal with safely despawning -- i think we don't want this actually. Game should deal with loading/unloading entities for us
    // @Override
    // public void tick(){
    //     World world = getWorld();
    //     if(world==null){
    //         return;
    //     }
    //     if (parentBlockEntity == null){
    //         parentBlockEntity = world.getBlockEntity(getBlockPos());
    //     }
    //     if (parentBlockEntity == null){
    //         this.remove(RemovalReason.DISCARDED);
    //         if(world.isClient()){
    //             DuckyPeriph.LOGGER.info("despawn entityFromBlockEntity on client");
    //         } else {
    //             DuckyPeriph.LOGGER.info("despawn entityFromBlockEntity on server");
    //         }
    //         return;
    //     }
    // }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}
