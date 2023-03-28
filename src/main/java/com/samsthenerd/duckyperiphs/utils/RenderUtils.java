package com.samsthenerd.duckyperiphs.utils;

import org.lwjgl.opengl.GL30;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;

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
        // RenderSystem.setShaderColor(Argb.getRed(argb)/255f, Argb.getGreen(argb)/255f, Argb.getBlue(argb)/255f, Argb.getAlpha(argb)/255f);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        // RenderSystem.defaultBlendFunc();
        // RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        // RenderSystem.disableCull();

        RenderSystem.setShaderTexture(0, readFromBuffer.getColorAttachment());

        // RenderSystem.setShaderTexture(0, DefaultSkinHelper.getTexture());

        Window window = MinecraftClient.getInstance().getWindow();
        DrawableHelper.drawTexture(matrices, 0, 0,
            window.getScaledWidth(), window.getScaledHeight(),
            0, readFromBuffer.textureHeight,
            readFromBuffer.textureWidth, -readFromBuffer.textureHeight,
            readFromBuffer.textureWidth, readFromBuffer.textureHeight);
        RenderSystem.disableBlend();
        RenderSystem.enableCull();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1f,1f,1f,1f);
    }

    public static void restoreFrameBuffer(int writeToBufferId){
        GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, writeToBufferId);
    }


    public static void drawFrameBufferToBufferTranslucent(int argb, Framebuffer readFromBuffer, BufferBuilder writeToBuffer){
        RenderSystem.setShader(GameRenderer::getRenderTypeTranslucentShader);
        Window window = MinecraftClient.getInstance().getWindow();
        int rfBufWidth = readFromBuffer.textureWidth;
        int rfBufHeight = readFromBuffer.textureHeight;
        RenderSystem.setShaderTexture(0, readFromBuffer.getColorAttachment());
        writeToBuffer.vertex(0, window.getHeight(), 0).color(argb).texture(0, rfBufHeight).light(255).normal(0,0,1).next();
        writeToBuffer.vertex(window.getWidth(), window.getHeight(), 0).color(argb).texture(rfBufWidth, rfBufHeight).light(255).normal(0,0,1).next();
        writeToBuffer.vertex(window.getWidth(), 0, 0).color(argb).texture(rfBufWidth, 0).light(255).normal(0,0,1).next();
        writeToBuffer.vertex(0, 0, 0).color(argb).texture(0,0).light(255).normal(0,0,1).next();
    }
}