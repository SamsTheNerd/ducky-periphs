package com.samsthenerd.duckyperiphs.compat.REI;

import com.samsthenerd.duckyperiphs.peripherals.keyboards.KeyboardScreen;

import me.shedaniel.rei.api.client.registry.screen.OverlayDecider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ActionResult;

@Environment(EnvType.CLIENT)
public class REIOverlayDecider implements OverlayDecider {
    @Override
	public <R extends Screen> boolean isHandingScreen(Class<R> screen) {
		return screen.getPackageName().startsWith("com.samsthenerd.duckyperiphs");
		// return false;
	}
	
	@Override
	public <R extends Screen> ActionResult shouldScreenBeOverlaid(R screen) {
		if(screen instanceof KeyboardScreen) {
			return ActionResult.FAIL;
		}
		return ActionResult.PASS;
	}
}

// public class REIOverlayDecider{
	
// }