package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.BeeNestDestroyedCriterionMacro;
import net.minecraft.advancement.criterion.BeeNestDestroyedCriterion;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeeNestDestroyedCriterion.class)
public abstract class BeeNestDestroyedCriterionMixin {

    @Shadow
    @Final
    static Identifier ID;

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/BeeNestDestroyedCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, BlockState state, ItemStack stack, int beeCount, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$add(player, ID, triggerContext -> triggerContext
            .add(BeeNestDestroyedCriterionMacro.BROKEN_BEE_NEST_KEY_FIELD, state)
            .add(BeeNestDestroyedCriterionMacro.TOOL_ITEM_KEY_FIELD, stack)
            .add(BeeNestDestroyedCriterionMacro.BEE_COUNT_KEY_FIELD, beeCount));
    }

}
