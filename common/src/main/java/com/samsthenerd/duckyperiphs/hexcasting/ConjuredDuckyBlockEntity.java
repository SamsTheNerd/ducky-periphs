package com.samsthenerd.duckyperiphs.hexcasting;

import javax.annotation.Nullable;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

public class ConjuredDuckyBlockEntity extends BlockEntity{
    private FrozenPigment colorizer = FrozenPigment.DEFAULT.get();
    public static final String TAG_COLORIZER = "tag_colorizer";

    public ConjuredDuckyBlockEntity(BlockPos pos, BlockState state) {
        super(DuckyCasting.CONJURED_DUCKY_BLOCK_ENTITY.get(), pos, state);
    }

    public void setColorizer(FrozenPigment colorizer){
        this.colorizer = colorizer;
        markDirty();
    }

    public FrozenPigment getColorizer(){
        return colorizer;
    }

    @Override
    public void readNbt(NbtCompound nbt){
        super.readNbt(nbt);
        if(nbt.contains(TAG_COLORIZER)){
            colorizer = FrozenPigment.fromNBT(nbt.getCompound(TAG_COLORIZER));
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
