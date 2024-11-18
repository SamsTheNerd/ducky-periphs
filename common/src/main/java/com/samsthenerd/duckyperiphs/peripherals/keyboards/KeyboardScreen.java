package com.samsthenerd.duckyperiphs.peripherals.keyboards;

import java.util.HashSet;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;
import com.samsthenerd.duckyperiphs.mixin.MixinMouseLockAccessor;
import com.samsthenerd.duckyperiphs.utils.BlockHitFromScreen;

import dan200.computercraft.shared.peripheral.monitor.MonitorBlock;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;

public class KeyboardScreen extends HandledScreen<KeyboardScreenHandler> {

    HashSet<Integer> pressedKeys = new HashSet<Integer>();

    // events are terminate, shut-down, and reboot 
    // shortcuts are CTRL+T, CTRL+S, CTRL+R
    long eventsStarted[] = {0,0,0};
    boolean eventChecking[] = {false,false,false};
    static int eventShortcutKeys[] = {84, 83, 82};


    public KeyboardScreen(KeyboardScreenHandler handler, PlayerInventory inventory, Text title){
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY){
        // not sure what needs to go here 
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        // renderBackground(matrices);
        // super.render(matrices, mouseX, mouseY, delta);
        // drawMouseoverTooltip(matrices, x, y); // don't think we need this? 

        // need to draw stuff to say like 'hey press keys to type, press whatever to turn computer on/off, press esc to exit'
    }



    @Override
    protected void init(){
        super.init();
        ((MixinMouseLockAccessor) this.client.mouse).setCursorLocked(true); // so that you can look around while using keyboard
        InputUtil.setCursorParameters(this.client.getWindow().getHandle(), InputUtil.GLFW_CURSOR_DISABLED, // hide cursor
            this.client.mouse.getX(), this.client.mouse.getY());
        this.client.player.sendMessage(Text.translatable("ducky-periphs.keyboardescmsg"), true);
    }

    @Override
    public final boolean keyPressed( int key, int scancode, int modifiers )
    {
        // screen is only clientside
        if(key == 256){ // don't send, just close the screen
            return super.keyPressed( key, scancode, modifiers );
        }

        PacketByteBuf keyData = new PacketByteBuf(Unpooled.buffer());

        keyData.writeInt(key); // might need more
        keyData.writeInt(scancode);
        keyData.writeInt(modifiers);
        // might maybe need to send if caps lock is on or not too -- or throw that in with modifiers?
        // DuckyPeriphs.logPrint("modifiers: " + modifiers + " scancode: " + scancode + " key: " + key);
        // need to get char too ?
        // Key GLFW_Key = InputUtil.fromKeyCode(key, scancode);
        // logPrint("keycode: " + key + "| toString: " + GLFW_Key.toString() + "| localized: " + GLFW_Key.getLocalizedText());

        keyData.writeBoolean(pressedKeys.contains(key)); // for repeat, just need to actually add that

        String pasteText = "";

        // could make this cmd and ctrl ? or figure out how to do the specific stuff for OS 
        if(isPaste(key)){
            pasteText = this.client.keyboard.getClipboard();
        }
        keyData.writeString(pasteText);

        keyData.writeBlockPos(this.handler.pos);
        // DuckyPeriphs.logPrint("sending key press packet: " + key);
        NetworkManager.sendToServer(new Identifier(DuckyPeriphs.MOD_ID, "key_press_packet"), keyData);
        
        pressedKeys.add(key);

        // return super.keyPressed( key, scancode, modifiers );
        return true; // ?
    }

    @Override
    public final boolean charTyped(char this_char, int modifiers){
        // DuckyPeriphs.logPrint("char typed: " + this_char);
        PacketByteBuf charData = new PacketByteBuf(Unpooled.buffer());

        charData.writeChar(this_char);
        charData.writeInt(modifiers);
        charData.writeBlockPos(this.handler.pos);
        NetworkManager.sendToServer(new Identifier(DuckyPeriphs.MOD_ID, "char_typed_packet"), charData);

        return super.charTyped(this_char, modifiers);
    }

    @Override
    public final boolean keyReleased(int key, int scancode, int modifiers){
        PacketByteBuf keyData = new PacketByteBuf(Unpooled.buffer());

        keyData.writeInt(key); // might need more
        keyData.writeInt(scancode);
        keyData.writeInt(modifiers);
        // might maybe need to send if caps lock is on or not too -- or throw that in with modifiers?
        // DuckyPeriphs.logPrint("modifiers: " + modifiers + " scancode: " + scancode + " key: " + key);
        // need to get char too ?
        // Key GLFW_Key = InputUtil.fromKeyCode(key, scancode);
        // logPrint("keycode: " + key + "| toString: " + GLFW_Key.toString() + "| localized: " + GLFW_Key.getLocalizedText());

        keyData.writeBlockPos(this.handler.pos);
//        DuckyPeriphs.logPrint("sending key press packet: " + key);
        NetworkManager.sendToServer(new Identifier(DuckyPeriphs.MOD_ID, "key_up_packet"), keyData);
        
        pressedKeys.remove(key);
        return super.keyReleased(key, scancode, modifiers);
    }

    // handle our various event checks in here

    @Override
    protected void handledScreenTick(){
        boolean ctrlDownNow = hasControlDown();
        for(int sCut = 0; sCut < 3; sCut++){
            boolean isSKeyPressed = pressedKeys.contains(eventShortcutKeys[sCut]); // use it a few times, don't need to check multiple times
            if(eventChecking[sCut]){ // we're checking for event already
                if(isSKeyPressed && ctrlDownNow){
                    // still down
                    if(Util.getMeasuringTimeMs() - eventsStarted[sCut] > 1000){ // may need to adjust the time it takes ?
                        // ready to fire event
                        eventChecking[sCut] = false;
                        eventsStarted[sCut] = 0;
                        PacketByteBuf eventDataBuf = new PacketByteBuf(Unpooled.buffer());
                        eventDataBuf.writeInt(sCut);
                        // eventDataBuf.writeBlockPos(this.handler.pos);
                        NetworkManager.sendToServer(new Identifier(DuckyPeriphs.MOD_ID, "event_sent_packet"), eventDataBuf);
                    }
                } else {
                    // not down anymore, need to cancel event
                    eventChecking[sCut] = false;
                }
            } else if(isSKeyPressed && ctrlDownNow){ // need to start event checking
                eventChecking[sCut] = true;
                eventsStarted[sCut] = Util.getMeasuringTimeMs();
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        BlockHitResult hit = BlockHitFromScreen.getHit(0, (int)mouseX, (int)mouseY);
        if(client.crosshairTarget instanceof BlockHitResult hit){
            DuckyPeriphs.logPrint("hit: " + hit.getType() + "; pos: " + hit.getBlockPos() + "; side: " + hit.getSide() + "; pos: " + hit.getPos());
            if(client.world == null) return super.mouseClicked(mouseX, mouseY, button);
            BlockState state = client.world.getBlockState(hit.getBlockPos());
            if(state != null && state.getBlock() instanceof MonitorBlock){
                // do stuff
                client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hit);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
