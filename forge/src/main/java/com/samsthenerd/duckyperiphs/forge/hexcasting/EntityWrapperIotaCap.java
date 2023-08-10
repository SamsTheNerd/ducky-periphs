package com.samsthenerd.duckyperiphs.forge.hexcasting;

import javax.annotation.Nullable;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.forge.cap.adimpl.CapEntityIotaHolder;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;

public class EntityWrapperIotaCap<ET extends Entity & ADIotaHolder> extends CapEntityIotaHolder {
    ET innerEntity;
    public EntityWrapperIotaCap(ET _ent){
        innerEntity = _ent;
    }

    // perhaps these should pass through to the inner entity, prob fine?
    public void writeToNbt(NbtCompound tag){
        tag.putBoolean("hasHexCC", true);
    }

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
