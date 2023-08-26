package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.RecipeUnlockedCriterionMacro;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.recipe.Recipe;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeUnlockedCriterion.class)
public abstract class RecipeUnlockedCriterionMixin {

    @Shadow
    @Final
    static Identifier ID;

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/RecipeUnlockedCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, Recipe<?> recipe, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$add(player, ID, triggerContext -> triggerContext
            .add(RecipeUnlockedCriterionMacro.RECIPE_ID_KEY_FIELD, recipe.getId())
            .add(RecipeUnlockedCriterionMacro.INGREDIENTS_KEY_FIELD, recipe.getIngredients()));
    }

}
