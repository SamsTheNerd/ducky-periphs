package com.samsthenerd.duckyperiphs.hexcasting.hexal;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;

public class DuckyHexalClient {
    public static void init(){
        // RenderTypeRegistry.register(RenderLayer.getTranslucent(), DuckyHexal.FOCAL_LINK_BLOCK.get());

        // setupColorProviders();
    }

    private static void setupColorProviders(){
        ColorHandlerRegistry.registerBlockColors((state, world, pos, tintIndex) -> {
            if(tintIndex != 0) {
				return 0xFFFFFF;
			}
            int thisColor = FocalLinkBlock.getColor(world, pos);
            return thisColor;
        }, DuckyHexal.FOCAL_LINK_BLOCK.get());
    }
}
