package com.samsthenerd.duckyperiphs.hexcasting.hexal;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;
import com.samsthenerd.duckyperiphs.hexcasting.FocalPortBlockEntity;

import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.iota.Vec3Iota;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtLong;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import ram.talia.hexal.api.linkable.ILinkable;
import ram.talia.hexal.api.linkable.LinkableRegistry;
import ram.talia.hexal.api.linkable.LinkableRegistry.LinkableType;

public class DuckyHexal {
    public static RegistrySupplier<ItemRGBColorizer> ITEM_RGB_COLORIZER;
    public static RegistrySupplier<FocalLinkBlock> FOCAL_LINK_BLOCK;
	public static RegistrySupplier<BlockEntityType<FocalLinkBlockEntity>> FOCAL_LINK_BLOCK_ENTITY;
    public static LinkableType<FocalLinkBlockEntity, FocalLinkBlockEntity> FOCAL_LINKABLE_TYPE =
        new LinkableType<FocalLinkBlockEntity,FocalLinkBlockEntity>(new Identifier(DuckyPeriphs.MOD_ID, "focal_linkable_type")) {
            
            private FocalLinkBlockEntity fromTag(@NotNull NbtElement nbt, @NotNull World world){
                BlockPos pos = BlockPos.fromLong(((NbtLong)nbt).longValue());
                BlockEntity be = world.getBlockEntity(pos);
                if(be instanceof FocalLinkBlockEntity){
                    return (FocalLinkBlockEntity)be;
                }
                return null;
            }

            @Override 
            public FocalLinkBlockEntity fromNbt(NbtElement nbt, ServerWorld world){
                return fromTag(nbt, world);
            }

            @Override
            public FocalLinkBlockEntity fromSync(@NotNull NbtElement nbt, @NotNull World world){
                return fromTag(nbt, world);
            }

            // not sure if this is quite right ?
            @Override
            public boolean matchSync(@NotNull ILinkable.IRenderCentre renderCenter, @NotNull NbtElement nbt){
                BlockPos nbtPos = BlockPos.fromLong(((NbtLong)nbt).longValue());
                return ((FocalPortBlockEntity)renderCenter).getPos().equals(nbtPos);
            }

            @Override
            public boolean getCanCast(){
                return false;
            }

            @Override
            public int getIotaPriority(){
                return 0;
            }

            @Override
            public int getCastingContextPriority(){
                return 0;
            }

            @Override
            @Nullable
            // need ServerWorld for this?
            public FocalLinkBlockEntity linkableFromIota(@NotNull Iota iota, ServerWorld world){
                if(iota instanceof Vec3Iota vIota){
                    BlockEntity be = world.getBlockEntity(new BlockPos(vIota.getVec3()));
                    if(be instanceof FocalLinkBlockEntity){
                        return (FocalLinkBlockEntity)be;
                    }
                }
                return null;
            }

            @Override
            @Nullable
            public FocalLinkBlockEntity linkableFromCastingContext(@NotNull CastingContext castingContext){
                // can't cast, ignore this
                return null;
            }

            @Override
            @NotNull
            public NbtElement toNbt(ILinkable linkable){
                return ((FocalLinkBlockEntity)linkable).toTag();
            }

            @Override
            @NotNull
            public NbtElement toSync(ILinkable linkable){
                return ((FocalLinkBlockEntity)linkable).toTag();
            }
        };

    public static void init(){
		// do these registries in here so we can be sure it only happens when hex casting is installed
        ITEM_RGB_COLORIZER = DuckyPeriphs.item("cc_internal_pigment", () -> new ItemRGBColorizer(new Item.Settings()));

		FOCAL_LINK_BLOCK = DuckyPeriphs.blockItem("focal_link_block", 
			() -> new FocalLinkBlock(Block.Settings.of(Material.AMETHYST).hardness((float)1.0).luminance(state -> 5)));
		
		FOCAL_LINK_BLOCK_ENTITY = DuckyPeriphs.blockEntities.register(new Identifier(DuckyPeriphs.MOD_ID, "focal_link_block_entity"), 
			() -> BlockEntityType.Builder.create(FocalLinkBlockEntity::new, FOCAL_LINK_BLOCK.get()).build(null));

        LinkableRegistry.INSTANCE.registerLinkableType(FOCAL_LINKABLE_TYPE);
    }
}
