package com.samsthenerd.duckyperiphs.hexcasting;

import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.samsthenerd.duckyperiphs.DuckyPeriph;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.NullIota;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.block.BlockState;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class FocalPortBlockEntity extends TileGeneric implements IPeripheralTile, ADIotaHolder{
    private FocalPortPeripheral fpPeriph;
    private Iota innerIota;
    private NbtCompound innerIotaTag; // just so that we can read stuff in before we get a proper serverworld.
    private FocalPortWrapperEntity wrapperEntity;
    private UUID wrapperEntityUUID;

    public FocalPortBlockEntity(BlockPos pos, BlockState state) {
        super(DuckyCasting.FOCAL_PORT_BLOCK_ENTITY, pos, state);
        DuckyPeriph.LOGGER.info("FocalPortBlockEntity created at " + pos.toString());
        fpPeriph = new FocalPortPeripheral(this);
    }

    // not sure if this is quite right
    public void spawnWrapperEntity(BlockPos pos){
        // check that we have a server world and that we don't already have a wrapper entity
        if(world instanceof ServerWorld == false){
            return;
        }
        if(wrapperEntityUUID != null){
            wrapperEntity = (FocalPortWrapperEntity)(((ServerWorld)world).getEntity(wrapperEntityUUID));
        }
        if(wrapperEntity != null){
            return;
        }
        wrapperEntity = new FocalPortWrapperEntity(DuckyCasting.FOCAL_PORT_WRAPPER_ENTITY, world);
        wrapperEntity = DuckyCasting.FOCAL_PORT_WRAPPER_ENTITY.spawn((ServerWorld)world, null, null, null, pos.subtract(new Vec3i(0, 1, 0)), SpawnReason.TRIGGERED, true, false);
        wrapperEntityUUID = wrapperEntity.getUuid();
        this.markDirty();
    }

    public void despawnWrapperEntity(){
        if(wrapperEntityUUID != null && world instanceof ServerWorld){
            wrapperEntity = (FocalPortWrapperEntity)(((ServerWorld)world).getEntity(wrapperEntityUUID));
        }
        if(wrapperEntity != null){
            wrapperEntity.discard();
            wrapperEntity = null;
        }
    }

    public void resetWrapperEntity(){
        if(wrapperEntity != null){
            BlockPos goalPos = getPos().subtract(new Vec3i(0, 1, 0));
            wrapperEntity.setPosition(goalPos.getX()+0.5, goalPos.getY(), goalPos.getZ()+0.5);
            DuckyPeriph.LOGGER.info("FocalPortBlockEntity: resetWrapperEntity: " + wrapperEntity.getPos().toString());
        }
    }

    @Override
    public void destroy(){
        despawnWrapperEntity();
        super.destroy();
    }

    @Nullable
    @Override
    public IPeripheral getPeripheral(@NotNull Direction side){
        if(fpPeriph == null)
            fpPeriph = new FocalPortPeripheral(this);
        return fpPeriph;
    }


    public BlockPos getPos(){
        return this.pos;
    }

    public World getWorld(){
        return this.world;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if(nbt.contains("wrapperEntityUUID")){
            wrapperEntityUUID = nbt.getUuid("wrapperEntityUUID");
        }
        if (nbt != null && nbt.contains("innerIota", 10)) {
            this.innerIotaTag = nbt.getCompound("innerIota");
            if(getWorld() instanceof ServerWorld){
                this.innerIota = HexIotaTypes.deserialize(innerIotaTag,(ServerWorld)getWorld());
            }
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if(wrapperEntityUUID != null){
            nbt.putUuid("wrapperEntityUUID", wrapperEntityUUID);
        }
        if (this.innerIota != null) {
            nbt.put("innerIota", HexIotaTypes.serialize(innerIota));
        } else if(this.innerIotaTag != null){
            nbt.put("innerIota", innerIotaTag);
        }
    }

    @Override
    public NbtCompound readIotaTag() {
        if(this.innerIota != null){
            return HexIotaTypes.serialize(innerIota);
        } else if(this.innerIotaTag != null){
            return this.innerIotaTag;
        } else {
            return HexIotaTypes.serialize(new NullIota());
        }
    }

    // fromHex is true if we're writing from hex casting, false if we're writing from CC
    public boolean writeIota(@Nullable Iota iota, boolean simulate, boolean fromHex){
        boolean result = writeIota(iota, simulate);
        if(!simulate && result){
            if(fromHex){
                fpPeriph.updateIota();
            }
        }
        return result;
    }

    @Override
    public boolean writeIota(@Nullable Iota iota, boolean simulate){
        if(iota == null){
            return false;
        }
        if(!simulate){
            this.innerIota = iota;
            this.markDirty();
            
        }
        return true;
    }

    

    public Iota getIota(){
        if(this.innerIota != null){
            return innerIota;
        } else if(innerIotaTag != null && getWorld() instanceof ServerWorld){
            this.innerIota = HexIotaTypes.deserialize(innerIotaTag,(ServerWorld)getWorld());
            return innerIota;
        } else {
            return new NullIota();
        }
    }

}
