package com.samsthenerd.duckyperiphs.hexcasting.hexal;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;

public class DuckyHexal {
    public static RegistrySupplier<FocalLinkBlock> FOCAL_LINK_BLOCK;
	public static RegistrySupplier<BlockEntityType<FocalLinkBlockEntity>> FOCAL_LINK_BLOCK_ENTITY;

    public static void init(){
		// do these registries in here so we can be sure it only happens when hex casting is installed
		FOCAL_LINK_BLOCK = DuckyPeriphs.blockItem("focal_link_block", 
			() -> new FocalLinkBlock(Block.Settings.of(Material.AMETHYST).hardness((float)1.0).luminance(state -> 5)));
		
		FOCAL_LINK_BLOCK_ENTITY = DuckyPeriphs.blockEntities.register(new Identifier(DuckyPeriphs.MOD_ID, "focal_link_block_entity"), 
			() -> BlockEntityType.Builder.create(FocalLinkBlockEntity::new, FOCAL_LINK_BLOCK.get()).build(null));

    }
}
