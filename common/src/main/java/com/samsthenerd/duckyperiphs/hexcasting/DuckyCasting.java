package com.samsthenerd.duckyperiphs.hexcasting;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import at.petrak.hexcasting.api.PatternRegistry;
import at.petrak.hexcasting.api.spell.math.HexDir;
import at.petrak.hexcasting.api.spell.math.HexPattern;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;

public class DuckyCasting {
    // focal port (hex casting)
	public static RegistrySupplier<FocalPortBlock> FOCAL_PORT_BLOCK;
	public static RegistrySupplier<BlockEntityType<FocalPortBlockEntity>> FOCAL_PORT_BLOCK_ENTITY;

	public static RegistrySupplier<EntityType<FocalPortWrapperEntity>> FOCAL_PORT_WRAPPER_ENTITY;

	public static RegistrySupplier<BlockEntityType<ConjuredDuckyBlockEntity>> CONJURED_DUCKY_BLOCK_ENTITY;
	public static RegistrySupplier<ConjuredDuckyBlock> CONJURED_DUCKY_BLOCK;


	public static void init(){
		// do these registries in here so we can be sure it only happens when hex casting is installed
		FOCAL_PORT_BLOCK = DuckyPeriphs.blockItem("focal_port_block", 
			() -> new FocalPortBlock(Block.Settings.of(Material.AMETHYST).hardness((float)1.0).luminance(state -> 5)));
		
		FOCAL_PORT_BLOCK_ENTITY = DuckyPeriphs.blockEntities.register(new Identifier(DuckyPeriphs.MOD_ID, "focal_port_block_entity"), 
			() -> BlockEntityType.Builder.create(FocalPortBlockEntity::new, FOCAL_PORT_BLOCK.get()).build(null));

		FOCAL_PORT_WRAPPER_ENTITY = DuckyPeriphs.entities.register(new Identifier(DuckyPeriphs.MOD_ID, "focal_port_wrapper_entity"),
			() -> EntityType.Builder.create(FocalPortWrapperEntity::new, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).build(new Identifier(DuckyPeriphs.MOD_ID, "focal_port_wrapper_entitiy").toString()));


		CONJURED_DUCKY_BLOCK = DuckyPeriphs.blockItem("conjured_ducky_block", 
			() -> new ConjuredDuckyBlock(Block.Settings.of(Material.AMETHYST).hardness((float)1.0).luminance(state -> 5)));

		CONJURED_DUCKY_BLOCK_ENTITY = DuckyPeriphs.blockEntities.register(new Identifier(DuckyPeriphs.MOD_ID, "conjured_ducky_block_entity"), 
			() -> BlockEntityType.Builder.create(ConjuredDuckyBlockEntity::new, CONJURED_DUCKY_BLOCK.get()).build(null));

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
