<!-- # Weather Machine -->
<img  align="right" width=300 src="images/renders/WeatherMachineRenderPadded.png" alt="An isometric render of the weather machine block">

<br clear="center">

<p valign="left"> 
This peripheral can be used to get data on the current weather and biome. 
</p>

<br clear="right">

## Crafting


<img align=center width=400 src="images/recipes/weather_machine_recipe.gif" alt="crafting recipe for the weather machine block. In english reading order the recipe is: stone, grass block, stone / any glass pane, observer, empty bucket / stone, redstone, stone">


<br>

## Methods
<br>

<table align=center>
    <tr>
        <th>
            Method
        </th>
        <th width=300>
            Description
        </th>
        <th>
            Arguments
        </th>
        <th>
            Returns
        </th>
    </tr>
    <tr>
        <td>
            isRaining()
        </td>
        <td>
            Returns whether or not it is currently raining
        </td>
        <td>
            none
        </td>
        <td width=350>
            boolean
        </td>
    </tr>
    <tr>
        <td>
            isThundering()
        </td>
        <td>
            Returns whether or not it is currently thundering
        </td>
        <td>
            none
        </td>
        <td width=350>
            boolean
        </td>
    </tr>
    <tr>
        <td>
            getBiome()
        </td>
        <td>
            Returns the biome the block is placed in.
        </td>
        <td>
            none
        </td>
        <td width=350>
            String - "namespace:biomename"
        </td>
    </tr>
    <tr>
        <td>
            canSnow()
        </td>
        <td>
            Returns whether or not it can snow where the block is placed
        </td>
        <td>
            none
        </td>
        <td width=350>
            boolean
        </td>
    </tr>
    <tr>
        <td>
            isSnowing()
        </td>
        <td>
            Returns whether or not it is currently snowing
        </td>
        <td>
            none
        </td>
        <td width=350>
            boolean
        </td>
    </tr>
    <tr>
        <td>
            isPrecipitating()
        </td>
        <td>
            Returns whether or not it is currently precipitating (snowing or raining)
        </td>
        <td>
            none
        </td>
        <td width=350>
            boolean
        </td>
    </tr>
    <tr>
        <td>
            getTemperature()
        </td>
        <td>
            Returns the temperature where the block is placed.
        </td>
        <td>
            none
        </td>
        <td width=350>
            int - 0 being cold, 4 being hottest.
        </td>
    </tr>
</table>

<br>
<br>
<br>
<br>

