package com.samsthenerd.duckyperiphs.hexcasting;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

public class HexyRenderLayer extends RenderLayer {
    public HexyRenderLayer(String name, VertexFormat vertexFormat, VertexFormat.DrawMode drawMode, int expectedBufferSize, boolean hasCrumbling, boolean translucent, Runnable startAction, Runnable endAction) {
        super(name, vertexFormat, drawMode, expectedBufferSize, hasCrumbling, translucent, startAction, endAction);
    }

    public static final Shader SOLID_SHADER = new Shader(GameRenderer::getRenderTypeSolidShader);

    private static final RenderLayer HEXY_SOLID = RenderLayer.of("hexy_solid", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL, VertexFormat.DrawMode.QUADS, 0x200000, true, false,MultiPhaseParameters.builder().lightmap(ENABLE_LIGHTMAP).shader(SOLID_SHADER).texture(MIPMAP_BLOCK_ATLAS_TEXTURE).build(true));

    public static RenderLayer getHexySolid(){
        return HEXY_SOLID;
    }

}
