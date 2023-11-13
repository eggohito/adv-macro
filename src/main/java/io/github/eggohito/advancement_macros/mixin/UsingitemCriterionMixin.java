package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.UsingItemCriterionMacro;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.UsingItemCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(UsingItemCriterion.class)
public abstract class UsingitemCriterionMixin extends AbstractCriterion<UsingItemCriterion.Conditions> {

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/UsingItemCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, ItemStack stack, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$add(player, this, triggerContext -> triggerContext
            .add(UsingItemCriterionMacro.ITEM_KEY, stack));
    }

}
