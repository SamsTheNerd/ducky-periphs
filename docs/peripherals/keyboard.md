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
<br>
<br>
You can put dye in a slot to dye the corresponding keyboard section. You can layer multiple dyes to get a more specific color in the same way that you can for dyeing leather armor. Use a wet sponge or water bucket to remove the dye in a specific section.
<br>
<br>
Some sections overlap and can be overridden by other parts. For example, dyeing the `main` section will dye all keys but dyeing any other overlapping slot will dye that section independently, it won't blend colors with the `main`. 
<br>
The sections that are like this are:
- `Main`: sets the whole top of the keyboard
- `General Control Keys`: Most of the non-character keys such as ESC, enter, arrows, ctrl, alt, etc
- `Main FN`: this dyes the whole function row but you can use the `left/right fn` slots to dye it in thirds.
- `Case Main`: this will dye the whole bottom of the keyboard. Similar to the function row you can use `case left/right` to dye in thirds, however you can also dye it in halves instead by only dyeing `case left` and `case right` and not dying `case main`.

<br>

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

For example, to dye the ESC key you would put the keyboard in the bottom right slot and your dye in the top left slot. 

<!-- <details>
<summary>
Section Images:
</summary> -->
<br>

### Section Images

<br>

<table align=center>
<tr>
    <td><img src="images/renders/section_renders/esc.png"></td>
    <td><img src="images/renders/section_renders/left_fn.png"></td>
    <td><img src="images/renders/section_renders/main_fn.png"></td>
    <td><img src="images/renders/section_renders/right_fn.png"></td>
    <td><img src="images/renders/section_renders/upper_right_control.png"></td>
</tr>
<tr>
    <td><img src="images/renders/section_renders/tab.png"></td>
    <td><img src="images/renders/section_renders/wasd.png"></td>
    <td><img src="images/renders/section_renders/num_row.png"></td>
    <td><img src="images/renders/section_renders/backspace.png"></td>
    <td><img src="images/renders/section_renders/upper_side_keys.png"></td>
</tr>
<tr>
    <td><img src="images/renders/section_renders/caps.png"></td>
    <td><img src="images/renders/section_renders/main.png"></td>
    <td><img src="images/renders/section_renders/base.png"></td>
    <td><img src="images/renders/section_renders/enter.png"></td>
    <td><img src="images/renders/section_renders/mid_side_keys.png"></td>
</tr>
<tr>
    <td><img src="images/renders/section_renders/left_shift.png"></td>
    <td><img src="images/renders/section_renders/general_control.png"></td>
    <td><img src="images/renders/section_renders/space_bar.png"></td>
    <td><img src="images/renders/section_renders/arrows.png"></td>
    <td><img src="images/renders/section_renders/right_shift.png"></td>
</tr>
<tr>
    <td><img src="images/renders/section_renders/bottom_left_controls.png"></td>
    <td><img src="images/renders/section_renders/case_left.png"></td>
    <td><img src="images/renders/section_renders/case_main.png"></td>
    <td><img src="images/renders/section_renders/case_right.png"></td>
    <td><img src="images/renders/section_renders/bottom_right_controls.png"></td>
</tr>
</table>
<!-- </details> -->