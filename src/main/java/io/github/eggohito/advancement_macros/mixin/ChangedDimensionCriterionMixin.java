package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.RewardMacroData;
import io.github.eggohito.advancement_macros.util.TriggerContext;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.ChangedDimensionCriterion;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChangedDimensionCriterion.class)
public abstract class ChangedDimensionCriterionMixin extends AbstractCriterion<ChangedDimensionCriterion.Conditions> {

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/ChangedDimensionCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passDataToAbstract(ServerPlayerEntity player, RegistryKey<World> from, RegistryKey<World> to, CallbackInfo ci) {
        TriggerContext context = new TriggerContext(this)
            .addMappedData("to", to)
            .addMappedData("from", from);
        ((RewardMacroData) this).advancement_macros$setContext(context);
    }

}
