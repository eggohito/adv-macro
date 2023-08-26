package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.PlayerGeneratesContainerLootCriterionMacro;
import net.minecraft.advancement.criterion.PlayerGeneratesContainerLootCriterion;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerGeneratesContainerLootCriterion.class)
public abstract class PlayerGeneratesContainerLootCriterionMixin {

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/PlayerGeneratesContainerLootCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, Identifier id, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$add(player, id, triggerContext -> triggerContext
            .add(PlayerGeneratesContainerLootCriterionMacro.LOOT_TABLE_ID_KEY_FIELD, id));
    }

}
