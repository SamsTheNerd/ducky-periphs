package com.samsthenerd.duckyperiphs.hexcasting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.samsthenerd.duckyperiphs.DuckyPeriph;
import com.samsthenerd.duckyperiphs.ducks.DuckItem;

import at.petrak.hexcasting.api.misc.FrozenColorizer;
import at.petrak.hexcasting.api.misc.MediaConstants;
import at.petrak.hexcasting.api.spell.OperationResult;
import at.petrak.hexcasting.api.spell.OperatorUtils;
import at.petrak.hexcasting.api.spell.ParticleSpray;
import at.petrak.hexcasting.api.spell.RenderedSpell;
import at.petrak.hexcasting.api.spell.SpellAction;
import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation;
import at.petrak.hexcasting.api.spell.iota.Iota;
import at.petrak.hexcasting.api.spell.mishaps.MishapBadBlock;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import kotlin.Triple;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class OpPlaceDucky implements SpellAction{
    private static final Random RANDOM = new Random();
    @Override
    public int getArgc(){ return 2;}

    @Override
    public boolean hasCastingSound(CastingContext context){ return true;}

    @Override
    public boolean awardsCastingStat(CastingContext context){ return true;}

    @Override
    public boolean isGreat(){ return false;}

    @Override
    public boolean getCausesBlindDiversion(){ return false;}

    @Override 
    public boolean getAlwaysProcessGreatSpell(){ return false;}

    @Override
    public Text getDisplayName(){ return Text.translatable("ducky-periphs.spellaction.place_ducky");}

    @Override
    public Triple<RenderedSpell, Integer, List<ParticleSpray>> execute(List<? extends Iota> args, CastingContext context){
        try{
        BlockPos pos = OperatorUtils.getBlockPos(args, 0, getArgc());
        context.assertVecInRange(pos);

        DuckyPeriph.LOGGER.info(context.getCaster().getName().toString() + " casted a duck at " + pos.toShortString());

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

        return new Triple<RenderedSpell, Integer, List<ParticleSpray>>(
            new Spell(pos, 15),
            MediaConstants.DUST_UNIT,
            particles
        );
        } catch (MishapBadBlock e){
            DuckyPeriph.LOGGER.error("Error in casting: " + e.toString());
            return null;
        }
    }

    private class Spell implements RenderedSpell {
        BlockPos pos;
        int light;
        public Spell(BlockPos pos, int light) {
            this.pos = pos;
            this.light = light;
        }

        @Override
        public void cast(CastingContext context) {
            DuckyPeriph.LOGGER.info("hey we're in casting cast casted cool");
            if (!context.canEditBlockAt(pos))
                return;
            
            Block block = DuckyPeriph.DUCK_BLOCK;

            FrozenColorizer colorizer = IXplatAbstractions.INSTANCE.getColorizer(context.getCaster());

            int color = colorizer.getColor(context.getWorld().getTime(), new Vec3d(pos.getX() + RANDOM.nextFloat(), 
                pos.getY() + RANDOM.nextFloat(), pos.getZ() + RANDOM.nextFloat()).multiply(
                RANDOM.nextFloat() * 3));


            ItemPlacementContext placeContext = new ItemPlacementContext(context.getWorld(), context.getCaster(), 
                context.getCastingHand(), DuckItem.getDuckItemStack(color), new BlockHitResult(Vec3d.ofCenter(pos), Direction.UP, pos, false));


            BlockState worldState = context.getWorld().getBlockState(pos);
            if (worldState.canReplace(placeContext)) {

                
                if (!IXplatAbstractions.INSTANCE.isPlacingAllowed(context.getWorld(), pos, DuckItem.getDuckItemStack(color), context.getCaster()))
                    return;

                BlockState state = block.getPlacementState(placeContext);
                if (state != null) {
                    context.getWorld().setBlockState(pos, state);

                    // if (context.getWorld().getBlockState(pos).getBlock() instanceof BlockConjured) {
                    //     BlockConjured.setColor(context.getWorld(), pos, colorizer)
                    // }
                }
            }
        }
    }

    @Override
    public OperationResult operate(SpellContinuation continuation, List<Iota> stack, Iota ravenmind, CastingContext castingContext){
        return SpellAction.DefaultImpls.operate(this, continuation, stack, ravenmind, castingContext);
    }
    
}
