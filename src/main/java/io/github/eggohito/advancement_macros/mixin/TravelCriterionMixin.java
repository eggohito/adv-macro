package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.TravelCriterionMacro;
import net.minecraft.advancement.criterion.TravelCriterion;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TravelCriterion.class)
public abstract class TravelCriterionMixin {

    @Shadow
    @Final
    Identifier id;

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/TravelCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, Vec3d startPos, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$add(player, id, triggerContext -> triggerContext
            .add(TravelCriterionMacro.START_LOCATION_KEY_FIELD, startPos));
    }

}
