package com.samsthenerd.duckyperiphs.forge.hexcasting;

import java.util.function.BooleanSupplier;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.samsthenerd.duckyperiphs.ducks.DuckItem;
import com.samsthenerd.duckyperiphs.hexcasting.FocalPortWrapperEntity;

import at.petrak.hexcasting.api.spell.iota.PatternIota;
import at.petrak.hexcasting.api.spell.math.HexDir;
import at.petrak.hexcasting.api.spell.math.HexPattern;
import at.petrak.hexcasting.forge.cap.ForgeCapabilityHandler;
import at.petrak.hexcasting.forge.cap.HexCapabilities;
import at.petrak.hexcasting.forge.cap.adimpl.CapStaticIotaHolder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public class DuckyCastingCaps {
    public static void attachEntityCaps(AttachCapabilitiesEvent<Entity> evt) {
        Entity entity = evt.getObject();
        if (entity instanceof FocalPortWrapperEntity fpEnt) {
            evt.addCapability(ForgeCapabilityHandler.IOTA_STORAGE_CAP, provide(fpEnt, HexCapabilities.IOTA, 
                () -> new EntityWrapperIotaCap<FocalPortWrapperEntity>(fpEnt)));
        }
    }

    public static void attachItemCaps(AttachCapabilitiesEvent<ItemStack> evt){
        ItemStack itemStack = evt.getObject();
        if(itemStack.getItem() instanceof DuckItem duckItem){
            evt.addCapability(ForgeCapabilityHandler.IOTA_STATIC_CAP, provide(itemStack, HexCapabilities.IOTA, 
                () -> new CapStaticIotaHolder((s) -> new PatternIota(HexPattern.fromAngles("aqadweeeede", HexDir.NORTH_EAST)), itemStack)
            ));
        }
    }

    //zoinked all these from hex, barely know what they do
    private static <CAP> SimpleProvider<CAP> provide(Entity entity, Capability<CAP> capability,
        NonNullSupplier<CAP> supplier) {
        return provide(entity::isRemoved, capability, supplier);
    }

    private static <CAP> SimpleProvider<CAP> provide(BlockEntity be, Capability<CAP> capability,
        NonNullSupplier<CAP> supplier) {
        return provide(be::isRemoved, capability, supplier);
    }

    private static <CAP> SimpleProvider<CAP> provide(ItemStack stack, Capability<CAP> capability,
        NonNullSupplier<CAP> supplier) {
        return provide(stack::isEmpty, capability, supplier);
    }

    private static <CAP> SimpleProvider<CAP> provide(BooleanSupplier invalidated, Capability<CAP> capability,
        NonNullSupplier<CAP> supplier) {
        return new SimpleProvider<>(invalidated, capability, LazyOptional.of(supplier));
    }


    private record SimpleProvider<CAP>(BooleanSupplier invalidated,
                                       Capability<CAP> capability,
                                       LazyOptional<CAP> instance) implements ICapabilityProvider {

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            if (invalidated.getAsBoolean()) {
                return LazyOptional.empty();
            }

            return cap == capability ? instance.cast() : LazyOptional.empty();
        }
    }
}
