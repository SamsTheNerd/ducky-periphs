<!-- # Keyboard -->
<img  align="right" width=300 src="images/renders/kb_best_crop.gif" alt="A gif that flashes through keyboards that are customizes with different colors.">

<br clear="center">

<p valign="left"> 
It's a keyboard, you can type on it ! 
<br>
Place it next to a computer or connected as a peripheral and it'll pass your keypresses through to the computer.
<br>
Right click on the keyboard to start typing and press ESC to exit.
</p>

<br clear="right">

## Crafting


<img align=center width=400 src="images/recipes/keyboard_recipe.png" alt="crafting recipe for the keyboard. In english reading order the recipe is: button, button, button / stone, redstone, stone. You can use any type of button">


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
            key
        </td>
        <td width = 200>
            Fires when you press a key on the keyboard
        </td>
        <td width=350>
<pre><code class="language-json">1: 'key' - event name
2: {
    1: key_code - int
    2: repeat - boolean
}
</code></pre>
    </tr>
    <tr>
        <td>
            key_up
        </td>
        <td width = 200>
            Fires when you release a key on the keyboard
        </td>
        <td width=350>
<pre><code class="language-json">1: 'key_up' - event name
2: key_code - int
</code></pre>
</tr>
    <tr>
        <td>
            char
        </td>
        <td width = 200>
            Fires when you type a character on the keyboard (doesn't necessarily line up with key presses)
        </td>
        <td width=350>
<pre><code class="language-json">1: 'char' - event name
2: char - string
</code></pre>
    </tr>
    <tr>
        <td>
            paste
        </td>
        <td width = 200>
            Fires when you paste text through the keyboard
        </td>
        <td width=350>
<pre><code class="language-json">1: 'paste' - event name
2: text - string
</code></pre></tr>
<tr>
        <td>
            terminate
        </td>
        <td width = 200>
            Fires when you use the terminate shortcut
        </td>
        <td width=350>
<pre><code class="language-json">1: 'terminate' - event name
</code></pre></tr>
<tr>
        <td>
            shutdown
        </td>
        <td width = 200>
            Fires when you use the shutdown shortcut
        </td>
        <td width=350>
<pre><code class="language-json">1: 'shutdown' - event name
</code></pre></tr>
<tr>
        <td>
            reboot
        </td>
        <td width = 200>
            Fires when you use the reboot shortcut
        </td>
        <td width=350>
<pre><code class="language-json">1: 'reboot' - event name
</code></pre></tr>
</table>

<br>

## Customization
In the future there will hopefully be a block to make this process simpler, but for now we have this.
<br>
<br>
You can customize your keyboard from any crafting grid. There is a 5x5 grid with the keyboard at the center, you can reach different parts of this grid by placing the keyboard in different slots.

<table align=center>
<tr>
    <td>ESC Key</td>
    <td>Left FN</td>
    <td>Main FN</td>
    <td>Right FN</td>
    <td>Upper Right Control Keys</td>
</tr>
<tr>
    <td>TAB</td>
    <td>WASD</td>
    <td>Number Row</td>
    <td>Backspace</td>
    <td>Upper Side Keys</td>
</tr>
<tr>
    <td>CAPS</td>
    <td>Main</td>
    <td>Keyboard</td>
    <td>Enter</td>
    <td>Mid Side Keys</td>
</tr>
<tr>
    <td>Left Shift</td>
    <td>General Control Keys</td>
    <td>Space Bar</td>
    <td>Arrow Keys</td>
    <td>Right Shift</td>
</tr>
<tr>
    <td>Bottom Left Control Keys</td>
    <td>Case Left</td>
    <td>Case Main</td>
    <td>Case Right</td>
    <td>Bottom Right Control</td>
</tr>
</table>