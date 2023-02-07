package net.fabricmc.duckyperiphs.hexcasting;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.registry.Registry;

public class DuckyCasting {
    // focal port (hex casting)
	public static FocalPortBlock FOCAL_PORT_BLOCK;
	public static BlockEntityType<FocalPortBlockEntity> FOCAL_PORT_BLOCK_ENTITY;

	public static void init(){
		FOCAL_PORT_BLOCK = new FocalPortBlock(FabricBlockSettings.of(Material.AMETHYST).hardness((float)1.0));
		FOCAL_PORT_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ducky-periphs:focal_port_block_entity",
				FabricBlockEntityTypeBuilder.create((blockPos, blockState) -> new FocalPortBlockEntity(blockPos, blockState), FOCAL_PORT_BLOCK).build(null));
	}
}
