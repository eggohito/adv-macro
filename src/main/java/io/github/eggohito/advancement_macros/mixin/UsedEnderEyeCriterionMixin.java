package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.util.TriggerContext;
import net.minecraft.advancement.criterion.UsedEnderEyeCriterion;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(UsedEnderEyeCriterion.class)
public abstract class UsedEnderEyeCriterionMixin {

    @Shadow
    @Final
    static Identifier ID;

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/UsedEnderEyeCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, BlockPos strongholdPos, CallbackInfo ci) {
        TriggerContext context = TriggerContext.create(ID)
            .addData(strongholdPos);
        ((MacroContext) this).advancement_macros$add(player, context);
    }

}
