package com.samsthenerd.duckyperiphs.hexcasting;

import com.samsthenerd.duckyperiphs.DuckyPeriph;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class DuckyCasting {
    // focal port (hex casting)
	public static FocalPortBlock FOCAL_PORT_BLOCK;
	public static BlockItem FOCAL_PORT_ITEM;
	public static BlockEntityType<FocalPortBlockEntity> FOCAL_PORT_BLOCK_ENTITY;

	public static EntityType<FocalPortWrapperEntity> FOCAL_PORT_WRAPPER_ENTITY;


	public static void init(){
		FOCAL_PORT_BLOCK = new FocalPortBlock(FabricBlockSettings.of(Material.AMETHYST).hardness((float)1.0));
		FOCAL_PORT_ITEM = new BlockItem(FOCAL_PORT_BLOCK, new Item.Settings().group(DuckyPeriph.CC_PERIPHS_GROUP));
		Registry.register(Registry.BLOCK, "ducky-periphs:focal_port_block", FOCAL_PORT_BLOCK);
		Registry.register(Registry.ITEM, "ducky-periphs:focal_port_block", FOCAL_PORT_ITEM);
		FOCAL_PORT_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ducky-periphs:focal_port_block_entity",
				FabricBlockEntityTypeBuilder.create((blockPos, blockState) -> new FocalPortBlockEntity(blockPos, blockState), FOCAL_PORT_BLOCK).build(null));
		FOCAL_PORT_WRAPPER_ENTITY = Registry.register(Registry.ENTITY_TYPE, "ducky-periphs:focal_port_wrapper_entity", 
				FabricEntityTypeBuilder.<FocalPortWrapperEntity>create(SpawnGroup.MISC, FocalPortWrapperEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build()); 

		// register it to the hex casting registries
	}
}
