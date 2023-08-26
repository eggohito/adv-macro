package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.OnKilledCriterionMacro;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OnKilledCriterion.class)
public abstract class OnKilledCriterionMixin {

    @Shadow
    @Final
    Identifier id;

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/OnKilledCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, Entity entity, DamageSource killingDamage, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$add(player, id, triggerContext -> triggerContext
            .add(OnKilledCriterionMacro.KILLER_KEY_FIELD, player)
            .add(OnKilledCriterionMacro.VICTIM_KEY_FIELD, entity)
            .add(OnKilledCriterionMacro.KILLING_BLOW_KEY_FIELD, killingDamage));
    }

}
