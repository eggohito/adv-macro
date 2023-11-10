package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.ItemDurabilityChangedCriterionMacro;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.ItemDurabilityChangedCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemDurabilityChangedCriterion.class)
public abstract class ItemDurabilityChangedCriterionMixin extends AbstractCriterion<ItemDurabilityChangedCriterion.Conditions> {

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/ItemDurabilityChangedCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, ItemStack stack, int durability, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$add(player, this, triggerContext -> triggerContext
            .add(ItemDurabilityChangedCriterionMacro.ITEM_KEY_FIELD, stack)
            .add(ItemDurabilityChangedCriterionMacro.DELTA_KEY_FIELD, stack.getDamage() - durability)
            .add(ItemDurabilityChangedCriterionMacro.DURABILITY_KEY_FIELD, stack.getMaxDamage() - durability));
    }

}
