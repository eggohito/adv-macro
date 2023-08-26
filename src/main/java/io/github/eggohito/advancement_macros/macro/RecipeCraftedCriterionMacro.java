package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.List;

public class RecipeCraftedCriterionMacro extends Macro {

    public static final Codec<RecipeCraftedCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf("recipe_id_key", "recipe_id").forGetter(RecipeCraftedCriterionMacro::getRecipeIdKey),
        Codec.STRING.optionalFieldOf("ingredients_key", "ingredients").forGetter(RecipeCraftedCriterionMacro::getIngredientsKey)
    ).apply(instance, RecipeCraftedCriterionMacro::new));

    private final String recipeIdKey;
    private final String ingredientsKey;

    public RecipeCraftedCriterionMacro(String recipeIdKey, String ingredientsKey) {
        super(Criteria.RECIPE_CRAFTED.getId());
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
    public void writeToNbt(NbtCompound rootNbt, Object object) {

        if (object instanceof Identifier recipeId) {
            rootNbt.putString(recipeIdKey, recipeId.toString());
        }

        if (object instanceof List<?> ingredients) {

            NbtList ingredientsNbt = new NbtList();
            ingredients.stream()
                .filter(obj -> obj instanceof ItemStack)
                .map(obj -> (ItemStack) obj)
                .forEach(stack -> ItemStack.CODEC
                    .encodeStart(NbtOps.INSTANCE, stack)
                    .result()
                    .ifPresent(ingredientsNbt::add));

            rootNbt.put(ingredientsKey, ingredientsNbt);

        }

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.RECIPE_CRAFTED.getId(), () -> CODEC);
    }

}
