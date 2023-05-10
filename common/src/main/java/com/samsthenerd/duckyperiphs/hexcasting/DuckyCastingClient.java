package com.samsthenerd.duckyperiphs.hexcasting;


import com.mojang.datafixers.util.Pair;

import at.petrak.hexcasting.api.client.ScryingLensOverlayRegistry;
import at.petrak.hexcasting.common.lib.HexItems;
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EmptyEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

@Environment(EnvType.CLIENT)
public class DuckyCastingClient {

    public static void init(){
        EntityRendererRegistry.register( DuckyCasting.FOCAL_PORT_WRAPPER_ENTITY, EmptyEntityRenderer::new);
        BlockEntityRendererRegistry.register(DuckyCasting.CONJURED_DUCKY_BLOCK_ENTITY.get(), ConjuredDuckyBER::new);

        RenderTypeRegistry.register(RenderLayer.getTranslucent(), DuckyCasting.FOCAL_PORT_BLOCK.get(), DuckyCasting.CONJURED_DUCKY_BLOCK.get());

        setupColorProviders();
        setupScryingDisplayers();
    }

    private static void setupColorProviders(){
        ColorHandlerRegistry.registerBlockColors((state, world, pos, tintIndex) -> {
            if(tintIndex != 0) {
				return 0xFFFFFF;
			}
            int thisColor = FocalPortBlock.getColor(world, pos);
            return thisColor;
        }, DuckyCasting.FOCAL_PORT_BLOCK.get());
    }

    private static void setupScryingDisplayers(){
        ScryingLensOverlayRegistry.addDisplayer(DuckyCasting.FOCAL_PORT_BLOCK.get(), 
        (lines, state, pos, observer, world, direction) -> {
            if(world.getBlockEntity(pos) instanceof FocalPortBlockEntity fpbe){
                if(!fpbe.hasFocus()){
                    lines.add(new Pair<>(ItemStack.EMPTY, Text.literal("No Focus")));
                } else {
                    ItemStack focusStack = fpbe.getStack(0);
                    NbtCompound tag = HexItems.FOCUS.readIotaTag(focusStack);
                    if(tag != null){
                        // put the focus description first so it doesn't cut off the name
                        lines.add(new Pair<ItemStack, Text>(new ItemStack(Items.AIR), HexIotaTypes.getDisplay(tag)));
                    }

                    lines.add(new Pair<>(focusStack, focusStack.getName().copy()
                    .styled((style) -> style.withColor(TextColor.fromRgb(fpbe.getColor())))));
                }
            }
        });
    }
}
