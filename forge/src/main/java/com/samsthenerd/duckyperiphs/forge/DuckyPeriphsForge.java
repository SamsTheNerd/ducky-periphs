package com.samsthenerd.duckyperiphs.forge;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;
import com.samsthenerd.duckyperiphs.DuckyPeriphsClientInit;
import com.samsthenerd.duckyperiphs.forge.hexcasting.DuckyCastingCaps;

import dan200.computercraft.api.ForgeComputerCraftAPI;
import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("duckyperiphs")
public class DuckyPeriphsForge {
    public DuckyPeriphsForge(){
        // so that we can register properly with architectury
        EventBuses.registerModEventBus(DuckyPeriphs.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);

        DuckyPeriphs.onInitialize();
        // EnvExecutor.runInEnv(Env.CLIENT, () -> DuckyPeriphsClientInit::initClient);

        ForgeComputerCraftAPI.registerPeripheralProvider(new DuckyPeriphsProviderForge());

        addLootPools();

        if(Platform.isModLoaded("hexcasting")){
            // DuckyCastingCaps.registerCaps();
            MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, DuckyCastingCaps::attachEntityCaps);
            MinecraftForge.EVENT_BUS.addGenericListener(ItemStack.class, DuckyCastingCaps::attachItemCaps);
        }
    }

    private void onClientSetup(FMLClientSetupEvent event) { 
        event.enqueueWork(() -> {
            DuckyPeriphsClientInit.initClient();
        });
    }

    // forge is a miserable pile of mess, idk why it's different in such weird ways
    public static void addLootPools(){
        // LootEvent.MODIFY_LOOT_TABLE.register((lootTables, id, context, builtin) -> {
		// 	if (DuckyPeriphs.shouldAddKeyboards(id, builtin)) {
		// 		LootTable kbLootTable = lootTables.getTable(DuckyPeriphs.keyboardLootTable);
        //         List<LootPool> pools = kbLootTable.f_79109_;
        //         DuckyPeriphs.logPrint("found " + pools.size() + " pools");
        //         for (LootPool pool : pools) {
        //             DuckyPeriphs.logPrint("Adding loot pool to " + id.toString() + ": " + pool.getName());
        //             context.addPool(pool);
        //         }
		// 	}
		// });
    }
}
