package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.PlayerHurtEntityCriterionMacro;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.PlayerHurtEntityCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerHurtEntityCriterion.class)
public abstract class PlayerHurtEntityCriterionMixin extends AbstractCriterion<PlayerHurtEntityCriterion.Conditions> {

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/PlayerHurtEntityCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, Entity entity, DamageSource damage, float dealt, float taken, boolean blocked, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$setContext(this, triggerContext -> triggerContext
            .add(PlayerHurtEntityCriterionMacro.ENTITY_KEY, entity)
            .add(PlayerHurtEntityCriterionMacro.DAMAGE_TYPE_KEY, damage)
            .add(PlayerHurtEntityCriterionMacro.DAMAGE_DEALT_KEY, dealt)
            .add(PlayerHurtEntityCriterionMacro.DAMAGE_TAKEN_KEY, taken)
            .add(PlayerHurtEntityCriterionMacro.DAMAGE_BLOCKED_KEY, blocked));
    }

}
