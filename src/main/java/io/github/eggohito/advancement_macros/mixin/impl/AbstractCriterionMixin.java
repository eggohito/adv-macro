package io.github.eggohito.advancement_macros.mixin.impl;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.eggohito.advancement_macros.access.RewardMacroData;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(AbstractCriterion.class)
public abstract class AbstractCriterionMixin implements RewardMacroData {

    @Unique
    private NbtCompound advancement_macros$data = new NbtCompound();

    @Override
    public void advancement_macros$setData(NbtCompound newData) {
        advancement_macros$data = newData;
    }

    @Override
    public NbtCompound advancement_macros$getData() {
        return advancement_macros$data;
    }

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/Criterion$ConditionsContainer;grant(Lnet/minecraft/advancement/PlayerAdvancementTracker;)V"))
    private void advancement_macros$passDataToTracker(ServerPlayerEntity player, Predicate<?> predicate, CallbackInfo ci, @Local PlayerAdvancementTracker tracker) {
        ((RewardMacroData) tracker).advancement_macros$setData(advancement_macros$data);
    }

}
