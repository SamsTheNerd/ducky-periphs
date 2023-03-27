package com.samsthenerd.duckyperiphs.hexcasting;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class ConjuredDuckyBlockEntity extends BlockEntity{
    private FrozenColorizer colorizer = FrozenColorizer.DEFAULT.get();;
    public ConjuredDuckyBlockEntity(BlockPos pos, BlockState state) {
        super(DuckyCasting.CONJURED_DUCKY_BLOCK_ENTITY, pos, state);
    }

    public void setColorizer(FrozenColorizer colorizer){
        this.colorizer = colorizer;
    }

    public FrozenColorizer getColorizer(){
        return colorizer;
    }

}
