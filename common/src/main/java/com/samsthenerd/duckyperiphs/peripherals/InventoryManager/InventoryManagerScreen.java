package com.samsthenerd.duckyperiphs.peripherals.InventoryManager;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

// ported from Advanced Peripherals InventoryManager (Apache-2.0)
// https://github.com/Seniorendi/AdvancedPeripherals
// Making this Peripheral Cross Mod Loader Available
public class InventoryManagerScreen extends HandledScreen<InventoryManagerScreenHandler> {

    private static final Identifier TEXTURE = DuckyPeriphs.id("textures/gui/inventory_manager_gui.png");

    public InventoryManagerScreen(InventoryManagerScreenHandler handler, PlayerInventory inventory, Text title){
        super(handler, inventory, title);

        this.backgroundWidth = 174;
        this.backgroundHeight = 165;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY){
        context.drawTexture(TEXTURE, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight, 256, 256);
    }
}
