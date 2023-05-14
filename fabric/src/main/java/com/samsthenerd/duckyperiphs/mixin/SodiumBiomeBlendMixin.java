package com.samsthenerd.duckyperiphs.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.samsthenerd.duckyperiphs.ducks.DuckBlock;
import com.samsthenerd.duckyperiphs.hexcasting.FocalPortBlock;
import com.samsthenerd.duckyperiphs.peripherals.keyboards.KeyboardBlock;

import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.model.quad.blender.ColorSampler;
import me.jellysquid.mods.sodium.client.model.quad.blender.LinearColorBlender;
import me.jellysquid.mods.sodium.client.util.color.ColorARGB;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;

@Pseudo
@Mixin(LinearColorBlender.class)
public abstract class SodiumBiomeBlendMixin {
    @Shadow(remap = false)
    protected abstract <T> int getBlockColor(BlockRenderView world, T state, ColorSampler<T> sampler, int x, int y, int z, int colorIdx);

    @Inject(method = "getVertexColor", at = @At("HEAD"), cancellable = true, remap = false)
    private <T> void onGetVertexColor(BlockRenderView world, BlockPos origin, ModelQuadView quad, ColorSampler<T> sampler, T state, int vertexIdx, CallbackInfoReturnable<Integer> info) {
        // The concept is from https://github.com/CaffeineMC/sodium-fabric/commit/634b11ed989482b9aab59ba45acc92ba31b97648
        // while the implementation is slightly based on #1331 by @devpelux: https://github.com/CaffeineMC/sodium-fabric/pull/1331
        if (state instanceof BlockState blockState) {
            Block block = blockState.getBlock();
            if (block instanceof DuckBlock || block instanceof FocalPortBlock || block instanceof KeyboardBlock) {
                int color = getBlockColor(world, state, sampler, origin.getX(), origin.getY(), origin.getZ(), quad.getColorIndex());
                info.setReturnValue(ColorARGB.toABGR(color));
            }
        }
    }
}
