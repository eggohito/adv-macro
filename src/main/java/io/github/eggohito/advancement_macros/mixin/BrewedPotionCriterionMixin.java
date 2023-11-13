package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.BrewedPotionCriterionMacro;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.BrewedPotionCriterion;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewedPotionCriterion.class)
public abstract class BrewedPotionCriterionMixin extends AbstractCriterion<BrewedPotionCriterion.Conditions> {

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/BrewedPotionCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, Potion potion, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$add(player, this, triggerContext -> triggerContext
            .add(BrewedPotionCriterionMacro.POTION_KEY, Registries.POTION.getId(potion))
            .add(BrewedPotionCriterionMacro.STATUS_EFFECTS_KEY, potion.getEffects()));
    }

}
