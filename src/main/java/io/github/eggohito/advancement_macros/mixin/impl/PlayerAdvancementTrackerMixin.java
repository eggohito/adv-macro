package io.github.eggohito.advancement_macros.mixin.impl;

import io.github.eggohito.advancement_macros.access.RewardMacroData;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin implements RewardMacroData {

    @Inject(method = "grantCriterion", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/AdvancementRewards;apply(Lnet/minecraft/server/network/ServerPlayerEntity;)V", shift = At.Shift.AFTER))
    private void advancement_macros$resetData(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        ((RewardMacroData) advancement).advancement_macros$setData(new NbtCompound());
    }

}
