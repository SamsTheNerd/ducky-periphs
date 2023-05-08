package com.samsthenerd.duckyperiphs.peripherals.sculkophone;


import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import net.minecraft.world.event.listener.VibrationListener;

public class SculkophoneBlockEntity
extends BlockEntity
implements VibrationListener.Callback, IPeripheralTile{
    private static final Logger LOGGER = LogUtils.getLogger();
    private VibrationListener listener;
    private int lastVibrationFrequency;
    private SculkophonePeripheral sculkPeriph;

    public SculkophoneBlockEntity(BlockPos pos, BlockState state) {
        super(DuckyPeriphs.SCULKOPHONE_BLOCK_ENTITY, pos, state);
        this.listener = new VibrationListener(new BlockPositionSource(this.pos), ((SculkophoneBlock)state.getBlock()).getRange(), this, null, 0.0f, 0);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.lastVibrationFrequency = nbt.getInt("last_vibration_frequency");
        if (nbt.contains("listener", NbtElement.COMPOUND_TYPE)) {
            new Dynamic<>(NbtOps.INSTANCE, nbt.getCompound("listener"));
            VibrationListener.createCodec(this).parse(new Dynamic<>(NbtOps.INSTANCE, nbt.getCompound("listener"))).resultOrPartial(LOGGER::error).ifPresent(listener -> {
                this.listener = listener;
            });
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("last_vibration_frequency", this.lastVibrationFrequency);
        VibrationListener.createCodec(this).encodeStart(NbtOps.INSTANCE, this.listener).resultOrPartial(LOGGER::error).ifPresent(listenerNbt -> nbt.put("listener", (NbtElement)listenerNbt));
    }

    public VibrationListener getEventListener() {
        return this.listener;
    }

    public int getLastVibrationFrequency() {
        return this.lastVibrationFrequency;
    }

    @Override
    public boolean triggersAvoidCriterion() {
        return true;
    }

    @Override
    public boolean accepts(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable GameEvent.Emitter emitter) {
        if (this.isRemoved() || pos.equals(this.getPos()) && (event == GameEvent.BLOCK_DESTROY || event == GameEvent.BLOCK_PLACE)) {
            return false;
        }
        return SculkophoneBlock.isInactive(this.getCachedState());
    }

    @Override
    public void accept(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable Entity entity, @Nullable Entity sourceEntity, float distance) {
        BlockState blockState = this.getCachedState();
        if (SculkophoneBlock.isInactive(blockState)) {
            // this is where we'd fire the event
            // Math.sqrt( pos.getSquaredDistance(sourceEntity.getPos()))
            if(sculkPeriph != null){
                sculkPeriph.vibrationEvent(event, distance);
            }
            SculkophoneBlock.setActive(entity, world, this.pos, blockState);
        }
    }

    @Override
    public void onListen() {
        this.markDirty();
    }

    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull Direction side){
        if(sculkPeriph == null)
            sculkPeriph = new SculkophonePeripheral(this);
        return sculkPeriph;
    }

    public void makeVibration(GameEvent event){
        
    }
}

