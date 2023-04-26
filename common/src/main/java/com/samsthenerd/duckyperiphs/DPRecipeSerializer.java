package com.samsthenerd.duckyperiphs;

import com.samsthenerd.duckyperiphs.peripherals.keyboards.KeyboardRecipe;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DPRecipeSerializer {
    // register our keyboard recipe
    public static RegistrySupplier<SpecialRecipeSerializer<KeyboardRecipe>> KEYBOARD_RECIPE;
    public static Registrar<RecipeSerializer<?> > recipeSerializers = DuckyPeriph.REGISTRIES.get().get(Registry.RECIPE_SERIALIZER_KEY);
    
    static {
        KEYBOARD_RECIPE = register("crafting_special_keyboardcustomization", new SpecialRecipeSerializer<>(KeyboardRecipe::new));
    }

    public static void init(){}

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> RegistrySupplier<S> register(String id, S serializer) {
        return recipeSerializers.register(new Identifier("ducky-periphs", id), () -> serializer);
    }
    
}
