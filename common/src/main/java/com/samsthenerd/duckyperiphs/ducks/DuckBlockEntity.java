package com.samsthenerd.duckyperiphs.ducks;

import org.jetbrains.annotations.Nullable;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.BlockPos;

public class DuckBlockEntity extends BlockEntity implements Nameable{
    private Text customName;
    private int repairCost;
    // values for our color stuff, not *really* sure how this all works but oh well, mostly copying from DyeableItem
    private int blockColor = 0;

    public DuckBlockEntity(BlockPos pos, BlockState state) {
        super(DuckyPeriphs.DUCK_BLOCK_ENTITY.get(), pos, state);
    }

    public DuckBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    
    public void setCustomName(Text customName) {
        this.customName = customName;
    }

    @Override
    public Text getName() {
        if (this.customName != null) {
            return this.customName;
        }
        return Text.translatable("block.minecraft.duck"); // i have no idea if this is right ??

    }

    @Override
    public Text getDisplayName() {
        return this.getName();
    }

    @Override
    @Nullable
    public Text getCustomName() {
        return this.customName;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt != null && nbt.contains("CustomName", 8)) {
            this.customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
        }
        if (nbt != null && nbt.contains("color", 99)) {
            this.blockColor = nbt.getInt("color");
        }
        if (nbt != null && nbt.contains("RepairCost", 99)) {
            this.repairCost = nbt.getInt("RepairCost");
        } else {
            this.repairCost = 1000000;
        }

    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.customName != null) {
            nbt.putString("CustomName", Text.Serializer.toJson(this.customName));
        }
        if (this.repairCost != 1000000){ // arbitrary number
            nbt.putInt("RepairCost", this.repairCost);
        }
        nbt.putInt("color", this.blockColor);
    }

    public int getBlockColor() {
        return blockColor;
    }

    public void setColor(int color) {
        this.blockColor = color;
    }

    public void setRepairCost(int cost){
        this.repairCost = cost;
    }


    // blah blah blah server stuff goes brrr
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
