package com.samsthenerd.duckyperiphs.hexcasting;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;
import com.samsthenerd.duckyperiphs.compat.gloopy.FakeGloopyUtils;
import com.samsthenerd.duckyperiphs.compat.gloopy.IGloopyUtils;
import com.samsthenerd.duckyperiphs.hexcasting.hexal.DuckyHexal;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.math.HexDir;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class DuckyCasting {
    // focal port (hex casting)
	public static RegistrySupplier<FocalPortBlock> FOCAL_PORT_BLOCK;
	public static RegistrySupplier<BlockEntityType<FocalPortBlockEntity>> FOCAL_PORT_BLOCK_ENTITY;

	public static RegistrySupplier<EntityType<FocalPortWrapperEntity>> FOCAL_PORT_WRAPPER_ENTITY;

	public static RegistrySupplier<BlockEntityType<ConjuredDuckyBlockEntity>> CONJURED_DUCKY_BLOCK_ENTITY;
	public static RegistrySupplier<ConjuredDuckyBlock> CONJURED_DUCKY_BLOCK;

	public static IGloopyUtils GLOOPY_UTILS_INSTANCE = new FakeGloopyUtils();

	private static Map<Identifier, ActionRegistryEntry> ACTIONS = new HashMap<>();

	public static void init(){
		// do these registries in here so we can be sure it only happens when hex casting is installed
		FOCAL_PORT_BLOCK = DuckyPeriphs.blockItem("focal_port_block", 
			() -> new FocalPortBlock(Block.Settings.create().hardness((float)1.0).luminance(state -> 5)));
		
		FOCAL_PORT_BLOCK_ENTITY = DuckyPeriphs.blockEntities.register(new Identifier(DuckyPeriphs.MOD_ID, "focal_port_block_entity"), 
			() -> BlockEntityType.Builder.create(FocalPortBlockEntity::new, FOCAL_PORT_BLOCK.get()).build(null));

		FOCAL_PORT_WRAPPER_ENTITY = DuckyPeriphs.entities.register(new Identifier(DuckyPeriphs.MOD_ID, "focal_port_wrapper_entity"),
			() -> EntityType.Builder.create(FocalPortWrapperEntity::new, SpawnGroup.MISC).setDimensions(0.5f, 0.5f).build(new Identifier(DuckyPeriphs.MOD_ID, "focal_port_wrapper_entitiy").toString()));


		CONJURED_DUCKY_BLOCK = DuckyPeriphs.blockItem("conjured_ducky_block", 
			() -> new ConjuredDuckyBlock(Block.Settings.create().hardness((float)1.0).luminance(state -> 5)), new Item.Settings());

		CONJURED_DUCKY_BLOCK_ENTITY = DuckyPeriphs.blockEntities.register(new Identifier(DuckyPeriphs.MOD_ID, "conjured_ducky_block_entity"), 
			() -> BlockEntityType.Builder.create(ConjuredDuckyBlockEntity::new, CONJURED_DUCKY_BLOCK.get()).build(null));

		// register it to the hex casting registries

		if(Platform.isForge()){
			EnvExecutor.runInEnv(Env.CLIENT, () -> DuckyCastingClient::registerEntityRenderers);
		}

		if(Platform.isModLoaded("hexal")){
			DuckyHexal.init();
		}

		makeSpells();
	}

	private static void makeSpells(){
		HexPattern conjureDuckyPattern = HexPattern.fromAngles("aqadweeeede", HexDir.NORTH_EAST);
		try {
			make("conjure_ducky", conjureDuckyPattern, new OpPlaceDucky());
		} catch (IllegalArgumentException exn) {
            exn.printStackTrace();
        }
	}

	// ok this needs to be called somewhere,, probably
	public static void registerSpells(BiConsumer<ActionRegistryEntry, Identifier> r) {
		for (Entry<Identifier, ActionRegistryEntry> entry : ACTIONS.entrySet()) {
			r.accept(entry.getValue(), entry.getKey());
		}
	}

	// yoink from hexal	
	private static ActionRegistryEntry make(String name, HexPattern pattern, Action action){
		return make(name, new ActionRegistryEntry(pattern, action));
	}

	private static ActionRegistryEntry make(String name, ActionRegistryEntry are){
		if (ACTIONS.put(new Identifier(DuckyPeriphs.MOD_ID, name), are) != null) {
			throw new IllegalArgumentException("Typo? Duplicate id $name");
		} else {
			return are;
		}
	}
}
