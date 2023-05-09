package com.samsthenerd.duckyperiphs.hexcasting;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.block.Material;

public class DummyNoHex {
    public static RegistrySupplier<Block> FOCAL_PORT_BLOCK;

	public static RegistrySupplier<Block> CONJURED_DUCKY_BLOCK;
    
    public static void init(){
        FOCAL_PORT_BLOCK = DuckyPeriphs.blockItem("focal_port_block", 
			() -> new Block(Block.Settings.of(Material.AMETHYST).hardness((float)1.0).luminance(state -> 5)));

        CONJURED_DUCKY_BLOCK = DuckyPeriphs.blockItem("conjured_ducky_block", 
			() -> new Block(Block.Settings.of(Material.AMETHYST).hardness((float)1.0).luminance(state -> 5)));
    }
}
