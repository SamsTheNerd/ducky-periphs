package com.samsthenerd.duckyperiphs.peripherals.keyboards;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;
import com.samsthenerd.duckyperiphs.peripherals.IPeripheralTileDucky;

import dan200.computercraft.api.peripheral.IPeripheral;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Nameable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class KeyboardTile extends BlockEntity implements IPeripheralTileDucky, Nameable {
    public KeyboardPeripheral kbPeriph;
    private Text customName;
    KeyCaps keyCaps = new KeyCaps();
    
    public KeyboardTile(BlockEntityType<KeyboardTile> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        kbPeriph = new KeyboardPeripheral(this);
    }

    public KeyboardTile(BlockPos pos, BlockState state) {
        this(DuckyPeriphs.KEYBOARD_TILE.get(), pos, state);
    }

    @Override
    public IPeripheral getPeripheral(@Nonnull Direction side) {
        if(kbPeriph == null)
            kbPeriph = new KeyboardPeripheral(this);
        return kbPeriph;
    }

    public @Nonnull ActionResult onActivate(PlayerEntity player, Hand hand, BlockHitResult hit){
        World world = getWorld();
        
        if( !world.isClient ) MenuRegistry.openExtendedMenu((ServerPlayerEntity) player, 
            new KeyboardScreenHandler.KeyboardScreenHandlerFactory(this), (packetByteBuf -> {
                writeScreenOpeningData((ServerPlayerEntity) player, packetByteBuf);
            }));
        return ActionResult.SUCCESS; 
    }

    @Nonnull
    public ScreenHandler createMenu(int syncId, @Nonnull PlayerInventory inv, @Nonnull PlayerEntity player) {
        return new KeyboardScreenHandler(syncId, inv, this.pos);
    }

    @Override
    public Text getDisplayName(){
       return Text.translatable( getCachedState().getBlock().getTranslationKey() );
    }

    public void setCustomName(Text customName) {
        this.customName = customName;
    }

    @Override
    public Text getName() {
        if (this.customName != null) {
            return this.customName;
        }
        return Text.translatable("block.minecraft.duck"); // i have no idea if this is right ??

    }

    @Override
    @Nullable
    public Text getCustomName() {
        return this.customName;
    }
    
    public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf){
        packetByteBuf.writeBlockPos( getPos() );
    }

    
    public static void keyPress(BlockPos pos, World world, int key, int scancode, int modifiers, Boolean repeat,
                                String pasteText, ServerPlayerEntity player){
//        DuckyPeriphs.logPrint("reached keyPress in world " + world.getRegistryKey().getValue() + " at " + pos.toString() + " with key " + key
//            + " and modifiers: " + modifiers + " with pasteText \"" + pasteText + "\"");

        KeyboardTile kbTileTry = (KeyboardTile) world.getBlockEntity(pos);
        if(kbTileTry != null){
            kbTileTry.kbPeriph.sendKey(key, repeat, player);
            if(!pasteText.isEmpty()) kbTileTry.kbPeriph.sendPaste(pasteText, player);
            // I don't *think* you can select text in cc, so shouldn't be able to copy either?
        }
    }

    public static void keyUp(BlockPos pos, World world, int key, int scancode, int modifiers, ServerPlayerEntity player){
        KeyboardTile kbTileTry = (KeyboardTile) world.getBlockEntity(pos);
        if(kbTileTry != null){
            kbTileTry.kbPeriph.sendKeyUp(key, player);
        }
    }

    public static void charTyped(BlockPos pos, World world, char typedChar, int modifiers, ServerPlayerEntity player){
        KeyboardTile kbTileTry = (KeyboardTile) world.getBlockEntity(pos);
        if(kbTileTry != null){
            if(typedChar != '\0')
                kbTileTry.kbPeriph.sendChar(typedChar, player);
        }
    }

    // for terminate, reboot, shutdown
    public static void handleEvents(BlockPos pos, World world, int event, ServerPlayerEntity player){
        KeyboardTile kbTileTry = (KeyboardTile) world.getBlockEntity(pos);
        if(kbTileTry != null){
            // kbTileTry.kbPeriph.sendEvent(event);
            if (event == 0){
                kbTileTry.kbPeriph.sendTerminate(player);
            }
            if (event == 1){
                kbTileTry.kbPeriph.sendShutdown(player);
            }
            if (event == 2){
                kbTileTry.kbPeriph.sendReboot(player);
            }
        }
    }

    // maybe move into some util class? or not idc - might not even be needed lol
    private static char getChar(int key, int modifiers){
        // check modifiers first
        Boolean shift = (modifiers & 0x1) != 0;
        if((modifiers & 2) == 1){
            return 0;
        }
        // handle numbers first
        if(key >= 48 && key <= 57){
            if(shift){
                return ")!@#$%^&*(".charAt(key-48);
            } else {
                return (char) (key - 48 + '0');
            }
        }
        // handle numpad
        if(key >= 320 && key <= 323){
            return (char) (key - 320 + '0');
        }
        // handle letters
        if(key >= 65 && key <= 90){
            if(shift){ //shift
                return (char) key; //uppercase
            } else {
                return (char) (key + 32); // lowercase
            }
        }
        // handle odd characters - could probably make this into a map of some sort for *speed* so we don't have to go throuh each conditional?
        if(key == 32){ // space
            return ' '; 
        }
        if(key == 39){ // aprostrophe
            return (shift ? '"' : '\'');
        }
        if(key == 44){ // comma/left bracket
            return (shift ? '<' : ',');
        }
        if(key == 45){ // minus/underscore
            return (shift ? '_' : '-');
        }
        if(key == 46){ // period/right bracket
            return (shift ? '>' : '.');
        }
        if(key == 47){ // slash/question mark
            return (shift ? '?' : '/');
        }
        if(key == 59){ // semicolon/colon
            return (shift ? ':' : ';');
        }
        if(key == 61){ // equals/plus
            return (shift ? '+' : '=');
        }
        if(key == 91){ // left bracket
            return (shift ? '{' : '[');
        }
        if(key == 92){ // backslash
            return (shift ? '|' : '\\');
        }
        if(key == 93){ // right bracket
            return (shift ? '}' : ']');
        }
        if(key == 96){ // tilde
            return (shift ? '~' : '`');
        }




        return 0; // no char
    }

    // blah blah blah server stuff goes brrr ( not sure if needed)
    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    
    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains("CustomName", 8)) {
            this.customName = Text.Serializer.fromJson(nbt.getString("CustomName"));
        }
        if(nbt != null){
            NbtCompound subNBT = nbt.getCompound(KeyCaps.KEY_CAP_KEY);
            keyCaps = KeyCaps.fromNBT(subNBT);
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (this.customName != null) {
            nbt.putString("CustomName", Text.Serializer.toJson(this.customName));
        }
        nbt.put(KeyCaps.KEY_CAP_KEY, keyCaps.toSubNBT());
    }
}
