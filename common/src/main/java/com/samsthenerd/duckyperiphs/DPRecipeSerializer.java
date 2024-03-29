package com.samsthenerd.duckyperiphs;

import com.samsthenerd.duckyperiphs.peripherals.keyboards.KeyboardRecipe;

import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class DPRecipeSerializer {
    // register our keyboard recipe
    public static RegistrySupplier<SpecialRecipeSerializer<KeyboardRecipe>> KEYBOARD_RECIPE;
    public static Registrar<RecipeSerializer<?> > recipeSerializers = DuckyPeriphs.REGISTRIES.get().get(RegistryKeys.RECIPE_SERIALIZER);
    
    static {
        KEYBOARD_RECIPE = register("crafting_special_keyboardcustomization", new SpecialRecipeSerializer<>(KeyboardRecipe::new));
    }

    public static void init(){}

    public static <S extends RecipeSerializer<?>, T extends Recipe<?>> RegistrySupplier<S> register(String id, S serializer) {
        return recipeSerializers.register(new Identifier(DuckyPeriphs.MOD_ID, id), () -> serializer);
    }
    
}
