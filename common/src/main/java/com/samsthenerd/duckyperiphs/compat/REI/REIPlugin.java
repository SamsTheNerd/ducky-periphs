package com.samsthenerd.duckyperiphs.compat.REI;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

// so that REI won't pop up when we use keyboard
@Environment(EnvType.CLIENT)
public class REIPlugin implements REIClientPlugin {
    @Override
	public void registerScreens(ScreenRegistry registry) {
		registry.registerDecider(new REIOverlayDecider());
	}
}

// @Environment(EnvType.CLIENT)
// public class REIPlugin{

// }
