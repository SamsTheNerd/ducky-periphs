package net.fabricmc.duckyperiphs.hexcasting;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import at.petrak.hexcasting.api.addldata.ADIotaHolder;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import dan200.computercraft.shared.common.TileGeneric;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class FocalPortBlockEntity extends TileGeneric implements IPeripheralTile, ADIotaHolder{
    private FocalPortPeripheral fpPeriph;
    private Iota innerIota;

    public FocalPortBlockEntity(BlockPos pos, BlockState state) {
        super(DuckyCasting.FOCAL_PORT_BLOCK_ENTITY, pos, state);
        fpPeriph = new FocalPortPeripheral(this);
    }

    @Nullable
    @Override
    public IPeripheral getPeripheral(@NotNull Direction side){
        if(fpPeriph == null)
            fpPeriph = new FocalPortPeripheral(this);
        return fpPeriph;
    }


    // get pos and get world so we can get the weather from our WeatherMachinePeripheral
    public BlockPos getPos(){
        return this.pos;
    }

    public World getWorld(){
        return this.world;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt != null && nbt.contains("innerIota", 10) && !getWorld().isClient()) {
            this.innerIota = HexIotaTypes.deserialize(nbt.getCompound("innerIota"),(ServerWorld)getWorld());
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.innerIota != null) {
            nbt.put("innerIota", HexIotaTypes.serialize(innerIota));
        }
    }

    @Override
    public NbtCompound readIotaTag() {
        return this.innerIota != null ? HexIotaTypes.serialize(innerIota) : null;
    }

    @Override
    public boolean writeIota(@Nullable Iota iota, boolean simulate){
        if(iota == null){
            return false;
        }
        if(!simulate){
            this.innerIota = iota;
        }
        return true;
    }

    public Iota getIota(){
        return this.innerIota;
    }

}
