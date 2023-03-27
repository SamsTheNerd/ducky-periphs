package com.samsthenerd.duckyperiphs.utils;

import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.ColorHelper.Argb;

public class RenderUtils{
    // public static int initialFrameBuffer;
    // public static Framebuffer transFrameBuffer; 

    // returns <oldFrameBufferId, newFrameBuffer>
    public static Pair<Integer,Framebuffer> setupNewFrameBuffer(){
        Window window = MinecraftClient.getInstance().getWindow();
        int initialFrameBuffer = GlStateManager.getBoundFramebuffer();
        Framebuffer transFrameBuffer = new SimpleFramebuffer(window.getFramebufferWidth(), window.getFramebufferHeight(), false, MinecraftClient.IS_SYSTEM_MAC);
        transFrameBuffer.setClearColor(0, 0, 0, 0);
        transFrameBuffer.beginWrite(false);
        return new Pair<Integer, Framebuffer>(initialFrameBuffer, transFrameBuffer);
    }

    public static void drawFrameBuffer(MatrixStack matrices, int argb, Framebuffer readFromBuffer, int writeToBufferId){
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, writeToBufferId);
        RenderSystem.setShaderColor(Argb.getRed(argb)/255f, Argb.getGreen(argb)/255f, Argb.getBlue(argb)/255f, Argb.getAlpha(argb)/255f);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShaderTexture(0, readFromBuffer.getColorAttachment());

        Window window = MinecraftClient.getInstance().getWindow();
        DrawableHelper.drawTexture(matrices, 0, 0,
            window.getScaledWidth(), window.getScaledHeight(),
            0, readFromBuffer.textureHeight,
            readFromBuffer.textureWidth, -readFromBuffer.textureHeight,
            readFromBuffer.textureWidth, readFromBuffer.textureHeight);
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.setShaderColor(1,1,1,1);
    }
}