package com.samsthenerd.duckyperiphs.utils;

import net.minecraft.client.render.FixedColorVertexConsumer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.util.math.Vec3d;

// a wrapper class to modify data being passed to some vertex consumer before it's actually written
// override the methods you want to modify - note: might be a better way to do this with interfaces but this seems neat enough
public class MiddleVertexConsumer extends FixedColorVertexConsumer {
    public VertexConsumer innerConsumer;
    // tells us info about where we probably are. should maybe expand to save more info if we use this class more.
    protected Vec3d lastVecWritten = new Vec3d(0,0,0);

    // meant to be passed something like a block buffer
    public MiddleVertexConsumer(VertexConsumer innerConsumer) {
        this.innerConsumer = innerConsumer;
    }

    public VertexConsumer vertex(double x, double y, double z) {
        lastVecWritten = new Vec3d(x,y,z);
        this.innerConsumer.vertex(x, y, z);
        return this;
    }

    public VertexConsumer color(int red, int green, int blue, int alpha) {
        this.innerConsumer.color(red, green, blue, alpha);
        return this;
    }

    public VertexConsumer texture(float u, float v) {
        this.innerConsumer.texture(u, v);
        return this;
    }

    public VertexConsumer overlay(int u, int v) {
        this.innerConsumer.overlay(u, v);
        return this;
    }

    public VertexConsumer light(int u, int v) {
        this.innerConsumer.light(u, v);
        return this;
    }

    public VertexConsumer normal(float x, float y, float z) {
        this.innerConsumer.normal(x, y, z);
        return this;
    }

    public void next() {
        this.innerConsumer.next();
    }

}
