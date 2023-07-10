package com.samsthenerd.duckyperiphs.hexcasting.hexal;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;
import com.samsthenerd.duckyperiphs.peripherals.IPeripheralTileDucky;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.Vec3Iota;
import at.petrak.hexcasting.api.utils.NBTHelper;
import at.petrak.hexcasting.common.lib.HexItems;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtLong;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import ram.talia.hexal.api.HexalAPI;
import ram.talia.hexal.api.linkable.ClientLinkableHolder;
import ram.talia.hexal.api.linkable.ILinkable;
import ram.talia.hexal.api.linkable.ILinkable.IRenderCentre;
import ram.talia.hexal.api.linkable.LinkableRegistry.LinkableType;
import ram.talia.hexal.api.linkable.ServerLinkableHolder;

public class FocalLinkBlockEntity extends BlockEntity implements IPeripheralTileDucky, ILinkable, IRenderCentre{
    private FocalLinkPeripheral flPeriph = null;
    private static final double MAX_SQR_LINK_RANGE = 32.0*32.0;
    private FrozenColorizer colorizer = new FrozenColorizer(new ItemStack(HexItems.DYE_COLORIZERS.get(DyeColor.PURPLE)), Util.NIL_UUID);
    private long colorizerTime = 0L;
    @NotNull
    public static final String TAG_COLOURISER = "hexal:colouriser";
    // @NotNull
    // public static final String TAG_COLOURISER_TIME = "hexal:colouriser_time";

    private Random random = Random.create();
    private ServerLinkableHolder linkableHolder;// = this.world != null && !this.world.isClient ? new ServerLinkableHolder(this, (ServerWorld) this.world) : null;

    @Nullable
    private NbtCompound serialisedLinkableHolder;

    private ClientLinkableHolder clientLinkableHolder;// = this.world != null && this.world.isClient ? new ClientLinkableHolder(this, this.world, this.random) : null;

    public FocalLinkBlockEntity(BlockPos pos, BlockState state) {
        super(DuckyHexal.FOCAL_LINK_BLOCK_ENTITY.get(), pos, state);
        flPeriph = new FocalLinkPeripheral(this);
    }

    @Override
    public IPeripheral getPeripheral( @Nonnull Direction side ){
        return flPeriph;
    }

    protected void saveModData(NbtCompound tag){
        ServerLinkableHolder holder = getLinkableHolder();
        if(holder != null)
        NBTHelper.putCompound(tag, "hexal:linkable_holder", holder.writeToNbt());
        NBTHelper.putCompound(tag, TAG_COLOURISER, colorizer.serializeToNBT());
        // NBTHelper.putLong(tag, TAG_COLOURISER_TIME, colorizerTime);
    }

    protected void loadModData(NbtCompound tag){
        if(tag == null) return;
        if (tag.contains("hexal:linkable_holder")) {
            this.serialisedLinkableHolder = tag.getCompound("hexal:linkable_holder");
        }
        if (tag.contains(TAG_COLOURISER)) {
            FrozenColorizer newColorizer = FrozenColorizer.fromNBT(tag.getCompound(TAG_COLOURISER));
            this.setColorizer(newColorizer);
        }

        // if (tag.contains(TAG_COLOURISER_TIME)) {
        //     setTimeColorizer(tag.getLong(TAG_COLOURISER_TIME));
        // }

    }

    @Override
    protected void writeNbt(NbtCompound pTag) {
        this.saveModData(pTag);
    }

    @Override
    public void readNbt(NbtCompound pTag) {
        super.readNbt(pTag);
        this.loadModData(pTag);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound tag = new NbtCompound();
        this.saveModData(tag);
        return tag;
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public void markDirty() {
        if (world != null) {
            if (world.isClient()) {
                // MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
            } else if (world instanceof ServerWorld) {
                ((ServerWorld) world).getChunkManager().markForUpdate(pos);
            }
            super.markDirty();
        }
    }

    @Override
    @NotNull
    public UUID owner() {
        return new UUID(0L, this.pos.asLong());
    }
    
    @Override
    public int currentMediaLevel() {
        return 0;
    }

    // linkable linking stuff:
    @Override
    public void link(@NotNull ILinkable other, boolean linkOther){
        // ILinkable.DefaultImpls.link(this, other, linkOther);
        // markDirty();
        ServerLinkableHolder holder = getLinkableHolder();
        if (holder != null && other != null) {
            holder.link(other, linkOther);
        }
    }

    @Override
    public void unlink(@NotNull ILinkable other, boolean linkOther){
        ILinkable.DefaultImpls.unlink(this, other, linkOther);
        markDirty();
    }

    @Override
    public ILinkable getLinked(int index){
        ServerLinkableHolder holder = getLinkableHolder();
        if (holder != null) {
            return holder.getLinked(index);
        } else {
            DuckyPeriphs.logPrint("null linkable holder");
            return null;
        }
    }

    @Override
    public int getLinkedIndex(@NotNull ILinkable other){
        return ILinkable.DefaultImpls.getLinkedIndex(this, other);
    }

    @Override
    public int numLinked(){
        // return ILinkable.DefaultImpls.numLinked(this);
        ServerLinkableHolder holder = getLinkableHolder();
        if (holder != null) {
            return holder.numLinked();
        }
        return -3;
    }

    // transmitting stuff:

    public void sendIota(int index, @NotNull Iota iota){
        ILinkable other = getLinked(index);
        HexalAPI.LOGGER.debug("sending {} to {}", iota, other);
        if (other != null) {
            other.receiveIota(this, iota);
        }
    }
    
    @Override
    public void checkLinks(){
        // ILinkable.DefaultImpls.checkLinks(this);
        ServerLinkableHolder holder = getLinkableHolder();
        if (holder != null) {
            holder.checkLinks();
        }
    }
    
    @Override
    public void receiveIota(@NotNull ILinkable other, @NotNull Iota iota){
        ILinkable.DefaultImpls.receiveIota(this, other, iota);
        if (!this.world.isClient) {
            flPeriph.receivedIota();
        }
    }
    
    @Override
    public Iota nextReceivedIota(){
        return ILinkable.DefaultImpls.nextReceivedIota(this);
    }
    
    @Override
    public int numRemainingIota(){
        return ILinkable.DefaultImpls.numRemainingIota(this);
    }
    
    @Override
    public void clearReceivedIotas(){
        ILinkable.DefaultImpls.clearReceivedIotas(this);
    }
    
    @Override
    @NotNull
    public List<Iota> allReceivedIotas(){
        return ILinkable.DefaultImpls.allReceivedIotas(this);
    }
    
    
    @Override
    @NotNull
    public List<Text> transmittingTargetReturnDisplay(){
        List<Text> list =  new ArrayList<Text>();
        Text text = Text.literal("Focal Link ").append(pos.toShortString())
            .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(Vec3Iota.TYPE.color())));
        list.add(text);
        return list;
    }


    // boring linkable stuff:

    public NbtElement toTag(){
        return NbtLong.of(this.pos.asLong());
    }


    @Override
    public List<Iota> getAsActionResult(){
        List<Iota> list =  new ArrayList<Iota>();
        list.add(new Vec3Iota(getPosition()));
        return list;
    }

    // need to make this grab from serialized data
    @Override
    @Nullable
    public ServerLinkableHolder getLinkableHolder(){
        ServerLinkableHolder tempLinkableHolder = this.linkableHolder;
        if (tempLinkableHolder == null) {
            if ((this.world instanceof ServerWorld ? (ServerWorld)world : null) != null) {
                tempLinkableHolder = new ServerLinkableHolder((ILinkable)this, (ServerWorld)world);
                if (serialisedLinkableHolder != null) {
                    tempLinkableHolder.readFromNbt(serialisedLinkableHolder);
                    this.serialisedLinkableHolder = null;
                }
            }
        }
        linkableHolder = tempLinkableHolder;
        if(tempLinkableHolder == null)
            DuckyPeriphs.logPrint("linkable holder is null");
        return tempLinkableHolder;
    }

    @Override
    public LinkableType<?,?> getLinkableType(){
        return DuckyHexal.FOCAL_LINKABLE_TYPE;
    }

    @Override
    public double maxSqrLinkRange(){
        return MAX_SQR_LINK_RANGE;
    }

    @Override
    public boolean shouldRemove(){
        return this.removed;
    }

    @Override
    public int canAcceptMedia(@NotNull ILinkable other, int otherMediaLevel){
        return 0;
    }

    @Override
    public void acceptMedia(@NotNull ILinkable other, int otherMediaLevel){
        return;
    }

    @Override
    public Vec3d getPosition(){
        return Vec3d.of(getPos());
    }

    @Override
    public boolean isInRange(@NotNull ILinkable other){
        return getPosition().squaredDistanceTo(other.getPosition()) <= 2*(maxSqrLinkRange()+other.maxSqrLinkRange());
    }

    // render center stuff:
    @Override
    public ClientLinkableHolder getClientLinkableHolder(){
        ClientLinkableHolder tempLinkableHolder = this.clientLinkableHolder;
        if (tempLinkableHolder == null) {
            if (world != null) {
                tempLinkableHolder = new ClientLinkableHolder((IRenderCentre)this, world, random);
            }
        }
        clientLinkableHolder = tempLinkableHolder;
        return tempLinkableHolder;
    }

    @Override
    public void renderLinks(){
        // ILinkable.IRenderCentre.DefaultImpls.renderLinks(this);
        ClientLinkableHolder holder = getClientLinkableHolder();
        if(holder != null){
            holder.renderLinks();
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, FocalLinkBlockEntity fl_tile){
        if(fl_tile == null || world == null)
            return;
        if(world.isClient){
            fl_tile.renderLinks();
        } else {
            fl_tile.checkLinks();
        }
    }

    // is this what this is supposed to do ? i have no idea
    @Override
    @NotNull
    public Vec3d renderCentre(@NotNull ILinkable.IRenderCentre other, boolean recursioning) {
        return Vec3d.ofCenter(getPos());
    }

    @Override
    public FrozenColorizer colouriser(){
        return this.colorizer;
    }

    public final void setColorizer(@NotNull FrozenColorizer newColorizer) {
        this.colorizer = newColorizer;
        markDirty();
    }

    public final void setRGBColorizer(int argb){
        setColorizer(new FrozenColorizer(DuckyHexal.ITEM_RGB_COLORIZER.get().stackFromRGB(argb), owner()));
    }

    // public final void setTimeColorizer(long time){
    //     colorizerTime = time;
    // }

    
}
