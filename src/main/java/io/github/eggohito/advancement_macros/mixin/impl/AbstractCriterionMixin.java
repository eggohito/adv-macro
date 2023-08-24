package io.github.eggohito.advancement_macros.mixin.impl;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.eggohito.advancement_macros.access.RewardMacroData;
import io.github.eggohito.advancement_macros.event.CriteriaTriggerCallback;
import io.github.eggohito.advancement_macros.util.TriggerContext;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;
import java.util.function.Predicate;

@Mixin(AbstractCriterion.class)
public abstract class AbstractCriterionMixin<T extends AbstractCriterionConditions> implements RewardMacroData {

    @Unique
    private TriggerContext advancement_macros$triggerContext;

    @Override
    public void advancement_macros$setContext(TriggerContext context) {
        this.advancement_macros$triggerContext = context;
    }

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void advancement_macros$passContextToContainer(ServerPlayerEntity player, Predicate<T> predicate, CallbackInfo ci, @Local Criterion.ConditionsContainer<T> container) {

        if (advancement_macros$triggerContext == null || !advancement_macros$triggerContext.getCriterionTriggerId().equals(container.getConditions().getId())) {
            return;
        }

        String containerName = ((ConditionsContainerAccessor) container)
            .getId()
            .toLowerCase(Locale.ROOT)
            .replaceAll("[\\s/-:]", "_");

        Advancement advancement = ((ConditionsContainerAccessor) container).getAdvancement();
        NbtCompound contextNbt = new NbtCompound();

        CriteriaTriggerCallback.EVENT.invoker().writeToNbt(advancement_macros$triggerContext, contextNbt);
        ((RewardMacroData) advancement).advancement_macros$getData().put(containerName, contextNbt);


    }

}
