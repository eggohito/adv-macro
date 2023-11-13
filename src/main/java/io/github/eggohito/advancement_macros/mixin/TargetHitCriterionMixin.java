package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.TargetHitCriterionMacro;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.TargetHitCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TargetHitCriterion.class)
public abstract class TargetHitCriterionMixin extends AbstractCriterion<TargetHitCriterion.Conditions> {

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/TargetHitCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, Entity projectile, Vec3d hitPos, int signalStrength, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$setContext(this, triggerContext -> triggerContext
            .add(TargetHitCriterionMacro.PROJECTILE_KEY, projectile)
            .add(TargetHitCriterionMacro.HIT_LOCATION_KEY, hitPos)
            .add(TargetHitCriterionMacro.SIGNAL_STRENGTH_KEY, signalStrength));
    }

}
