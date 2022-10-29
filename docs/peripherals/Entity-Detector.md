<img  align="right" width=350 src="images/renders/EntityDetectorRender.png" alt="An orthographic render of the entity detector block">

<br clear="center">

<p valign="left"> 
<br>
< Insert block description here later >
< Insert block description here later >
< Insert block description here later >
< Insert block description here later >
< Insert block description here later >
< Insert block description here later >
< Insert block description here later >
< Insert block description here later >
< Insert block description here later >
< Insert block description here later >
</p>

<br clear="right">


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
