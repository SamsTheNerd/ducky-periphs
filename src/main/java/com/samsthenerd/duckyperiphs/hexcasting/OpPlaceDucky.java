package com.samsthenerd.duckyperiphs.hexcasting;

import java.util.List;

import at.petrak.hexcasting.api.spell.OperationResult;
import at.petrak.hexcasting.api.spell.ParticleSpray;
import at.petrak.hexcasting.api.spell.RenderedSpell;
import at.petrak.hexcasting.api.spell.SpellAction;
import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.eval.SpellContinuation;
import at.petrak.hexcasting.api.spell.iota.Iota;
import kotlin.Triple;
import net.minecraft.text.Text;

public class OpPlaceDucky implements SpellAction{
    @Override
    public int getArgc(){ return 1;}

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
        
    }

    @Override
    public OperationResult operate(SpellContinuation continuation, List<Iota> stack, Iota ravenmind, CastingContext castingContext){
        return SpellAction.DefaultImpls.operate(this, continuation, stack, ravenmind, castingContext);
    }
    
}
