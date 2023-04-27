package com.samsthenerd.duckyperiphs.hexcasting;

import javax.annotation.Nullable;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class ConjuredDuckyBlockEntity extends BlockEntity{
    private FrozenColorizer colorizer = FrozenColorizer.DEFAULT.get();
    public static final String TAG_COLORIZER = "tag_colorizer";

    public ConjuredDuckyBlockEntity(BlockPos pos, BlockState state) {
        super(DuckyCasting.CONJURED_DUCKY_BLOCK_ENTITY, pos, state);
    }

    public void setColorizer(FrozenColorizer colorizer){
        this.colorizer = colorizer;
        if(getWorld() instanceof ServerWorld){
            DuckyPeriphs.LOGGER.info("set colorizer on server");
        } else {
            DuckyPeriphs.LOGGER.info("set colorizer on client");
        }
        markDirty();
    }

    public FrozenColorizer getColorizer(){
        return colorizer;
    }

    @Override
    public void readNbt(NbtCompound nbt){
        super.readNbt(nbt);
        if(nbt.contains(TAG_COLORIZER)){
            colorizer = FrozenColorizer.fromNBT(nbt.getCompound(TAG_COLORIZER));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt){
        super.writeNbt(nbt);
        nbt.put(TAG_COLORIZER, colorizer.serializeToNBT());
    }


    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
