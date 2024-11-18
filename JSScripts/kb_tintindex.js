var kb_json_in = {
	"credit": "Made with Blockbench",
	"texture_size": [16, 16],
	"textures": {
		"0": "ducky-periphs:block/keyboard/case_base_w_corners",
		"1": "ducky-periphs:block/keyboard/keys_base_full",
		"particle": "ducky-periphs:block/keyboard/case_base_w_corners",
		"ArrowKeys": "ducky-periphs:block/keyboard/keyzones/arrow_keys",
		"BackKeys": "ducky-periphs:block/keyboard/keyzones/back_key",
		"BackslashKeys": "ducky-periphs:block/keyboard/keyzones/backslash_key",
		"CapsKeys": "ducky-periphs:block/keyboard/keyzones/caps_key",
		"EnterKeys": "ducky-periphs:block/keyboard/keyzones/enter_key",
		"ESCKeys": "ducky-periphs:block/keyboard/keyzones/esc_key",
		"GraveKeys": "ducky-periphs:block/keyboard/keyzones/grave_key",
		"LeftFNKeys": "ducky-periphs:block/keyboard/keyzones/left_fn_keys",
		"LShift": "ducky-periphs:block/keyboard/keyzones/lshift",
		"MidFNKeys": "ducky-periphs:block/keyboard/keyzones/mid_fn_keys",
		"MidSideKeys": "ducky-periphs:block/keyboard/keyzones/mid_side_keys",
		"ModifierKeysLeft": "ducky-periphs:block/keyboard/keyzones/modifier_keys_left",
		"ModifierKeysRight": "ducky-periphs:block/keyboard/keyzones/modifier_keys_right",
		"MostKeys": "ducky-periphs:block/keyboard/keyzones/most_keys",
		"NumRow": "ducky-periphs:block/keyboard/keyzones/num_row",
		"RightFNKeys": "ducky-periphs:block/keyboard/keyzones/right_fn_keys",
		"RShift": "ducky-periphs:block/keyboard/keyzones/rshift",
		"ScreenKeys": "ducky-periphs:block/keyboard/keyzones/screen_keys",
		"SpaceKey": "ducky-periphs:block/keyboard/keyzones/space_key",
		"TabKey": "ducky-periphs:block/keyboard/keyzones/tab_key",
		"TopSideKeys": "ducky-periphs:block/keyboard/keyzones/top_side_keys",
		"WASDKeys": "ducky-periphs:block/keyboard/keyzones/wasd_keys"
	},
	"elements": [
		{
			"name": "Case-Right",
			"from": [5, 0, 11],
			"to": [11, 1, 15],
			"faces": {
				"north": {"uv": [4, 8, 10, 10], "texture": "#0"},
				"east": {"uv": [11, 0, 15, 2], "texture": "#0"},
				"south": {"uv": [15, 2, 16, 14], "rotation": 90, "texture": "#0"},
				"west": {"uv": [11, 14, 15, 16], "texture": "#0"},
				"up": {"uv": [15, 14, 11, 2], "rotation": 270, "texture": "#0"},
				"down": {"uv": [11, 14, 15, 2], "rotation": 270, "texture": "#0"}
			}
		},
		{
			"name": "Case-Left",
			"from": [5, 0, 1],
			"to": [11, 1, 5],
			"faces": {
				"north": {"uv": [0, 2, 1, 14], "rotation": 270, "texture": "#0"},
				"east": {"uv": [1, 0, 5, 2], "texture": "#0"},
				"south": {"uv": [14, 8, 20, 10], "texture": "#0"},
				"west": {"uv": [1, 14, 5, 16], "texture": "#0"},
				"up": {"uv": [5, 14, 1, 2], "rotation": 270, "texture": "#0"},
				"down": {"uv": [1, 14, 5, 2], "rotation": 270, "texture": "#0"}
			}
		},
		{
			"name": "Case-Mid-Left",
			"from": [5, 0, 5],
			"to": [11, 1, 8],
			"faces": {
				"north": {"uv": [3, 6, 9, 8], "texture": "#0"},
				"east": {"uv": [5, 0, 8, 2], "texture": "#0"},
				"south": {"uv": [12, 6, 18, 8], "texture": "#0"},
				"west": {"uv": [5, 14, 8, 16], "texture": "#0"},
				"up": {"uv": [8, 14, 5, 2], "rotation": 270, "texture": "#0"},
				"down": {"uv": [5, 14, 8, 2], "rotation": 270, "texture": "#0"}
			}
		},
		{
			"name": "Case-Mid-Right",
			"from": [5, 0, 8],
			"to": [11, 1, 11],
			"faces": {
				"north": {"uv": [3, 6, 9, 8], "texture": "#0"},
				"east": {"uv": [8, 0, 11, 2], "texture": "#0"},
				"south": {"uv": [12, 6, 18, 8], "texture": "#0"},
				"west": {"uv": [8, 14, 11, 16], "texture": "#0"},
				"up": {"uv": [11, 14, 8, 2], "rotation": 270, "texture": "#0"},
				"down": {"uv": [8, 14, 11, 2], "rotation": 270, "texture": "#0"}
			}
		},
		{
			"name": "2DCase Right",
			"from": [4.5, 0, 15.5],
			"to": [11.5, 1, 15.5],
			"faces": {
				"north": {"uv": [0, 1, 1, 15], "rotation": 90, "texture": "#0"},
				"east": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"south": {"uv": [0, 1, 1, 15], "rotation": 90, "texture": "#0"},
				"west": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"up": {"uv": [0, 0, 7, 0], "texture": "#0"},
				"down": {"uv": [0, 0, 7, 0], "texture": "#0"}
			}
		},
		{
			"name": "2DCase Left",
			"from": [4.5, 0, 0.5],
			"to": [11.5, 1, 0.5],
			"faces": {
				"north": {"uv": [0, 1, 1, 15], "rotation": 90, "texture": "#0"},
				"east": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"south": {"uv": [0, 1, 1, 15], "rotation": 90, "texture": "#0"},
				"west": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"up": {"uv": [0, 0, 7, 0], "texture": "#0"},
				"down": {"uv": [0, 0, 7, 0], "texture": "#0"}
			}
		},
		{
			"name": "2DCase Front FR",
			"from": [4.5, 0, 11],
			"to": [4.5, 1, 15.5],
			"faces": {
				"north": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"east": {"uv": [11, 0, 15.5, 2], "texture": "#0"},
				"south": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"west": {"uv": [11, 14, 15.5, 16], "texture": "#0"},
				"up": {"uv": [0, 0, 0, 16], "texture": "#0"},
				"down": {"uv": [0, 0, 0, 16], "texture": "#0"}
			}
		},
		{
			"name": "2DCase Front FL",
			"from": [4.5, 0, 0.5],
			"to": [4.5, 1, 5],
			"faces": {
				"north": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"east": {"uv": [0.5, 0, 5, 2], "texture": "#0"},
				"south": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"west": {"uv": [0.5, 14, 5, 16], "texture": "#0"},
				"up": {"uv": [0, 0, 0, 16], "texture": "#0"},
				"down": {"uv": [0, 0, 0, 16], "texture": "#0"}
			}
		},
		{
			"name": "2DCase Front ML",
			"from": [4.5, 0, 5],
			"to": [4.5, 1, 8],
			"faces": {
				"north": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"east": {"uv": [5, 0, 8, 2], "texture": "#0"},
				"south": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"west": {"uv": [5, 14, 8, 16], "texture": "#0"},
				"up": {"uv": [0, 0, 0, 16], "texture": "#0"},
				"down": {"uv": [0, 0, 0, 16], "texture": "#0"}
			}
		},
		{
			"name": "2DCase Front MR",
			"from": [4.5, 0, 8],
			"to": [4.5, 1, 11],
			"faces": {
				"north": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"east": {"uv": [8, 0, 11, 2], "texture": "#0"},
				"south": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"west": {"uv": [8, 14, 11, 16], "texture": "#0"},
				"up": {"uv": [0, 0, 0, 16], "texture": "#0"},
				"down": {"uv": [0, 0, 0, 16], "texture": "#0"}
			}
		},
		{
			"name": "2DCase Back FR",
			"from": [11.5, 0, 11],
			"to": [11.5, 1, 15.5],
			"faces": {
				"north": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"east": {"uv": [11, 0, 15.5, 2], "texture": "#0"},
				"south": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"west": {"uv": [11, 14, 15.5, 16], "texture": "#0"},
				"up": {"uv": [0, 0, 0, 16], "texture": "#0"},
				"down": {"uv": [0, 0, 0, 16], "texture": "#0"}
			}
		},
		{
			"name": "2DCase Back FL",
			"from": [11.5, 0, 0.5],
			"to": [11.5, 1, 5],
			"faces": {
				"north": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"east": {"uv": [0.5, 0, 5, 2], "texture": "#0"},
				"south": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"west": {"uv": [0.5, 14, 5, 16], "texture": "#0"},
				"up": {"uv": [0, 0, 0, 16], "texture": "#0"},
				"down": {"uv": [0, 0, 0, 16], "texture": "#0"}
			}
		},
		{
			"name": "2DCase Back ML",
			"from": [11.5, 0, 5],
			"to": [11.5, 1, 8],
			"faces": {
				"north": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"east": {"uv": [5, 0, 8, 2], "texture": "#0"},
				"south": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"west": {"uv": [5, 14, 8, 16], "texture": "#0"},
				"up": {"uv": [0, 0, 0, 16], "texture": "#0"},
				"down": {"uv": [0, 0, 0, 16], "texture": "#0"}
			}
		},
		{
			"name": "2DCase Back MR",
			"from": [11.5, 0, 8],
			"to": [11.5, 1, 11],
			"faces": {
				"north": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"east": {"uv": [8, 0, 11, 2], "texture": "#0"},
				"south": {"uv": [0, 0, 0, 2], "texture": "#0"},
				"west": {"uv": [8, 14, 11, 16], "texture": "#0"},
				"up": {"uv": [0, 0, 0, 16], "texture": "#0"},
				"down": {"uv": [0, 0, 0, 16], "texture": "#0"}
			}
		},
		{
			"name": "ArrowKeys",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#ArrowKeys"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#ArrowKeys"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#ArrowKeys"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#ArrowKeys"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#ArrowKeys"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#ArrowKeys"}
			}
		},
		{
			"name": "BackKeys",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#BackKeys"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#BackKeys"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#BackKeys"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#BackKeys"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#BackKeys"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#BackKeys"}
			}
		},
		{
			"name": "BackslashKeys",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#BackslashKeys"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#BackslashKeys"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#BackslashKeys"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#BackslashKeys"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#BackslashKeys"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#BackslashKeys"}
			}
		},
		{
			"name": "CapsKeys",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#CapsKeys"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#CapsKeys"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#CapsKeys"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#CapsKeys"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#CapsKeys"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#CapsKeys"}
			}
		},
		{
			"name": "EnterKeys",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#EnterKeys"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#EnterKeys"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#EnterKeys"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#EnterKeys"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#EnterKeys"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#EnterKeys"}
			}
		},
		{
			"name": "ESCKeys",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#ESCKeys"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#ESCKeys"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#ESCKeys"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#ESCKeys"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#ESCKeys"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#ESCKeys"}
			}
		},
		{
			"name": "GraveKeys",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#GraveKeys"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#GraveKeys"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#GraveKeys"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#GraveKeys"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#GraveKeys"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#GraveKeys"}
			}
		},
		{
			"name": "LeftFNKeys",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#LeftFNKeys"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#LeftFNKeys"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#LeftFNKeys"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#LeftFNKeys"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#LeftFNKeys"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#LeftFNKeys"}
			}
		},
		{
			"name": "RightFNKeys",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#RightFNKeys"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#RightFNKeys"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#RightFNKeys"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#RightFNKeys"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#RightFNKeys"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#RightFNKeys"}
			}
		},
		{
			"name": "LShift",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#LShift"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#LShift"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#LShift"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#LShift"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#LShift"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#LShift"}
			}
		},
		{
			"name": "RShift",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#RShift"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#RShift"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#RShift"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#RShift"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#RShift"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#RShift"}
			}
		},
		{
			"name": "MidFNKeys",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#MidFNKeys"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#MidFNKeys"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#MidFNKeys"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#MidFNKeys"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#MidFNKeys"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#MidFNKeys"}
			}
		},
		{
			"name": "MidSideKeys",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#MidSideKeys"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#MidSideKeys"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#MidSideKeys"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#MidSideKeys"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#MidSideKeys"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#MidSideKeys"}
			}
		},
		{
			"name": "ModifierKeysLeft",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#ModifierKeysLeft"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#ModifierKeysLeft"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#ModifierKeysLeft"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#ModifierKeysLeft"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#ModifierKeysLeft"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#ModifierKeysLeft"}
			}
		},
		{
			"name": "ModifierKeysRight",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#ModifierKeysRight"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#ModifierKeysRight"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#ModifierKeysRight"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#ModifierKeysRight"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#ModifierKeysRight"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#ModifierKeysRight"}
			}
		},
		{
			"name": "MostKeys",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#MostKeys"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#MostKeys"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#MostKeys"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#MostKeys"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#MostKeys"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#MostKeys"}
			}
		},
		{
			"name": "NumRow",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#NumRow"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#NumRow"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#NumRow"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#NumRow"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#NumRow"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#NumRow"}
			}
		},
		{
			"name": "ScreenKeys",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#ScreenKeys"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#ScreenKeys"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#ScreenKeys"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#ScreenKeys"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#ScreenKeys"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#ScreenKeys"}
			}
		},
		{
			"name": "SpaceKey",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#SpaceKey"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#SpaceKey"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#SpaceKey"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#SpaceKey"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#SpaceKey"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#SpaceKey"}
			}
		},
		{
			"name": "TabKey",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#TabKey"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#TabKey"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#TabKey"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#TabKey"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#TabKey"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#TabKey"}
			}
		},
		{
			"name": "TopSideKeys",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#TopSideKeys"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#TopSideKeys"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#TopSideKeys"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#TopSideKeys"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#TopSideKeys"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#TopSideKeys"}
			}
		},
		{
			"name": "WASDKeys",
			"from": [5, 1, 1],
			"to": [11, 2, 15],
			"shade": false,
			"faces": {
				"north": {"uv": [0, 14, 1, 2], "rotation": 90, "texture": "#WASDKeys"},
				"east": {"uv": [15, 0, 1, 2], "texture": "#WASDKeys"},
				"south": {"uv": [15, 14, 16, 2], "rotation": 270, "texture": "#WASDKeys"},
				"west": {"uv": [15, 14, 1, 16], "rotation": 180, "texture": "#WASDKeys"},
				"up": {"uv": [1, 2, 15, 14], "rotation": 90, "texture": "#WASDKeys"},
				"down": {"uv": [20, 0, 26, 28], "texture": "#WASDKeys"}
			}
		}
	],
	"display": {
		"ground": {
			"translation": [0, 4.5, 0]
		},
		"gui": {
			"rotation": [0, 90, 90]
		},
		"fixed": {
			"rotation": [0, -90, 90],
			"translation": [0, 1, -8.75]
		}
	},
	"groups": [
		0,
		1,
		{
			"name": "Case",
			"origin": [0, 0, 0],
			"color": 0,
			"children": [6, 7, 8, 9]
		},
		10,
		11,
		{
			"name": "2DCase Front",
			"origin": [0, 0, 0],
			"color": 0,
			"children": [12, 13, 14, 15]
		},
		{
			"name": "2DCase Back",
			"origin": [0, 0, 0],
			"color": 0,
			"children": [16, 17, 18, 19]
		}
	]
}

// want to add tint index
var TIRef = [];
var kb_json_out = kb_json_in;
    

for(var e = 0; e < kb_json_in.elements.length; e++) {
    var faceNames = Object.keys(kb_json_in.elements[e].faces);
    for(var f = 0; f <faceNames.length; f++) {
        kb_json_out.elements[e].faces[faceNames[f]]["tintindex"] = TIRef.length;
        kb_json_out.elements[e].faces[faceNames[f]]["uv"][1] /= 2
        kb_json_out.elements[e].faces[faceNames[f]]["uv"][3] /= 2
        // console.log(`tintindex for ${e} face(${faceNames[f]}): ` + kb_json_out.elements[e].faces[faceNames[f]].tintindex);
    }
    // console.log(`running ${e} element`);
    TIRef.push(kb_json_in.elements[e].name);
    if(e == kb_json_in.elements.length - 1) {
        console.log(JSON.stringify(TIRef));
        console.log(JSON.stringify(kb_json_out));
    }
}





