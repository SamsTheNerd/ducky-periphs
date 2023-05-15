These are the mappings used between Lua tables and Hex Casting Iotas used for peripherals like the focal port. Properties marked with \^ are given when reading an iota into lua but are not required when writing from a lua table to an iota.

<br>

## Base Hex Iotas

<table align=center>
    <tr>
        <th>
            Iota Type
        </th>
        <th>
            Lua Representation
        </th>
    </tr>
    <tr id="null">
        <td>
            Null
        </td>
        <td width=350>
<pre><code class="language-json">{
    "null": true
}
</code></pre>
</td>
    </tr>
    <tr id="garbage">
        <td>
            Garbage
        </td>
        <td width=350>
<pre><code class="language-json">{
    "garbage": true
}
</code></pre> or any input that can't be parsed as some other iota type.
</td>
    </tr>
    <tr id="double">
        <td>
            Double
        </td>
        <td width=350>
<pre><code class="language-json">double
</code></pre>
</td>
    </tr>
    <tr id="boolean">
        <td>
            Boolean
        </td>
        <td width=350>
<pre><code class="language-json">boolean
</code></pre>
</td>
    </tr>
    <tr id="vector">
        <td>
            Vector
        </td>
        <td width=350>
<pre><code class="language-json">{
    "x": double,
    "y": double,
    "z": double
}
</code></pre>
</td>
    </tr>
    <tr id="list">
        <td>
            List
        </td>
        <td width=350>
        indexed table with non-nil values
</td>
    </tr>
    <tr id="entity">
        <td>
            Entity (except players)
        </td>
        <td width=350>
<pre><code class="language-json">{
    "uuid": string of entity's uuid,
    [^]"name": string
}
</code></pre>
</td>
    </tr>
    <tr id="pattern">
        <td>
            Pattern
        </td>
        <td width=400>
<pre><code class="language-json">{
    "startDir": string of start direction,
    "angles": string of angle signature
}
</code></pre>
</td>
    </tr>
</table>

</br>

## Hexal Iotas

Some hexal iotas are represented as arbitrary UUIDs to prevent arbitrary iota writing. For these you'll get a UUID when you read that iota into lua and you can only write that iota back from lua using that UUID.

<table align=center>
    <tr>
        <th>
            Iota Type
        </th>
        <th>
            Lua Representation
        </th>
    </tr>
    <tr id="iotaType">
        <td>
            "Iota Type"
        </td>
        <td width=400>
<pre><code class="language-json">{
    "iotaType": string
}
</code></pre>
</td>
    </tr>
    <tr id="entityType">
        <td>
            Entity Type
        </td>
        <td width=400>
<pre><code class="language-json">{
    "entityType": string
}
</code></pre>
</td>
    </tr>
    <tr id="gate">
        <td>
            Gate
        </td>
        <td width=400>
<pre><code class="language-json">{
    "gate": string -- arbitrary UUID,
}
</code></pre>
</td>
    </tr>
    <tr id="mote">
        <td>
            Mote
        </td>
        <td width=400>
<pre><code class="language-json">{
    "moteUuid": string -- arbitrary UUID,
    "itemID": string -- id of item in mote,
    [^]"nexusUuid": string
}
</code></pre>
</td>
    </tr>
</table>

## More Iotas Iotas

<table align=center>
    <tr>
        <th>
            Iota Type
        </th>
        <th>
            Lua Representation
        </th>
    </tr>
    <tr id="string">
        <td>
            String
        </td>
        <td width=400>
<pre><code class="language-json">String
</code></pre>
</td>
    </tr>
    <tr id="matrix">
        <td>
            Matrix
        </td>
        <td width=400>
<pre><code class="language-json">{
    "col": double -- num of columns,
    "row": double -- num of rows,
    "matrix": [double] -- with linear indexing (top to bottom then left to right)
}
</code></pre>
</td>
    </tr>
</table>

