<img  align="right" width=300 src="images/renders/EntityDetectorRenderPadded.png" alt="An isometric render of the entity detector block">

<br clear="center">

<p valign="left"> 
This peripheral can be used to get data on nearby entities, including players. Note that all coordinates are returned relative to the entity detector
</p>

<br clear="right">

## Crafting


<img align=center width=400 src="images/recipes/entity_detector_recipe.gif" alt="crafting recipe for the entity detector block. In english reading order the recipe is: stone, any glass pane, stone / observer, eye of ender, observer / stone, redstone, stone">


<br>

## Methods
<br>

<table align=center>
    <tr>
        <th>
            Method
        </th>
        <th>
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
            nearbyEntities()
        </td>
        <td width = 200>
            Gets the entities in a 17x33x17 range around the block
        </td>
        <td>
            none
        </td>
        <td width=350>
<pre><code class="language-json">array: {
  i: {
    "uuid": string,
    "name": string, //entity name
    "isPlayer": bool,
    "type": string,
    // coordinates:
    "x": double,
    "y": double,
    "z": double
  }
}
</code></pre>
</td>
    </tr>
</table>

<br>
<br>
<br>
<br>

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
            newEntity
        </td>
        <td width = 200>
            Fires when an entity spawns or enters the range
        </td>
        <td width=350>
<pre><code class="language-json">1: 'new_entity' - event name
2: string: peripheral name/side,
3: array: {
  i: {
    "uuid": string,
    "name": string, //entity name
    "isPlayer": bool,
    "type": string,
    // coordinates:
    "x": double,
    "y": double,
    "z": double
  }
}

</code></pre>
</td>
    </tr>
    <tr>
        <td>
            removedEntity
        </td>
        <td width = 200>
            Fires when an entity dies or leaves the range
        </td>
        <td width=350>
<pre><code class="language-json">1: 'removed_entity' - event name,
2: string: peripheral name/side,
3: array: {
  i: {
    "uuid": string,
    "name": string, //entity name
    "isPlayer": bool,
    "type": string,
    // coordinates:
    "x": double,
    "y": double,
    "z": double
  }
}

</code></pre>
</td>
    </tr>
</table>

