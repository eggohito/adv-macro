package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.ItemCriterionMacro;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemCriterion.class)
public abstract class ItemCriterionMixin {

    @Shadow
    @Final
    Identifier id;

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/ItemCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, BlockPos pos, ItemStack stack, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$add(player, id, triggerContext -> triggerContext
            .add(ItemCriterionMacro.LOCATION_KEY_FIELD, pos)
            .add(ItemCriterionMacro.ITEM_KEY_FIELD, stack));
    }

}
