package com.samsthenerd.duckyperiphs.hexcasting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.samsthenerd.duckyperiphs.hexcasting.utils.mishapJavaSkillIssues.MishapThrowerWrapper;

import at.petrak.hexcasting.api.casting.OperatorUtils;
import at.petrak.hexcasting.api.casting.ParticleSpray;
import at.petrak.hexcasting.api.casting.RenderedSpell;
import at.petrak.hexcasting.api.casting.castables.SpellAction;
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment;
import at.petrak.hexcasting.api.casting.eval.OperationResult;
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage;
import at.petrak.hexcasting.api.casting.eval.vm.SpellContinuation;
import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.mishaps.Mishap;
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.api.pigment.FrozenPigment;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class OpPlaceDucky implements SpellAction{
    private static final Random RANDOM = new Random();
    @Override
    public int getArgc(){ return 2;}

    @Override
    public boolean hasCastingSound(CastingEnvironment context){ return true;}

    @Override
    public boolean awardsCastingStat(CastingEnvironment context){ return true;}

    @Override
    public Result execute(List<? extends Iota> args, CastingEnvironment context){
        try{
        BlockPos pos = OperatorUtils.getBlockPos(args, 0, getArgc());
        context.assertPosInRange(pos);

        Vec3d dirVec = OperatorUtils.getVec3(args, 1, getArgc());
        Direction dir = Direction.getFacing(dirVec.x, 0, dirVec.z);
        if(dir == Direction.UP || dir == Direction.DOWN){
            dir = Direction.NORTH;
        }



        if (!context.getWorld().canPlayerModifyAt(context.getCaster(), pos))
            return null;

        ItemPlacementContext placeContext = new ItemPlacementContext(context.getWorld(), context.getCaster(), 
            context.getCastingHand(), ItemStack.EMPTY, new BlockHitResult(Vec3d.ofCenter(pos), Direction.UP, pos, false));

        BlockState worldState = context.getWorld().getBlockState(pos);
        if (!worldState.canReplace(placeContext))
            // return null;
            throw MishapBadBlock.of(pos, "replaceable");

        List<ParticleSpray> particles = new ArrayList<>();
        particles.add(ParticleSpray.cloud(Vec3d.ofCenter(pos), 1.0, 1));

        return new Result( new Spell(pos, dir), MediaConstants.DUST_UNIT, particles, 0);

        } catch (Mishap e){
            MishapThrowerWrapper.throwMishap(e);
        }
        return null; // should never reach here. just java - kotlin skill issue-ing
    }

    private class Spell implements RenderedSpell {
        BlockPos pos;
        Direction direction;
        public Spell(BlockPos pos, Direction direction) {
            this.pos = pos;
            this.direction = direction;
        }

        @Nullable
        public CastingImage cast(@NotNull CastingEnvironment env, @NotNull CastingImage image) {
            return DefaultImpls.cast(this, env, image);
        }

        @Override
        public void cast(CastingEnvironment context) {
            if (!context.canEditBlockAt(pos))
                return;
            
            Block block = DuckyCasting.CONJURED_DUCKY_BLOCK.get();

            FrozenPigment colorizer = IXplatAbstractions.INSTANCE.getPigment(context.getCaster());

            ItemStack duckyItemStack = new ItemStack(DuckyCasting.CONJURED_DUCKY_BLOCK.get().asItem());

            ItemPlacementContext placeContext = new ItemPlacementContext(context.getWorld(), context.getCaster(), 
                context.getCastingHand(), duckyItemStack, new BlockHitResult(Vec3d.ofCenter(pos), Direction.UP, pos, false));


            BlockState worldState = context.getWorld().getBlockState(pos);
            if (worldState.canReplace(placeContext)) {
                if (!IXplatAbstractions.INSTANCE.isPlacingAllowed(context.getWorld(), pos, new ItemStack(DuckyCasting.CONJURED_DUCKY_BLOCK.get().asItem()), context.getCaster()))
                    return;

                BlockState state = block.getPlacementState(placeContext).with(ConjuredDuckyBlock.FACING, direction);
                ConjuredDuckyBlock.quack(context.getWorld(), pos);
                if (state != null) {
                    context.getWorld().setBlockState(pos, state);

                    if (context.getWorld().getBlockState(pos).getBlock() instanceof ConjuredDuckyBlock) {
                        ConjuredDuckyBlock.setColor(context.getWorld(), pos, colorizer);
                    }
                }
            }
        }
    }

    @Override
    public OperationResult operate(CastingEnvironment env, CastingImage image, SpellContinuation continuation){
        return SpellAction.DefaultImpls.operate(this, env, image, continuation);
    }
    
    @NotNull
    public SpellAction.Result executeWithUserdata(@NotNull List<? extends Iota> args, @NotNull CastingEnvironment env, @NotNull NbtCompound userData) {
        return DefaultImpls.executeWithUserdata(this, args, env, userData);
    }
}
