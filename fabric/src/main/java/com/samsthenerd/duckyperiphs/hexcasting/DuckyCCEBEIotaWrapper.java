package com.samsthenerd.duckyperiphs.hexcasting;

import javax.annotation.Nullable;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.fabric.cc.adimpl.CCIotaHolder;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

// awful ! awful little class with a bad name. Just wraps an entity to be used as a cardinal component
public class DuckyCCEBEIotaWrapper<ET extends Entity & ADIotaHolder> implements CCIotaHolder {
    ET innerEntity;
    public DuckyCCEBEIotaWrapper(ET _ent){
        innerEntity = _ent;
    }

    // perhaps these should pass through to the inner entity, prob fine?
    @Override 
    public void writeToNbt(NbtCompound tag){
        tag.putBoolean("hasHexCC", true);
    }

    @Override
    public void readFromNbt(NbtCompound tag){
        tag.getBoolean("hasHexCC");
    }

    @Override
    public NbtCompound readIotaTag(){
        return innerEntity.readIotaTag();
    }

    @Override
    public Iota readIota(ServerWorld world) {
        return innerEntity.readIota(world);
    }

    @Override
    public Iota emptyIota() {
        return innerEntity.emptyIota();
    }

    /**
     * @return if the writing succeeded/would succeed
     */
    @Override
    public boolean writeIota(@Nullable Iota iota, boolean simulate){
        return innerEntity.writeIota(iota, simulate);
    }
}
