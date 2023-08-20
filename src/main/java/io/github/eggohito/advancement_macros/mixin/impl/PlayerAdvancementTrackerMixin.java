package io.github.eggohito.advancement_macros.mixin.impl;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.eggohito.advancement_macros.access.RewardMacroData;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin implements RewardMacroData {

    @Unique
    private NbtCompound advancement_macros$data;

    @Override
    public void advancement_macros$setData(NbtCompound newData) {
        advancement_macros$data = newData;
    }

    @ModifyExpressionValue(method = "grantCriterion", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/Advancement;getRewards()Lnet/minecraft/advancement/AdvancementRewards;"))
    private AdvancementRewards advancement_macros$passDataToRewards(AdvancementRewards original) {
        ((RewardMacroData) original).advancement_macros$setData(advancement_macros$data);
        return original;
    }

}
