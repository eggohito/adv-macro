package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.ThrownItemPickedUpByEntityCriterionMacro;
import net.minecraft.advancement.criterion.ThrownItemPickedUpByEntityCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownItemPickedUpByEntityCriterion.class)
public abstract class ThrownItemPickedUpByEntityCriterionMixin {

    @Shadow
    @Final
    private Identifier id;

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/ThrownItemPickedUpByEntityCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, ItemStack stack, Entity entity, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$add(player, id, triggerContext -> triggerContext
            .add(ThrownItemPickedUpByEntityCriterionMacro.THROWN_ITEM_KEY_FIELD, stack)
            .add(ThrownItemPickedUpByEntityCriterionMacro.ENTITY_KEY_FIELD, entity));
    }

}
