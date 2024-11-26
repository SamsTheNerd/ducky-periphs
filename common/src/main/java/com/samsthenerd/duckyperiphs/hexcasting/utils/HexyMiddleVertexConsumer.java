package com.samsthenerd.duckyperiphs.hexcasting.utils;

import com.samsthenerd.duckyperiphs.hexcasting.ConjuredDuckyBER;
import com.samsthenerd.duckyperiphs.utils.MiddleVertexConsumer;

import at.petrak.hexcasting.api.pigment.FrozenPigment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.ColorHelper.Argb;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

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
        int color = colorizer.getColorProvider().getColor(time*3, new Vec3d(lastVecWritten.getX(),
        lastVecWritten.getY(), lastVecWritten.getZ()).multiply(
        3));
        color = Argb.getArgb((int)(Argb.getAlpha(color)*0.6), Argb.getRed(color), Argb.getGreen(color), Argb.getBlue(color));
        innerConsumer.color(color);
        return this;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
//        double newX = (0.9 + 0.1 * Math.abs(Math.sin(time/2f))) * x;
//        double newY = (0.9 + 0.1 * Math.random()) * y;

        Random rand = Random.create((long)time * (new Vec3d(x,y,z).hashCode()));
        double randish = 0.1 * rand.nextDouble();
        double newX = randish*(0.1 * Math.sin(x*time/2f)) + x;
        double newY = randish*(0.1 * (Math.cos(y*time/2f))) + y;
        double newZ = randish*(0.1 * (Math.sin(z*time/1.5f))) + z;
        lastVecWritten = new Vec3d(newX,newY,newZ);
        this.innerConsumer.vertex(newX, newY, newZ);
        return this;
    }
}
