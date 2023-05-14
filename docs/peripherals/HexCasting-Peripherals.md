These are a collection of peripherals for blocks from hex casting. This page has slight spoilers for later game hex casting.

<!--
.d8888. db       .d8b.  d888888b d88888b .d8888.
88'  YP 88      d8' `8b `~~88~~' 88'     88'  YP
`8bo.   88      88ooo88    88    88ooooo `8bo.
  `Y8b. 88      88~~~88    88    88~~~~~   `Y8b.
db   8D 88booo. 88   88    88    88.     db   8D
`8888Y' Y88888P YP   YP    YP    Y88888P `8888Y'


-->

<br>

# Slates

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
    <tr id="readPattern">
        <td>
            readPattern()
        </td>
        <td width = 200>
            Returns the pattern on the slate
        </td>
        <td>
            none
        </td>
        <td width=350>
<pre><code class="language-json">varies depending on the Iota
</code></pre>
</td>
</table>

<!--
.d8888. db   db d88888b db      d88888b
88'  YP 88   88 88'     88      88'
`8bo.   88ooo88 88ooooo 88      88ooo
  `Y8b. 88~~~88 88~~~~~ 88      88~~~
db   8D 88   88 88.     88booo. 88
`8888Y' YP   YP Y88888P Y88888P YP

-->

<br>

# Akashic Bookshelf

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
    <tr id="readShelf">
        <td>
            readShelf()
        </td>
        <td width = 200>
            Returns the pattern and iota from the bookshelf
        </td>
        <td>
            none
        </td>
        <td width=350>
<pre><code class="language-json">{
    "patternKey": pattern table,
    "shelfIota": iota table,
    "shelfIotaType": string
}
</code></pre>
</td>
</tr>
</table>

<!--
d8888b. d88888b  .o88b.  .d88b.  d8888b. d8888b.
88  `8D 88'     d8P  Y8 .8P  Y8. 88  `8D 88  `8D
88oobY' 88ooooo 8P      88    88 88oobY' 88   88
88`8b   88~~~~~ 8b      88    88 88`8b   88   88
88 `88. 88.     Y8b  d8 `8b  d8' 88 `88. 88  .8D
88   YD Y88888P  `Y88P'  `Y88P'  88   YD Y8888D'

-->

<br>

# Akashic Record

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
    <tr id="readRecord">
        <td>
            readRecord()
        </td>
        <td width = 200>
            Returns a table of all the iotas in attached shelves
        </td>
        <td>
            none
        </td>
        <td width=350>
<pre><code class="language-json">[anglesig: {
    "patternKey": pattern table,
    "shelfIota": iota table,
    "shelfIotaType": string
},]
</code></pre>
</td>
</table>

<!--
d888888b .88b  d88. d8888b. d88888b d888888b db    db .d8888.
  `88'   88'YbdP`88 88  `8D 88'     `~~88~~' 88    88 88'  YP
   88    88  88  88 88oodD' 88ooooo    88    88    88 `8bo.
   88    88  88  88 88~~~   88~~~~~    88    88    88   `Y8b.
  .88.   88  88  88 88      88.        88    88b  d88 db   8D
Y888888P YP  YP  YP 88      Y88888P    YP    ~Y8888P' `8888Y'


-->

<br>

# Impetus

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
    <tr id="getMedia">
        <td>
            getMedia()
        </td>
        <td width = 200>
            Returns the amount of dust stored in the impetus
        </td>
        <td>
            none
        </td>
        <td width=350>
<pre><code class="language-json">double
</code></pre>
</td>
    <tr id="getRemainingMediaCapacity">
        <td>
            getRemainingMediaCapacity()
        </td>
        <td width = 200>
            Returns the remaining media dust capacity
        </td>
        <td>
            none
        </td>
        <td width=350>
<pre><code class="language-json">double
</code></pre>
</td>
    </tr>
    <tr id="getLastMishap">
        <td>
            getLastMishap()
        </td>
        <td width = 200>
            Returns the last mishap from the spell circle.
        </td>
        <td>
            none
        </td>
        <td width=350>
<pre><code class="language-json">string: "" if no previous mishap
</code></pre>
</td>
    </tr>
    <tr id="getCaster">
        <td>
            getCaster()
        </td>
        <td width = 200>
            Returns UUID of the caster while circle is active or the bound player of a cleric impetus
        </td>
        <td>
            none
        </td>
        <td width=350>
<pre><code class="language-json">string: player uuid
</code></pre>
</td>
    </tr>
    <tr id="isCasting">
        <td>
            isCasting()
        </td>
        <td width = 200>
            Checks if the circle is active and casting
        </td>
        <td>
            none
        </td>
        <td width=350>
<pre><code class="language-json">boolean
</code></pre>
</td>
    </tr>
    </tr>
    <tr id="activateCircle">
        <td>
            activateCircle()
        </td>
        <td width = 200>
            Activates the circle. Only works on cleric impetus.
        </td>
        <td>
            none
        </td>
        <td width=350>
<pre><code class="language-json">boolean: if it was successful
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
    <tr id="circle_activated">
        <td>
            circle_activated
        </td>
        <td width = 200>
            Fires when the impetus is activated.
        </td>
        <td width=350>
<pre><code class="language-json">1: 'circle_activated' - event name
2: string: peripheral name/side
</code></pre>
</td>
    </tr>
    <tr id="circle_stopped">
        <td>
            circle_stopped
        </td>
        <td width = 200>
            Fires when the impetus is activated.
        </td>
        <td width=350>
<pre><code class="language-json">1: 'circle_stopped' - event name
2: string: peripheral name/side
</code></pre>
</td>
    </tr>
</table>

