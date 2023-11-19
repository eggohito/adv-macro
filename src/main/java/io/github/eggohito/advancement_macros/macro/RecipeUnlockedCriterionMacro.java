package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;

public class RecipeUnlockedCriterionMacro extends Macro {

    public static final String RECIPE_KEY = "recipe";
    public static final String INGREDIENTS_KEY = "ingredients";

    public static final Codec<RecipeUnlockedCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(RECIPE_KEY, RECIPE_KEY).forGetter(RecipeUnlockedCriterionMacro::getRecipeKey),
        strictOptionalField(INGREDIENTS_KEY, INGREDIENTS_KEY).forGetter(RecipeUnlockedCriterionMacro::getIngredientsKey)
    ).apply(instance, RecipeUnlockedCriterionMacro::new));

    private final String recipeKey;
    private final String ingredientsKey;

    public RecipeUnlockedCriterionMacro(String recipeKey, String ingredientsKey) {
        super(Criteria.RECIPE_UNLOCKED);
        this.recipeKey = recipeKey;
        this.ingredientsKey = ingredientsKey;
    }

    public String getRecipeKey() {
        return recipeKey;
    }

    public String getIngredientsKey() {
        return ingredientsKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<Identifier>ifPresent(RECIPE_KEY, recipeId ->
            rootNbt.putString(recipeKey, recipeId.toString())
        );

        context.<DefaultedList<Ingredient>>ifPresent(INGREDIENTS_KEY, ingredients -> {

            NbtList ingredientsNbt = new NbtList();
            for (Ingredient ingredient : ingredients) {

                NbtList matchingStacksNbt = new NbtList();
                for (ItemStack matchingStack : ingredient.getMatchingStacks()) {
                    ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, matchingStack)
                        .resultOrPartial(AdvancementMacros.LOGGER::error)
                        .ifPresent(matchingStacksNbt::add);
                }

                ingredientsNbt.add(matchingStacksNbt);

            }

            rootNbt.put(ingredientsKey, ingredientsNbt);

        });

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.RECIPE_UNLOCKED, () -> CODEC);
    }

}
