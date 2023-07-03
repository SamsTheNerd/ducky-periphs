package com.samsthenerd.duckyperiphs.hexcasting.hexal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.samsthenerd.duckyperiphs.peripherals.IPeripheralTileDucky;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.Vec3Iota;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtLong;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import ram.talia.hexal.api.HexalAPI;
import ram.talia.hexal.api.linkable.ClientLinkableHolder;
import ram.talia.hexal.api.linkable.ILinkable;
import ram.talia.hexal.api.linkable.ILinkable.IRenderCentre;
import ram.talia.hexal.api.linkable.LinkableRegistry.LinkableType;
import ram.talia.hexal.api.linkable.ServerLinkableHolder;

public class FocalLinkBlockEntity extends BlockEntity implements IPeripheralTileDucky, ILinkable, IRenderCentre{
    private FocalLinkPeripheral flPeriph = null;
    private static final double MAX_SQR_LINK_RANGE = 32.0*32.0;
    public static ServerWorld storedWorld = null;

    private Random random = Random.create();
    private ServerLinkableHolder linkableHolder = this.world != null && !this.world.isClient ? new ServerLinkableHolder(this, (ServerWorld) this.world) : null;
    private ClientLinkableHolder clientLinkableHolder = this.world != null && this.world.isClient ? new ClientLinkableHolder(this, this.world, this.random) : null;

    public FocalLinkBlockEntity(BlockPos pos, BlockState state) {
        super(DuckyHexal.FOCAL_LINK_BLOCK_ENTITY.get(), pos, state);
        flPeriph = new FocalLinkPeripheral(this);
        verifyLinkableHolder();
    }

    @Override
    public IPeripheral getPeripheral( @Nonnull Direction side ){
        return flPeriph;
    }
    
    private void verifyLinkableHolder(){
        if(this.world == null || this.world.isClient || this.linkableHolder != null){
            return;
        }
        this.linkableHolder = new ServerLinkableHolder(this, (ServerWorld) this.world);
    }


    public void storeWorld(){
        storedWorld = (ServerWorld) this.world;
    }

    // linkable linking stuff:
    @Override
    public void link(@NotNull ILinkable other, boolean linkOther){
        verifyLinkableHolder();
        if (this.world.isClient) {
            // this.clientLinkableHolder.link(other, linkOther);
        } else {
            this.linkableHolder.link(other, linkOther);
        }
    }

    @Override
    public void unlink(@NotNull ILinkable other, boolean linkOther){
        verifyLinkableHolder();
        if (this.world.isClient) {
            // this.clientLinkableHolder.unlink(other, linkOther);
        } else {
            if(this.linkableHolder == null){
                return;
            }
            this.linkableHolder.unlink(other, linkOther);
        }
    }

    @Override
    public ILinkable getLinked(int index){
        verifyLinkableHolder();
        if (linkableHolder == null){
            return null;
        }
        return this.linkableHolder.getLinked(index);
    }

    @Override
    public int getLinkedIndex(@NotNull ILinkable other){
        verifyLinkableHolder();
        if (linkableHolder == null){
            return -1;
        }
        return this.linkableHolder.getLinkedIndex(other);
    }

    @Override
    public int numLinked(){
        verifyLinkableHolder();
        if (linkableHolder == null){
            return 0;
        }
        return this.linkableHolder.numLinked();
    }

    // transmitting stuff:

    public void sendIota(int index, @NotNull Iota iota){
        ILinkable other = getLinked(index);
        HexalAPI.LOGGER.debug("sending {} to {}", iota, other);
        if (other != null) {
            other.receiveIota(iota);
        }
    }
    
    @Override
    public void checkLinks(){
        verifyLinkableHolder();
        if (this.world.isClient) {
            // this.clientLinkableHolder.checkLinks();
        } else {
            this.linkableHolder.checkLinks();
        }
    }
    
    @Override
    public void receiveIota(@NotNull Iota iota){
        verifyLinkableHolder();
        if (!this.world.isClient) {
            this.linkableHolder.receiveIota(iota);
            flPeriph.receivedIota();
        }
    }
    
    @Override
    public Iota nextReceivedIota(){
        verifyLinkableHolder();
        if (this.world.isClient) {
            return null;
        }
        return this.linkableHolder.nextReceivedIota();
    }
    
    @Override
    public int numRemainingIota(){
        verifyLinkableHolder();
        if (this.world.isClient) {
            return 0;
        }
        return this.linkableHolder.numRemainingIota();
    }
    
    @Override
    public void clearReceivedIotas(){
        verifyLinkableHolder();
        if (!this.world.isClient) {
            this.linkableHolder.clearReceivedIotas();
        }
    }
    
    @Override
    @NotNull
    public List<Iota> allReceivedIotas(){
        verifyLinkableHolder();
        if (this.world.isClient) {
            return null;
        }
        return this.linkableHolder.allReceivedIotas();
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

    private NbtElement toTag(){
        return NbtLong.of(this.pos.asLong());
    }

    @Override
    @NotNull
    public NbtElement writeToNbt(){
        return toTag();
    }

    @Override
    @NotNull
    public NbtElement writeToSync(){
        return toTag();
    }


    @Override
    public List<Iota> getAsActionResult(){
        List<Iota> list =  new ArrayList<Iota>();
        list.add(new Vec3Iota(getPosition()));
        return list;
    }

    @Override
    @Nullable
    public ServerLinkableHolder getLinkableHolder(){
        return this.linkableHolder;
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
        return this.clientLinkableHolder;
    }

    @Override
    public void renderLinks(){
        // ILinkable.IRenderCentre.DefaultImpls.renderLinks(this);
        if(this.world.isClient){
            this.clientLinkableHolder.renderLinks();
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
        return FrozenColorizer.DEFAULT.get(); // maybe add more to this later
    }

    
}
