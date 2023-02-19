package net.fabricmc.duckyperiphs.hexcasting;

import javax.annotation.Nullable;

import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import at.petrak.hexcasting.fabric.cc.adimpl.CCIotaHolder;
import net.fabricmc.duckyperiphs.utils.EntityFromBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

public class FocalPortWrapperEntity extends EntityFromBlockEntity implements CCIotaHolder{
    public FocalPortWrapperEntity(EntityType<? extends FocalPortWrapperEntity> entityType, World world) {
        super(entityType, world, FocalPortBlockEntity.class);
    }

    @Override
    public NbtCompound readIotaTag(){
        if(hasBlockEntity()){
            return ((FocalPortBlockEntity)parentBlockEntity).readIotaTag();
        }
        return null;
    }

    @Override
    public Iota readIota(ServerWorld world) {
        var tag = readIotaTag();
        if (tag != null) {
            return HexIotaTypes.deserialize(tag, world);
        }
        return null;
    }

    @Override
    public Iota emptyIota() {
        return null;
    }

    /**
     * @return if the writing succeeded/would succeed
     */
    @Override
    public boolean writeIota(@Nullable Iota iota, boolean simulate){
        if(hasBlockEntity()){
            return ((FocalPortBlockEntity)parentBlockEntity).writeIota(iota, simulate, true);
        }
        return false;
    }


    // this is for component, I don't think it really needs that ? - entity nbt is handled by super and iota nbt is handled by block entity ?
    @Override 
    public void writeToNbt(NbtCompound tag){
        tag.putBoolean("hasHexCC", true);
    }

    @Override
    public void readFromNbt(NbtCompound tag){
        tag.getBoolean("hasHexCC");
    }

}
