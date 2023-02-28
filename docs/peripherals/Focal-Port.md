<img  align="right" width=300 src="images/renders/FocalPortPadded.png" alt="An isometric render of the focal port block">

<br clear="center">

<p valign="left"> 
This peripheral lets you read and write between <a href="https://modrinth.com/mod/hex-casting/">Hex Casting</a> and ComputerCraft. On the hex casting side this will act just like a focus in an item frame. On the computercraft side you can use readIota() and writeIota() to work with the data.

<b>This block requires Hex Casting to be installed. It will not show up without it.</b>
</p>

<br clear="right">

## Crafting


<img align=center width=400 src="images/recipes/focal_port_recipe.png" alt="crafting recipe for the entity detector block. In english reading order the recipe is: edified wood, charged amethyst, edified wood / amethyst, focus, amethyst / block of slate, advanced computer, block of slate">


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
    <tr id="readIota">
        <td>
            readIota()
        </td>
        <td width = 200>
            Reads in an Iota as a lua object.
        </td>
        <td>
            none
        </td>
        <td width=350>
<pre><code class="language-json">varies depending on the Iota
</code></pre>
</td>
    <tr id="writeIota">
        <td>
            writeIota()
        </td>
        <td width = 200>
            Converts a lua object to an Iota and writes it to the focal port
        </td>
        <td>
            lua object
        </td>
        <td width=350>
<pre><code class="language-json">true or false based on if the write succeeded or not.
</code></pre>
</td>
    </tr>
    <tr id="canWriteIota">
        <td>
            canWriteIota()
        </td>
        <td width = 200>
            Tests if a lua object can successfully be converted to an Iota and written to the focal port.
        </td>
        <td>
            lua object
        </td>
        <td width=350>
<pre><code class="language-json">true or false based on if the write would succeed or not.
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
            new_iota
        </td>
        <td width = 200>
            Fires when the focal port has a new iota written to it from the hex casting side.
        </td>
        <td width=350>
<pre><code class="language-json">1: 'new_iota' - event name
2: string: peripheral name/side
</code></pre>
</td>
    </tr>
</table>

