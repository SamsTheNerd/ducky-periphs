package net.fabricmc.duckyperiphs;

import net.fabricmc.duckyperiphs.peripherals.keyboards.KeyboardRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DPRecipeSerializer {
    // register our keyboard recipe
    public static SpecialRecipeSerializer<KeyboardRecipe> KEYBOARD_RECIPE;
    
    static {
        KEYBOARD_RECIPE = register("crafting_special_keyboardcustomization", new SpecialRecipeSerializer<>(KeyboardRecipe::new));
    }

    public static void init(){}

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return (S)Registry.register(Registry.RECIPE_SERIALIZER, new Identifier("ducky-periphs", id), serializer);
    }
    
}
