package com.samsthenerd.duckyperiphs.hexcasting;


import com.mojang.datafixers.util.Pair;

import at.petrak.hexcasting.api.client.ScryingLensOverlayRegistry;
import at.petrak.hexcasting.common.lib.HexItems;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

public class DuckyCastingClient {

    public static void init(){
        // EntityRendererRegistry.register( DuckyCasting.FOCAL_PORT_WRAPPER_ENTITY, EmptyEntityRenderer::new);
        // BlockEntityRendererRegistry.register(DuckyCasting.CONJURED_DUCKY_BLOCK_ENTITY, ConjuredDuckyBER::new);

        setupColorProviders();
        setupScryingDisplayers();
    }

    private static void setupColorProviders(){
        // ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
        //     if(tintIndex != 0) {
		// 		return 0xFFFFFF;
		// 	}
        //     // int thisColor = FocalPortBlock.getColor(world, pos);
        //     int thisColor = 0;
        //     RenderAttachedBlockView view = (RenderAttachedBlockView) world;
        //     if(view != null){
        //         Object rawValue = view.getBlockEntityRenderAttachment(pos);
        //         if(rawValue != null){
        //             thisColor = (int) rawValue;
        //         }
        //     }
        //     // DuckyPeriph.LOGGER.info("Color: " + thisColor);
        //     return thisColor;
        // }, DuckyCasting.FOCAL_PORT_BLOCK);
    }

    private static void setupScryingDisplayers(){
        ScryingLensOverlayRegistry.addDisplayer(DuckyCasting.FOCAL_PORT_BLOCK.get(), 
        (lines, state, pos, observer, world, direction) -> {
            if(world.getBlockEntity(pos) instanceof FocalPortBlockEntity fpbe){
                if(!fpbe.hasFocus()){
                    lines.add(new Pair<>(ItemStack.EMPTY, Text.literal("No Focus")));
                } else {
                    ItemStack focusStack = fpbe.getStack(0);
                    // put the focus description first so it doesn't cut off the name
                    lines.add(new Pair<ItemStack, Text>(new ItemStack(Items.AIR), HexIotaTypes.getDisplay(HexItems.FOCUS.readIotaTag(focusStack))));

                    lines.add(new Pair<>(focusStack, focusStack.getName().copy()
                    .styled((style) -> style.withColor(TextColor.fromRgb(fpbe.getColor())))));
                }
            }
        });
    }
}
