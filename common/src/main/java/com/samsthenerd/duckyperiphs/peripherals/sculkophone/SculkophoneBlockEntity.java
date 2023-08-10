package com.samsthenerd.duckyperiphs.peripherals.sculkophone;


import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.samsthenerd.duckyperiphs.DuckyPeriphs;
import com.samsthenerd.duckyperiphs.peripherals.IPeripheralTileDucky;

import dan200.computercraft.api.peripheral.IPeripheral;
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
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.Vibrations;
import net.minecraft.world.event.listener.GameEventListener;

public class SculkophoneBlockEntity
extends BlockEntity
implements GameEventListener.Holder<Vibrations.VibrationListener>, Vibrations, IPeripheralTileDucky{
    private static final Logger LOGGER = LogUtils.getLogger();
    private Vibrations.ListenerData listenerData;
    private final Vibrations.VibrationListener listener;
    private final Vibrations.Callback callback;
    private int lastVibrationFrequency;
    private SculkophonePeripheral sculkPeriph;

    public SculkophoneBlockEntity(BlockPos pos, BlockState state) {
        super(DuckyPeriphs.SCULKOPHONE_BLOCK_ENTITY.get(), pos, state);
        sculkPeriph = new SculkophonePeripheral(this);
        this.callback = new SPVibrationCallback(this, pos, sculkPeriph);
        this.listenerData = new Vibrations.ListenerData();
        this.listener = new Vibrations.VibrationListener(this);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.lastVibrationFrequency = nbt.getInt("last_vibration_frequency");
        if (nbt.contains("listener", NbtElement.COMPOUND_TYPE)) {
            new Dynamic<>(NbtOps.INSTANCE, nbt.getCompound("listener"));
            ListenerData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, nbt.getCompound("listener"))).resultOrPartial(LOGGER::error).ifPresent(listener -> {
                this.listenerData = listener;
            });
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("last_vibration_frequency", this.lastVibrationFrequency);
        ListenerData.CODEC.encodeStart(NbtOps.INSTANCE, this.listenerData).resultOrPartial(LOGGER::error).ifPresent(listenerNbt -> nbt.put("listener", (NbtElement)listenerNbt));
    }

    public VibrationListener getEventListener() {
        return this.listener;
    }

    public Vibrations.ListenerData getVibrationListenerData() {
        return this.listenerData;
    }

    public Vibrations.Callback getVibrationCallback() {
        return this.callback;
    }

    public int getLastVibrationFrequency() {
        return this.lastVibrationFrequency;
    }

    public static class SPVibrationCallback implements Vibrations.Callback{
        private final SculkophoneBlockEntity spbe;
        private final SculkophonePeripheral sculkPeriph;
        private final BlockPos spPos;
        PositionSource posSource;

        public SPVibrationCallback(SculkophoneBlockEntity spbe, BlockPos pos, SculkophonePeripheral sculkPeriph) {
            this.spbe = spbe;
            this.spPos = pos;
            this.sculkPeriph = sculkPeriph;
            posSource = new BlockPositionSource(pos);
        }   

        @Override
        public boolean triggersAvoidCriterion() {
            return true;
        }

        @Override
        public boolean accepts(ServerWorld world, BlockPos pos, GameEvent event, @Nullable GameEvent.Emitter emitter) {
            if (spbe.isRemoved() || pos.equals(spPos) && (event == GameEvent.BLOCK_DESTROY || event == GameEvent.BLOCK_PLACE)) {
                return false;
            }
            return SculkophoneBlock.isInactive(spbe.getCachedState());
        }

        @Override
        public void accept(ServerWorld world, BlockPos pos, GameEvent event, @Nullable Entity entity, @Nullable Entity sourceEntity, float distance) {
            BlockState blockState = spbe.getCachedState();
            if (SculkophoneBlock.isInactive(blockState)) {
                // this is where we'd fire the event
                // Math.sqrt( pos.getSquaredDistance(sourceEntity.getPos()))
                if(sculkPeriph != null){
                    sculkPeriph.vibrationEvent(event, distance);
                }
                SculkophoneBlock.setActive(entity, world, spPos, blockState);
            }
        }

        @Override
        public void onListen() {
            spbe.markDirty();
        }

        public int getRange(){
            return 8;
        }

        public PositionSource getPositionSource(){
            return posSource;
        }

        public boolean requiresTickingChunksAround() {
            return true;
         }
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

