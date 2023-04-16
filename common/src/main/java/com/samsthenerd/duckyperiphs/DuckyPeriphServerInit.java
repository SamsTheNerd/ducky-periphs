package com.samsthenerd.duckyperiphs;
// package com.samsthenerd.duckyperiphs;

// import net.fabricmc.api.EnvType;
// import net.fabricmc.api.Environment;

// @Environment (EnvType.SERVER)
// public class DuckyPeriphServerInit implements ServerModInitializer {
//     private final Identifier keyboardLootTable = new Identifier("ducky-periphs", "loot_tables/keyboards");
// 	private void registerLoot(){
// 		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
// 			if (source.isBuiltin() && (LootTables.SIMPLE_DUNGEON_CHEST.equals(id)
// 			|| LootTables.ABANDONED_MINESHAFT_CHEST.equals(id) || LootTables.DESERT_PYRAMID_CHEST.equals(id)
// 			|| LootTables.WOODLAND_MANSION_CHEST.equals(id) || LootTables.JUNGLE_TEMPLE_CHEST.equals(id))) {
// 				LootPool.Builder poolBuilder = LootPool.builder();
// 				LootPool pool = lootManager.getTable(keyboardLootTable).pools[0];


// 				tableBuilder.pool(poolBuilder);
// 			}
// 		});
// 	}
// }
