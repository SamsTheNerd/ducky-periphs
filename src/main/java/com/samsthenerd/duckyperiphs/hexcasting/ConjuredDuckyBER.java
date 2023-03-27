package com.samsthenerd.duckyperiphs.hexcasting;

import com.samsthenerd.duckyperiphs.utils.RenderUtils;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper.Argb;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;

@Environment(EnvType.CLIENT)
public class ConjuredDuckyBER implements BlockEntityRenderer<ConjuredDuckyBlockEntity>{
    private static final Random RANDOM = Random.create();

    public ConjuredDuckyBER(BlockEntityRendererFactory.Context ctx) {}
 
    @Override
    public void render(ConjuredDuckyBlockEntity blockEntity, float tickDelta, MatrixStack matrices, 
        VertexConsumerProvider vertexConsumers, int light, int overlay) {
            matrices.push();

            // want to render the block ourselves so we can render it to its own layer and mess with it
            MinecraftClient mcClient = MinecraftClient.getInstance();
            BlockRenderManager brm = mcClient.getBlockRenderManager();

            Pair<Integer, Framebuffer> frameBuffersObj = RenderUtils.setupNewFrameBuffer();

            BlockState duckyVisibleState = DuckyCasting.CONJURED_DUCKY_BLOCK.getDefaultState().with(ConjuredDuckyBlock.VISIBLE, true);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();

            // settings stolen from RenderLayer.getTranslucent()
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);

            brm.renderBlock(duckyVisibleState, blockEntity.getPos(), (BlockRenderView) blockEntity.getWorld(),
                matrices, bufferBuilder, false, RANDOM);
                // matrices, vertexConsumers.getBuffer(RenderLayer.getTranslucent()), false, RANDOM);
            // brm.renderBlockAsEntity(duckyVisibleState, matrices, vertexConsumers, light, overlay);

            BlockPos camPos = mcClient.getCameraEntity().getBlockPos();
            RenderLayer.getTranslucent().draw(bufferBuilder, camPos.getX(), camPos.getY(), camPos.getZ());

            BlockPos pos = blockEntity.getPos();
            int color = blockEntity.getColorizer().getColor(blockEntity.getWorld().getTime(), new Vec3d(pos.getX() + RANDOM.nextFloat(), 
                pos.getY() + RANDOM.nextFloat(), pos.getZ() + RANDOM.nextFloat()).multiply(
                RANDOM.nextFloat() * 3));
            color = Argb.getArgb(32, 255, Argb.getGreen(color), Argb.getBlue(color));

            MatrixStack cleanMatrices = new MatrixStack();


            // bufferBuilder = tessellator.getBuffer();
            // bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

            // int w = mcClient.getWindow().getScaledWidth();
            // int h = mcClient.getWindow().getScaledHeight();

            // bufferBuilder.vertex(w*0.5, h*0.5, 0).color(255, 255, 255, 255).next();
            // bufferBuilder.vertex(w*0.7, h*0.7, 0).color(255, 255, 255, 255).next();
            // bufferBuilder.vertex(w*0.7, h*0.5, 0).color(255, 255, 255, 255).next();
            // bufferBuilder.vertex(w*0.6, h*0.3, 0).color(255, 255, 255, 255).next();



            RenderUtils.drawFrameBuffer(cleanMatrices, color, frameBuffersObj.getRight(), frameBuffersObj.getLeft());
            // GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffersObj.getLeft());

            matrices.pop();
    }
}
