package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.TargetHitCriterionMacro;
import net.minecraft.advancement.criterion.TargetHitCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TargetHitCriterion.class)
public abstract class TargetHitCriterionMixin {

    @Shadow
    @Final
    static Identifier ID;

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/TargetHitCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, Entity projectile, Vec3d hitPos, int signalStrength, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$add(player, ID, triggerContext -> triggerContext
            .add(TargetHitCriterionMacro.PROJECTILE_KEY_FIELD, projectile)
            .add(TargetHitCriterionMacro.HIT_LOCATION_KEY_FIELD, hitPos)
            .add(TargetHitCriterionMacro.SIGNAL_STRENGTH_KEY_FIELD, signalStrength));
    }

}
