package com.samsthenerd.duckyperiphs.mixin.HexMixins;

import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import at.petrak.hexcasting.common.blocks.entity.BlockEntityStoredPlayerImpetus;

@Mixin(BlockEntityStoredPlayerImpetus.class)
public interface ClericImpetusAccessor {
    @Accessor("storedPlayer")
    public UUID getStoredPlayer();
}
