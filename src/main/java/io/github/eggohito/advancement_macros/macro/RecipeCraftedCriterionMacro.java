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
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.List;

public class RecipeCraftedCriterionMacro extends Macro {

    public static final String RECIPE_KEY = "recipe";
    public static final String INGREDIENTS_KEY = "ingredients";

    public static final Codec<RecipeCraftedCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(RECIPE_KEY, RECIPE_KEY).forGetter(RecipeCraftedCriterionMacro::getRecipeKey),
        Codec.STRING.optionalFieldOf(INGREDIENTS_KEY, INGREDIENTS_KEY).forGetter(RecipeCraftedCriterionMacro::getIngredientsKey)
    ).apply(instance, RecipeCraftedCriterionMacro::new));

    private final String recipeKey;
    private final String ingredientsKey;

    public RecipeCraftedCriterionMacro(String recipeKey, String ingredientsKey) {
        super(Criteria.RECIPE_CRAFTED);
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

        context.<List<ItemStack>>ifPresent(INGREDIENTS_KEY, ingredients -> {

           NbtList ingredientsNbt = new NbtList();
           for (ItemStack stack : ingredients) {
               ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack)
                   .resultOrPartial(AdvancementMacros.LOGGER::error)
                   .ifPresent(ingredientsNbt::add);
           }

           rootNbt.put(ingredientsKey, ingredientsNbt);

        });

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.RECIPE_CRAFTED, () -> CODEC);
    }

}
