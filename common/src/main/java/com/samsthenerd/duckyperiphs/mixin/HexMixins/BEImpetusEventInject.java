package com.samsthenerd.duckyperiphs.mixin.HexMixins;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.samsthenerd.duckyperiphs.hexcasting.lonelyPeripherals.ImpetusPeripheral;
import com.samsthenerd.duckyperiphs.peripherals.IPeripheralTileDucky;

import at.petrak.hexcasting.api.casting.circles.BlockEntityAbstractImpetus;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.util.math.Direction;

@Pseudo
@Mixin(BlockEntityAbstractImpetus.class)
public class BEImpetusEventInject implements IPeripheralTileDucky {
    ImpetusPeripheral impPeriph;

    @Inject(method = "startExecution(Lnet/minecraft/server/network/ServerPlayerEntity;)V", 
    at = @At("TAIL"))
    private void injectCircleActivate(CallbackInfo ci) {
        if(impPeriph == null)
            impPeriph = new ImpetusPeripheral((BlockEntityAbstractImpetus)(Object)this);
        impPeriph.impetusStarted();
    }

    @Inject(method = "endExecution()V", at = @At("HEAD"), remap = false)
    private void injectCircleFinish(CallbackInfo ci) {
        if(impPeriph == null)
            impPeriph = new ImpetusPeripheral((BlockEntityAbstractImpetus)(Object)this);
        impPeriph.impetusEnded();
    }

    @Nullable
    @Override
    public IPeripheral getPeripheral(@Nonnull Direction side){
        if(impPeriph == null)
            impPeriph = new ImpetusPeripheral((BlockEntityAbstractImpetus)(Object)this);
        return impPeriph;
    }
}
