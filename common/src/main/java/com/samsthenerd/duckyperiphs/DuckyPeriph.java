package com.samsthenerd.duckyperiphs;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Suppliers;
import com.samsthenerd.duckyperiphs.ducks.DuckBlock;
import com.samsthenerd.duckyperiphs.ducks.DuckBlockEntity;
import com.samsthenerd.duckyperiphs.ducks.DuckItem;
import com.samsthenerd.duckyperiphs.hexcasting.DuckyCasting;
import com.samsthenerd.duckyperiphs.misc.DuckyBanners;
import com.samsthenerd.duckyperiphs.peripherals.EntityDetector.EntityDetectorBlock;
import com.samsthenerd.duckyperiphs.peripherals.EntityDetector.EntityDetectorTile;
import com.samsthenerd.duckyperiphs.peripherals.WeatherMachine.WeatherMachineBlock;
import com.samsthenerd.duckyperiphs.peripherals.WeatherMachine.WeatherMachineTile;
import com.samsthenerd.duckyperiphs.peripherals.keyboards.KeyboardBlock;
import com.samsthenerd.duckyperiphs.peripherals.keyboards.KeyboardItem;
import com.samsthenerd.duckyperiphs.peripherals.keyboards.KeyboardScreenHandler;
import com.samsthenerd.duckyperiphs.peripherals.keyboards.KeyboardTile;
import com.samsthenerd.duckyperiphs.peripherals.keyboards.KeyboardUtils;
import com.samsthenerd.duckyperiphs.peripherals.sculkophone.SculkophoneBlock;
import com.samsthenerd.duckyperiphs.peripherals.sculkophone.SculkophoneBlockEntity;
import com.samsthenerd.duckyperiphs.utils.EntityFromBlockEntity;

import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.Registries;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BannerPatternItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.GameEvent;

public class DuckyPeriph implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("ducky-periphs");

	public static final String MOD_ID = "ducky-periphs";
	public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));

	// various architectury registry wrappers - could maybe move each into their own files, we'll see.
	public static Registrar<Item> items = REGISTRIES.get().get(Registry.ITEM_KEY);
	public static Registrar<Block> blocks = REGISTRIES.get().get(Registry.BLOCK_KEY);
	public static Registrar<BlockEntityType<?>> blockEntities = REGISTRIES.get().get(Registry.BLOCK_ENTITY_TYPE_KEY);
	public static Registrar<EntityType<?>> entities = REGISTRIES.get().get(Registry.ENTITY_TYPE_KEY);
	public static final Registrar<SoundEvent> sounds = REGISTRIES.get().get(Registry.SOUND_EVENT_KEY);
	public static final Registrar<GameEvent> gameEvents = REGISTRIES.get().get(Registry.GAME_EVENT_KEY);
	public static final DeferredRegister<ScreenHandlerType<?> > screenHandlers = DeferredRegister.create(MOD_ID, Registry.MENU_KEY);

	// maps of things we need to register. add them here so we can set things up before actually registering.
	private static final Map<Identifier, Block> BLOCKS = new LinkedHashMap<>();
    private static final Map<Identifier, Pair<Block, Item.Settings>> BLOCK_ITEMS = new LinkedHashMap<>();
	private static final Map<Identifier, Item> ITEMS = new LinkedHashMap<>();
	private static final Map<Identifier, BlockEntityType<?>> BLOCK_ENTITIES = new LinkedHashMap<>();
	private static final Map<Identifier, EntityType<?>> ENTITIES = new LinkedHashMap<>();
	private static final Map<Identifier, SoundEvent> SOUND_EVENTS = new LinkedHashMap<>();
	private static final Map<Identifier, GameEvent> GAME_EVENTS = new LinkedHashMap<>();


	public static final ItemGroup CC_PERIPHS_GROUP = CreativeTabRegistry.create(
		new Identifier("ducky-periphs", "general"),
		() -> new ItemStack(DuckyPeriph.DUCK_ITEM));

	// Peripheral Registering

	// Weather Machine Registering
	public static WeatherMachineBlock WEATHER_MACHINE_BLOCK = blockItem("weather_machine_block", new WeatherMachineBlock(peripheralBlockSettings()));
	public static BlockEntityType<WeatherMachineTile> WEATHER_MACHINE_TILE = blockEntity("weather_machine_tile", (blockPos, blockState) -> new WeatherMachineTile(blockPos, blockState), WEATHER_MACHINE_BLOCK);

	// Entity Detector Registering
	public static final EntityDetectorBlock ENTITY_DETECTOR_BLOCK = blockItem("entity_detector_block", new EntityDetectorBlock(peripheralBlockSettings()));
	public static BlockEntityType<EntityDetectorTile> ENTITY_DETECTOR_TILE = blockEntity("entity_detector_tile", (blockPos, blockState) -> new EntityDetectorTile(blockPos, blockState), ENTITY_DETECTOR_BLOCK);

	// Keyboard Registering - may end up having more keyboards here later
	public static final KeyboardBlock KEYBOARD_BLOCK = blockNoItem("keyboard_block", new KeyboardBlock(peripheralBlockSettings().hardness((float)0.7)));
	public static BlockEntityType<KeyboardTile> KEYBOARD_TILE = blockEntity("keyboard_tile", (blockPos, blockState) -> new KeyboardTile(blockPos, blockState), KEYBOARD_BLOCK);
	public static final KeyboardItem KEYBOARD_ITEM = item("keyboard_block", new KeyboardItem(KEYBOARD_BLOCK, new Item.Settings().group(CC_PERIPHS_GROUP)));
	public static final ScreenHandlerType<KeyboardScreenHandler> KEYBOARD_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(KeyboardScreenHandler::new);
	public static final Identifier KEYBOARD_PRESS_PACKET_ID = new Identifier(MOD_ID, "keyboard_press");

	//duck time !
	public static final DuckBlock DUCK_BLOCK = blockNoItem("duck_block", new DuckBlock(Block.Settings.of(Material.WOOL).hardness((float)0.2)));
	public static BlockEntityType<DuckBlockEntity> DUCK_BLOCK_ENTITY = blockEntity("duck_block_entity", (blockPos, blockState) -> new DuckBlockEntity(blockPos, blockState), DUCK_BLOCK);
	public static final DuckItem DUCK_ITEM = item("duck_block", new DuckItem(DUCK_BLOCK, dpItemSettings()));
	public static SoundEvent QUACK_SOUND_EVENT = soundEvent("quack");
	public static GameEvent QUACK_GAME_EVENT = gameEvent("quack", 16);

	// sculkophone
	public static final SculkophoneBlock SCULKOPHONE_BLOCK = blockItem("sculkophone_block", new SculkophoneBlock(peripheralBlockSettings().hardness((float)0.7)));
	public static BlockEntityType<SculkophoneBlockEntity> SCULKOPHONE_BLOCK_ENTITY = blockEntity("sculkophone_block_entity", (blockPos, blockState) -> new SculkophoneBlockEntity(blockPos, blockState), SCULKOPHONE_BLOCK);
	public static SoundEvent SCULKOPHONE_CLICKING_EVENT = soundEvent( "sculkophone_clicking");
	public static SoundEvent SCULKOPHONE_CLICKING_STOP_EVENT = soundEvent("sculkophone_clicking_stop");
	// public static GameEvent SCULKOPHONE_CLICKING_GAME_EVENT;

	// it's just helpful - using it mainly for focal port hex casting rn but not unique to it
	public static EntityType<EntityFromBlockEntity> ENTITY_FROM_BLOCK_ENTITY;

	// Banners
	public static final BannerPatternItem DUCKY_PATTERN_ITEM = item("ducky_banner_pattern", new BannerPatternItem(DuckyBanners.DUCKY_PATTERN_ITEM_KEY, new Item.Settings().maxCount(1).group(ItemGroup.MISC)));
	
	// public static BlockEntityType<StrongModemBlockEntity> STRONG_MODEM_BLOCK_ENTITY;
	// public static final StrongModemBlock STRONG_MODEM_BLOCK=null; //= new StrongModemBlock(FabricBlockSettings.of(Material.STONE).hardness((float)0.7));
	// public static final BlockItem STRONG_MODEM_ITEM=null; //= new BlockItem(STRONG_MODEM_BLOCK, new Item.Settings().group(CC_PERIPHS_GROUP));


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		// setup registries
		items = REGISTRIES.get().get(Registry.ITEM_KEY);
		blocks = REGISTRIES.get().get(Registry.BLOCK_KEY);
		blockEntities = REGISTRIES.get().get(Registry.BLOCK_ENTITY_TYPE_KEY);

		

		DuckyBanners.registerBannerPatterns();
		registerLoot();

		DPRecipeSerializer.init();

		if(Platform.isModLoaded("hexcasting")){
			DuckyCasting.init();
		}

		
	}

	private void setupNetworkStuff(){
		// for key presses
		ServerPlayNetworking.registerGlobalReceiver(new Identifier(DuckyPeriph.MOD_ID, "key_press_packet"), 
		(server, player, handler, buf, responseSender) -> KeyboardUtils.keyPressHandler(server, player, handler, buf, responseSender));
		// for key ups
		ServerPlayNetworking.registerGlobalReceiver(new Identifier(DuckyPeriph.MOD_ID, "key_up_packet"), 
		(server, player, handler, buf, responseSender) -> KeyboardUtils.keyUpHandler(server, player, handler, buf, responseSender));

		// for chars typed
		ServerPlayNetworking.registerGlobalReceiver(new Identifier(DuckyPeriph.MOD_ID, "char_typed_packet"), 
        (server, player, handler, buf, responseSender) -> KeyboardUtils.charTypedHandler(server, player, handler, buf, responseSender));

		// for chars typed
		ServerPlayNetworking.registerGlobalReceiver(new Identifier(DuckyPeriph.MOD_ID, "event_sent_packet"), 
        (server, player, handler, buf, responseSender) -> KeyboardUtils.eventShortcutHandler(server, player, handler, buf, responseSender));
	}

	private void setupMisc(){
		screenHandlers.register(new Identifier("ducky-periphs", "keyboard_screen_handler"), () -> MenuRegistry.ofExtended(KeyboardScreenHandler::new));

		CauldronBehavior.WATER_CAULDRON_BEHAVIOR.put(DUCK_ITEM, CauldronBehavior.CLEAN_DYEABLE_ITEM);
	}


	private static Block.Settings peripheralBlockSettings(){
		return Block.Settings.of(Material.STONE).hardness((float)1.3);
	}

	private static Item.Settings dpItemSettings(){
		return new Item.Settings().group(CC_PERIPHS_GROUP);
	}

	// stealing from hex casting :D
	private static <T extends Block> T blockNoItem(String name, T block) {
        var old = BLOCKS.put(new Identifier(MOD_ID, name), block);
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return block;
    }

	private static <T extends Item> T item(String name, T item) {
		var old = ITEMS.put(new Identifier(MOD_ID, name), item);
		if (old != null) {
			throw new IllegalArgumentException("Typo? Duplicate id " + name);
		}
		return item;
	}

    private static <T extends Block> T blockItem(String name, T block) {
        return blockItem(name, block, dpItemSettings());
    }

    private static <T extends Block> T blockItem(String name, T block, Item.Settings props) {
        blockNoItem(name, block);
        var old = BLOCK_ITEMS.put(new Identifier(MOD_ID, name), new Pair<>(block, props));
        if (old != null) {
            throw new IllegalArgumentException("Typo? Duplicate id " + name);
        }
        return block;
    }

	private static <T extends BlockEntity> BlockEntityType<T> blockEntity(String id,
        BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
		var be = BlockEntityType.Builder.create(func::apply, blocks).build(null);
        var old = BLOCK_ENTITIES.put(new Identifier(MOD_ID, id), be);
        if (old != null) {
            throw new IllegalArgumentException("Duplicate id " + id);
        }
        return be;
    }

	private static SoundEvent soundEvent(String id){
		var soundEvent = new SoundEvent(new Identifier(MOD_ID, id));
		var old = SOUND_EVENTS.put(new Identifier(MOD_ID, id), soundEvent);
		if (old != null) {
			throw new IllegalArgumentException("Duplicate id " + id);
		}
		return soundEvent;
	}

	private static GameEvent gameEvent(String id, int range){
		var gameEvent = new GameEvent(new Identifier(MOD_ID, id).toString(), range);
		var old = GAME_EVENTS.put(new Identifier(MOD_ID, id), gameEvent);
		if (old != null) {
			throw new IllegalArgumentException("Duplicate id " + id);
		}
		return gameEvent;
	}


	private final Identifier keyboardLootTable = new Identifier("ducky-periphs", "chests/keyboards");
	private void registerLoot(){
		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (source.isBuiltin() && (LootTables.SIMPLE_DUNGEON_CHEST.equals(id)
			|| LootTables.ABANDONED_MINESHAFT_CHEST.equals(id) || LootTables.DESERT_PYRAMID_CHEST.equals(id)
			|| LootTables.WOODLAND_MANSION_CHEST.equals(id) || LootTables.JUNGLE_TEMPLE_CHEST.equals(id))) {
				LootPool[] pools = lootManager.getTable(keyboardLootTable).pools;
				if(pools.length > 0){
					LootPool pool= pools[0];
					tableBuilder.pool(pool);
				}
			}
		});
	}
}
