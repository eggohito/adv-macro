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

    public static final String RECIPE_ID_KEY_FIELD = "recipe_id_key";
    public static final String INGREDIENTS_KEY_FIELD = "ingredients_key";

    public static final Codec<RecipeUnlockedCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(RECIPE_ID_KEY_FIELD, "recipe_id").forGetter(RecipeUnlockedCriterionMacro::getRecipeIdKey),
        Codec.STRING.optionalFieldOf(INGREDIENTS_KEY_FIELD, "ingredients").forGetter(RecipeUnlockedCriterionMacro::getIngredientsKey)
    ).apply(instance, RecipeUnlockedCriterionMacro::new));

    private final String recipeIdKey;
    private final String ingredientsKey;

    public RecipeUnlockedCriterionMacro(String recipeIdKey, String ingredientsKey) {
        super(Criteria.RECIPE_UNLOCKED);
        this.recipeIdKey = recipeIdKey;
        this.ingredientsKey = ingredientsKey;
    }

    public String getRecipeIdKey() {
        return recipeIdKey;
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

        context.<Identifier>ifPresent(RECIPE_ID_KEY_FIELD, recipeId ->
            rootNbt.putString(recipeIdKey, recipeId.toString())
        );

        context.<DefaultedList<Ingredient>>ifPresent(INGREDIENTS_KEY_FIELD, ingredients -> {

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
