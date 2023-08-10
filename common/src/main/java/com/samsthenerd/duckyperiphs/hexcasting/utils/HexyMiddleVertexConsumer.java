package com.samsthenerd.duckyperiphs.hexcasting.utils;

import com.samsthenerd.duckyperiphs.hexcasting.ConjuredDuckyBER;
import com.samsthenerd.duckyperiphs.utils.MiddleVertexConsumer;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.ColorHelper.Argb;
import net.minecraft.util.math.Vec3d;

public class HexyMiddleVertexConsumer extends MiddleVertexConsumer {
    private FrozenPigment colorizer;
    private float time; // i mean, this shouldn't persist across frames?
    
    public HexyMiddleVertexConsumer(VertexConsumer innerConsumer, FrozenPigment colorizer, float time) {
        super(innerConsumer);
        this.colorizer = colorizer;
        this.time = time;
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        // do something with the color
        int color = colorizer.getColorProvider().getColor(time, new Vec3d(lastVecWritten.getX() + ConjuredDuckyBER.RANDOM.nextFloat(), 
        lastVecWritten.getY() + ConjuredDuckyBER.RANDOM.nextFloat(), lastVecWritten.getZ() + ConjuredDuckyBER.RANDOM.nextFloat()).multiply(
        ConjuredDuckyBER.RANDOM.nextFloat() * 3));
        color = Argb.getArgb((int)(Argb.getAlpha(color)*0.6), Argb.getRed(color), Argb.getGreen(color), Argb.getBlue(color));
        innerConsumer.color(color);
        return this;
    }
}
