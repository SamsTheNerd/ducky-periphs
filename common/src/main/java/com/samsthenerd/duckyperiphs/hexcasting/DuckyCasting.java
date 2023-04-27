package com.samsthenerd.duckyperiphs.hexcasting;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import at.petrak.hexcasting.api.PatternRegistry;
import at.petrak.hexcasting.api.spell.math.HexDir;
import at.petrak.hexcasting.api.spell.math.HexPattern;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DuckyCasting {
    // focal port (hex casting)
	public static FocalPortBlock FOCAL_PORT_BLOCK;
	public static BlockItem FOCAL_PORT_ITEM;
	public static BlockEntityType<FocalPortBlockEntity> FOCAL_PORT_BLOCK_ENTITY;

	public static EntityType<FocalPortWrapperEntity> FOCAL_PORT_WRAPPER_ENTITY;

	public static BlockEntityType<ConjuredDuckyBlockEntity> CONJURED_DUCKY_BLOCK_ENTITY;
	public static ConjuredDuckyBlock CONJURED_DUCKY_BLOCK;
	public static BlockItem CONJURED_DUCKY_ITEM; // yeah sure, throw it in for testing idc


	public static void init(){
		FOCAL_PORT_BLOCK = new FocalPortBlock(FabricBlockSettings.of(Material.AMETHYST).hardness((float)1.0).luminance(state -> 5));
		FOCAL_PORT_ITEM = new BlockItem(FOCAL_PORT_BLOCK, new Item.Settings().group(DuckyPeriphs.CC_PERIPHS_GROUP));
		Registry.register(Registry.BLOCK, "ducky-periphs:focal_port_block", FOCAL_PORT_BLOCK);
		Registry.register(Registry.ITEM, "ducky-periphs:focal_port_block", FOCAL_PORT_ITEM);
		FOCAL_PORT_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ducky-periphs:focal_port_block_entity",
				FabricBlockEntityTypeBuilder.create((blockPos, blockState) -> new FocalPortBlockEntity(blockPos, blockState), FOCAL_PORT_BLOCK).build(null));
		FOCAL_PORT_WRAPPER_ENTITY = Registry.register(Registry.ENTITY_TYPE, "ducky-periphs:focal_port_wrapper_entity", 
				FabricEntityTypeBuilder.<FocalPortWrapperEntity>create(SpawnGroup.MISC, FocalPortWrapperEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build()); 

		CONJURED_DUCKY_BLOCK = new ConjuredDuckyBlock(FabricBlockSettings.of(Material.AMETHYST).hardness((float)1.0).luminance(state -> 5));
		Registry.register(Registry.BLOCK, "ducky-periphs:conjured_ducky_block", CONJURED_DUCKY_BLOCK);
		CONJURED_DUCKY_ITEM = new BlockItem(CONJURED_DUCKY_BLOCK, new Item.Settings());
		Registry.register(Registry.ITEM, "ducky-periphs:conjured_ducky_block", CONJURED_DUCKY_ITEM);

		CONJURED_DUCKY_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ducky-periphs:conjured_ducky_block_entity",
				FabricBlockEntityTypeBuilder.create((blockPos, blockState) -> new ConjuredDuckyBlockEntity(blockPos, blockState), CONJURED_DUCKY_BLOCK).build(null));
		

		// register it to the hex casting registries

		registerSpells();
	}

	private static void registerSpells(){
		HexPattern conjureDuckyPattern = HexPattern.fromAngles("aqadweeeede", HexDir.NORTH_EAST);
		try {
			PatternRegistry.mapPattern(conjureDuckyPattern, new Identifier("ducky-periphs:conjure_ducky"), new OpPlaceDucky());
		} catch (PatternRegistry.RegisterPatternException exn) {
            exn.printStackTrace();
        }
	}
}
