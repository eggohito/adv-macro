package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.EntityHurtPlayerCriterionMacro;
import net.minecraft.advancement.criterion.EntityHurtPlayerCriterion;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityHurtPlayerCriterion.class)
public abstract class EntityHurtPlayerCriterionMixin {

    @Shadow
    @Final
    static Identifier ID;

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/EntityHurtPlayerCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, DamageSource source, float dealt, float taken, boolean blocked, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$add(player, ID, triggerContext -> triggerContext
            .add(EntityHurtPlayerCriterionMacro.DAMAGE_SOURCE_KEY_FIELD, source)
            .add(EntityHurtPlayerCriterionMacro.DAMAGE_DEALT_AMOUNT_KEY_FIELD, dealt)
            .add(EntityHurtPlayerCriterionMacro.DAMAGE_ABSORBED_AMOUNT_KEY_FIELD, taken)
            .add(EntityHurtPlayerCriterionMacro.DAMAGE_BLOCKED_KEY_FIELD, blocked));
    }

}
