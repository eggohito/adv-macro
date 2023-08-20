package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.RewardMacroData;
import io.github.eggohito.advancement_macros.util.TriggerContext;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.OnKilledCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OnKilledCriterion.class)
public abstract class OnKilledCriterionMixin extends AbstractCriterion<OnKilledCriterion.Conditions> {

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/OnKilledCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passDataToAbstract(ServerPlayerEntity player, Entity entity, DamageSource killingDamage, CallbackInfo ci) {
        TriggerContext context = new TriggerContext(this)
            .addData(player)
            .addData(entity)
            .addData(killingDamage);
        ((RewardMacroData) this).advancement_macros$setContext(context);
    }

}
