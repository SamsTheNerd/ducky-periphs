package com.samsthenerd.duckyperiphs.hexcasting;


import com.samsthenerd.duckyperiphs.DuckyPeriph;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;

public class DuckyCastingClient {

    public static void init(){
        EntityRendererRegistry.register( DuckyCasting.FOCAL_PORT_WRAPPER_ENTITY, EmptyEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(DuckyCasting.FOCAL_PORT_BLOCK, RenderLayer.getTranslucent());

        setupColorProviders();
    }

    public static void setupColorProviders(){
        ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
            if(tintIndex != 0) {
				return 0xFFFFFF;
			}
            // int thisColor = FocalPortBlock.getColor(world, pos);
            int thisColor = 0;
            RenderAttachedBlockView view = (RenderAttachedBlockView) world;
            if(view != null){
                Object rawValue = view.getBlockEntityRenderAttachment(pos);
                if(rawValue != null){
                    thisColor = (int) rawValue;
                }
            }
            DuckyPeriph.LOGGER.info("Color: " + thisColor);
            return thisColor;
        }, DuckyCasting.FOCAL_PORT_BLOCK);
    }
}
