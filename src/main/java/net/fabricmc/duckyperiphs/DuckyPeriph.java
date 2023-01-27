package net.fabricmc.duckyperiphs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.duckyperiphs.ducks.DuckBlock;
import net.fabricmc.duckyperiphs.ducks.DuckBlockEntity;
import net.fabricmc.duckyperiphs.ducks.DuckItem;
import net.fabricmc.duckyperiphs.peripherals.EntityDetector.EntityDetectorBlock;
import net.fabricmc.duckyperiphs.peripherals.EntityDetector.EntityDetectorTile;
import net.fabricmc.duckyperiphs.peripherals.WeatherMachine.WeatherMachineBlock;
import net.fabricmc.duckyperiphs.peripherals.WeatherMachine.WeatherMachineTile;
import net.fabricmc.duckyperiphs.peripherals.keyboards.KeyCaps;
import net.fabricmc.duckyperiphs.peripherals.keyboards.KeyboardBlock;
import net.fabricmc.duckyperiphs.peripherals.keyboards.KeyboardItem;
import net.fabricmc.duckyperiphs.peripherals.keyboards.KeyboardScreenHandler;
import net.fabricmc.duckyperiphs.peripherals.keyboards.KeyboardTile;
import net.fabricmc.duckyperiphs.peripherals.keyboards.KeyboardUtils;
import net.fabricmc.duckyperiphs.peripherals.sculkophone.SculkophoneBlock;
import net.fabricmc.duckyperiphs.peripherals.sculkophone.SculkophoneBlockEntity;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.Material;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DuckyPeriph implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("ducky-periphs");

	public static final String MOD_ID = "ducky-periphs";


	public static final ItemGroup CC_PERIPHS_GROUP = FabricItemGroupBuilder.build(
		new Identifier("ducky-periphs", "general"),
		() -> new ItemStack(DuckyPeriph.DUCK_ITEM));

	// Peripheral Registering

	// Weather Machine Registering
	public static BlockEntityType<WeatherMachineTile> WEATHER_MACHINE_TILE;
	public static final WeatherMachineBlock WEATHER_MACHINE_BLOCK = new WeatherMachineBlock(FabricBlockSettings.of(Material.STONE).hardness((float)1.3));
	public static final BlockItem WEATHER_MACHINE_ITEM = new BlockItem(WEATHER_MACHINE_BLOCK, new Item.Settings().group(CC_PERIPHS_GROUP));

	// Entity Detector Registering
	public static BlockEntityType<EntityDetectorTile> ENTITY_DETECTOR_TILE;
	public static final EntityDetectorBlock ENTITY_DETECTOR_BLOCK = new EntityDetectorBlock(FabricBlockSettings.of(Material.STONE).hardness((float)1.3));
	public static final BlockItem ENTITY_DETECTOR_ITEM = new BlockItem(ENTITY_DETECTOR_BLOCK, new Item.Settings().group(CC_PERIPHS_GROUP));

	// Keyboard Registering - may end up having more keyboards here later
	public static BlockEntityType<KeyboardTile> KEYBOARD_TILE;
	public static final KeyboardBlock KEYBOARD_BLOCK = new KeyboardBlock(FabricBlockSettings.of(Material.STONE).hardness((float)0.7));
	public static final KeyboardItem KEYBOARD_ITEM = new KeyboardItem(KEYBOARD_BLOCK, new Item.Settings().group(CC_PERIPHS_GROUP));
	public static final ScreenHandlerType<KeyboardScreenHandler> KEYBOARD_SCREEN_HANDLER = new ExtendedScreenHandlerType<>(KeyboardScreenHandler::new);
	public static final Identifier KEYBOARD_PRESS_PACKET_ID = new Identifier(MOD_ID, "keyboard_press");

	//duck time !
	public static BlockEntityType<DuckBlockEntity> DUCK_BLOCK_ENTITY;
	public static final DuckBlock DUCK_BLOCK = new DuckBlock(FabricBlockSettings.of(Material.WOOL).hardness((float)0.2));
	public static final DuckItem DUCK_ITEM = new DuckItem(DUCK_BLOCK, new Item.Settings().group(CC_PERIPHS_GROUP));
	public static SoundEvent QUACK_SOUND_EVENT = new SoundEvent(new Identifier(MOD_ID, "quack"));

	// sculkophone
	public static BlockEntityType<SculkophoneBlockEntity> SCULKOPHONE_BLOCK_ENTITY;
	public static final SculkophoneBlock SCULKOPHONE_BLOCK = new SculkophoneBlock(FabricBlockSettings.of(Material.STONE).hardness((float)0.2));
	public static final BlockItem SCULKOPHONE_ITEM = new BlockItem(SCULKOPHONE_BLOCK, new Item.Settings().group(CC_PERIPHS_GROUP));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		registerWeatherMachine();
		registerEntityDetector();
		registerKeyboard();
		registerDucks();
		registerSculkophone();

		DPRecipeSerializer.init();

	}

	private void registerWeatherMachine(){
		WEATHER_MACHINE_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ducky-periphs:weather_machine_tile",
		FabricBlockEntityTypeBuilder.create((blockPos, blockState) -> new WeatherMachineTile(WEATHER_MACHINE_TILE, blockPos, blockState), WEATHER_MACHINE_BLOCK).build(null));

		Registry.register(Registry.BLOCK, new Identifier("ducky-periphs", "weather_machine_block"), WEATHER_MACHINE_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("ducky-periphs", "weather_machine_block"), WEATHER_MACHINE_ITEM);

		ColorProviderRegistry.BLOCK.register((state, view, pos, tintIndex) -> {
			if (view == null || pos == null) {
				return GrassColors.getColor(0.5, 1.0);
            }
            return BiomeColors.getGrassColor(view, pos);
		}, WEATHER_MACHINE_BLOCK);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			return GrassColors.getColor(0.5, 1.0);
		}, WEATHER_MACHINE_ITEM);
	}

	private void registerEntityDetector(){
		ENTITY_DETECTOR_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ducky-periphs:entity_detector_tile",
				FabricBlockEntityTypeBuilder.create((blockPos, blockState) -> new EntityDetectorTile(ENTITY_DETECTOR_TILE, blockPos, blockState), ENTITY_DETECTOR_BLOCK).build(null));
		Registry.register(Registry.BLOCK, new Identifier("ducky-periphs", "entity_detector_block"), ENTITY_DETECTOR_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("ducky-periphs", "entity_detector_block"), ENTITY_DETECTOR_ITEM);
	}

	private void registerKeyboard(){
		// main stuff
		KEYBOARD_TILE = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ducky-periphs:keyboard_tile",
				FabricBlockEntityTypeBuilder.create((blockPos, blockState) -> new KeyboardTile(KEYBOARD_TILE, blockPos, blockState), KEYBOARD_BLOCK).build(null));
		Registry.register(Registry.BLOCK, new Identifier("ducky-periphs", "keyboard_block"), KEYBOARD_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("ducky-periphs", "keyboard_block"), KEYBOARD_ITEM);
		Registry.register(Registry.SCREEN_HANDLER, new Identifier("ducky-periphs", "keyboard_screen_handler"), KEYBOARD_SCREEN_HANDLER);
		// keycap color provider
		ColorProviderRegistry.BLOCK.register((state,view,pos,tintIndex)->{
			if(view == null || pos == null){
				return DyeColor.BLUE.getFireworkColor();
				// return KeyCaps.DEFAULT_COLOR;
			}
			return KEYBOARD_BLOCK.getKeyCaps(view, pos).getZoneColor(tintIndex);
		}, KEYBOARD_BLOCK);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			return KeyCaps.fromItemStack(stack).getZoneColor(tintIndex);
		}, KEYBOARD_ITEM);

		// network packet stuff 

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

	private void registerDucks(){
		// block/entity stuff
		DUCK_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ducky-periphs:duck_block_entity",
				FabricBlockEntityTypeBuilder.create((blockPos, blockState) -> new DuckBlockEntity(blockPos, blockState), DUCK_BLOCK).build(null));
		Registry.register(Registry.BLOCK, new Identifier("ducky-periphs", "duck_block"), DUCK_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("ducky-periphs", "duck_block"), DUCK_ITEM);

		// duck color providers
		ColorProviderRegistry.BLOCK.register((state,view,pos, tintIndex) -> {
			return DUCK_BLOCK.getColor(view, pos);
		}, DUCK_BLOCK);
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
			if(tintIndex != 0) {
				return 0xFFFFFF;
			}
			return DUCK_ITEM.getColor(stack);
		}, DUCK_ITEM);

		// quack
		Registry.register(Registry.SOUND_EVENT, new Identifier("ducky-periphs", "quack"), QUACK_SOUND_EVENT);

		// so we can dye it - hopefully this overrides block placing behavior?
		CauldronBehavior.WATER_CAULDRON_BEHAVIOR.put(DUCK_ITEM, CauldronBehavior.CLEAN_DYEABLE_ITEM);
	}

	private void registerSculkophone(){
		// default
		SCULKOPHONE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "ducky-periphs:sculkophone_block_entity",
				FabricBlockEntityTypeBuilder.create((blockPos, blockState) -> new SculkophoneBlockEntity(blockPos, blockState), SCULKOPHONE_BLOCK).build(null));
		Registry.register(Registry.BLOCK, new Identifier("ducky-periphs", "sculkophone_block"), SCULKOPHONE_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("ducky-periphs", "sculkophone_block"), SCULKOPHONE_ITEM);
	}
}
