package com.samsthenerd.duckyperiphs.peripherals.WeatherMachine;

import javax.annotation.Nonnull;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;


/*
 * Peripheral Block that can get the weather for you ! 
 * Maybe add limits that it has to be above ground or maybe high up?
 *   -> do later though
 */
public class WeatherMachinePeripheral implements IPeripheral{

    private final WeatherMachineTile wmTile;

    WeatherMachinePeripheral( WeatherMachineTile wmPTile){
        wmTile = wmPTile;
    }

    @Nonnull
    @Override
    public String getType()
    {
        return "weather_machine";
    }

    @Override
    public boolean equals( IPeripheral other )
    {
        return this == other;
    }

    // only returns true if the world is raining and the biome can have rain
    // not sure if it works for mountains/altitudes where it does snow?
    @LuaFunction
    public final boolean isRaining(){
        boolean isPrecipitating = wmTile.getWorld().isRaining();
        return isPrecipitating && wmTile.getWorld().getBiome(wmTile.getPos()).value().getPrecipitation().asString() == "rain";
    }

    @LuaFunction
    public final boolean isThundering(){
        return wmTile.getWorld().isThundering();
    }

    @LuaFunction
    public final String getBiome(){
        // return Biome.getCategory(wmTile.getWorld().getBiome(wmTile.getPos())).toString();
        String dirtyString = wmTile.getWorld().getBiome(wmTile.getPos()).getKey().toString();
        // probably a better way to do this
        String[] stringBits = dirtyString.split("/");
        return stringBits[stringBits.length - 1].replace("]]", "").replace(" ", "");
    }

    @LuaFunction
    public final boolean canSnow(){
        return wmTile.getWorld().getBiome(wmTile.getPos()).value().getPrecipitation().asString() == "snow";
    }

    // again, not sure if this works for mountains/altitudes
    @LuaFunction
    public final boolean isSnowing(){
        boolean isPrecipitating = wmTile.getWorld().isRaining();
        return isPrecipitating && wmTile.getWorld().getBiome(wmTile.getPos()).value().getPrecipitation().asString() == "snow";
    }

    // not really sure what the range on this is, not my business though
    @LuaFunction
    public final float getTemperature(){
        return wmTile.getWorld().getBiome(wmTile.getPos()).value().getTemperature();
    }

    @LuaFunction
    public final boolean isPrecipitating(){
        return wmTile.getWorld().isRaining() && wmTile.getWorld().getBiome(wmTile.getPos()).value().getPrecipitation().asString() != "none";
    }
    
    // just a debugging function so I can see if issue is ticking or updating
    // @LuaFunction
    // public final void updatePanel(){
    //     wmTile.updateFrontStateFull();
    // }
}
