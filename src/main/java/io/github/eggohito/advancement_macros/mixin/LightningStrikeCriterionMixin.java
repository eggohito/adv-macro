package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.LightningStrikeCriterionMacro;
import net.minecraft.advancement.criterion.LightningStrikeCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LightningStrikeCriterion.class)
public abstract class LightningStrikeCriterionMixin {

    @Shadow
    @Final
    static Identifier ID;

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/LightningStrikeCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, LightningEntity lightning, List<Entity> bystanders, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$add(player, ID, triggerContext -> triggerContext
            .add(LightningStrikeCriterionMacro.LIGHTNING_KEY_FIELD, lightning)
            .add(LightningStrikeCriterionMacro.BYSTANDERS_KEY_FIELD, bystanders));
    }

}
