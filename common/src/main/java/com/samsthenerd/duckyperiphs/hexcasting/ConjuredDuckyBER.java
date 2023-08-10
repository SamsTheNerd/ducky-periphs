package com.samsthenerd.duckyperiphs.hexcasting;

import com.mojang.blaze3d.systems.VertexSorter;
import com.samsthenerd.duckyperiphs.hexcasting.utils.HexyMiddleVertexConsumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class ConjuredDuckyBER implements BlockEntityRenderer<ConjuredDuckyBlockEntity>{
    public static final Random RANDOM = Random.create();

    public ConjuredDuckyBER(BlockEntityRendererFactory.Context ctx) {}
 
    @Override
    public void render(ConjuredDuckyBlockEntity blockEntity, float tickDelta, MatrixStack matrices, 
        VertexConsumerProvider vertexConsumers, int light, int overlay) {
            matrices.push();

            // want to render the block ourselves so we can render it to its own layer and mess with it
            MinecraftClient mcClient = MinecraftClient.getInstance();
            BlockRenderManager brm = mcClient.getBlockRenderManager();

            BlockPos pos = blockEntity.getPos();
            World world = blockEntity.getWorld();
            BlockState state = world.getBlockState(pos);
            if(!(state.getBlock() instanceof ConjuredDuckyBlock)){
                matrices.pop();
                return;
            }
            state = state.with(ConjuredDuckyBlock.VISIBLE, true);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();

            // settings stolen from RenderLayer.getTranslucent()
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);

            HexyMiddleVertexConsumer hexyConsumer = new HexyMiddleVertexConsumer(bufferBuilder, blockEntity.getColorizer(), 
                blockEntity.getWorld().getTime());

            brm.renderBlock(state, blockEntity.getPos(), (BlockRenderView) blockEntity.getWorld(),
                matrices, hexyConsumer, true, RANDOM);


            BlockPos camPos = mcClient.getCameraEntity().getBlockPos();
            VertexSorter vSorter = VertexSorter.byDistance(camPos.getX(), camPos.getY(), camPos.getZ());
            RenderLayer.getTranslucent().draw((BufferBuilder) hexyConsumer.innerConsumer, vSorter);

            matrices.pop();
    }
}
