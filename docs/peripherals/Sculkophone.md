<img  align="right" width=300 src="images/renders/SculkophonePadded.png" alt="An isometric render of the sculkophone block">

<br clear="center">

<p valign="left"> 
This peripheral works like a more detailed sculk sensor that can tell you the exact sound and distance to the sound.
</p>

<br clear="right">

## Crafting


<img align=center width=400 src="images/recipes/sculkophone_recipe.gif" alt="crafting recipe for the entity detector block. In english reading order the recipe is: stone, any glass pane, stone / observer, eye of ender, observer / stone, redstone, stone">


## Events

<table align=center>
    <tr>
        <th>
            Event Name
        </th>
        <th>
            Description
        </th>
        <th>
            Returns
        </th>
    </tr>
    <tr>
        <td>
            vibration
        </td>
        <td width = 200>
            Fires when the sculkophone detects a vibration
        </td>
        <td width=350>
<pre><code class="language-json">1: 'vibration' - event name
2: string: peripheral name/side,
3: string: vibration event id,
4: double: distance to source

</code></pre>
</td>
    </tr>
</table>

