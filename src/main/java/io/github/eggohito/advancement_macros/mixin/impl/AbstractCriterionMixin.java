package io.github.eggohito.advancement_macros.mixin.impl;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.access.MacroData;
import io.github.eggohito.advancement_macros.access.MacroStorage;
import io.github.eggohito.advancement_macros.macro.Macro;
import io.github.eggohito.advancement_macros.util.TriggerContext;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.function.Predicate;

@Mixin(AbstractCriterion.class)
public abstract class AbstractCriterionMixin<T extends AbstractCriterionConditions> implements MacroContext {

    @Unique
    private final Map<ServerPlayerEntity, TriggerContext> advancement_macros$context = new HashMap<>();

    @Override
    public void advancement_macros$add(ServerPlayerEntity player, TriggerContext context) {
        advancement_macros$context.put(player, context);
    }

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void advancement_macros$writeNbtToRewards(ServerPlayerEntity player, Predicate<T> predicate, CallbackInfo ci, @Local Criterion.ConditionsContainer<T> conditionsContainer) {

        TriggerContext context;
        if (!advancement_macros$context.containsKey(player) || (context = advancement_macros$context.get(player)).isEmpty()) {
            return;
        }

        Identifier conditionsId = conditionsContainer.getConditions().getId();
        if (!context.getId().equals(conditionsId)) {
            return;
        }

        String criterionName = ((ConditionsContainerAccessor) conditionsContainer).getId();
        Advancement advancement = ((ConditionsContainerAccessor) conditionsContainer).getAdvancement();

        advancement.getCriteria()
            .entrySet()
            .stream()
            .filter(entry -> advancement_macros$isValid(entry.getValue(), conditionsContainer))
            .map(entry -> Map.entry(entry.getKey(), ((MacroStorage) entry.getValue()).advancement_macros$getMacro()))
            .forEach(entry -> {

                NbtCompound nbt = new NbtCompound();
                String processedName = AdvancementMacros.CRITERION_NAME_REGEX
                    .matcher(entry.getKey())
                    .replaceAll("_");

                if (entry.getKey().equals(criterionName)) {
                    context.process(nbt, entry.getValue());
                    advancement_macros$context.remove(player);
                }

                ((MacroData) advancement.getRewards()).advancement_macros$getData().put(processedName, nbt);

            });

    }

    @Unique
    private boolean advancement_macros$isValid(AdvancementCriterion advancementCriterion, Criterion.ConditionsContainer<T> conditionsContainer) {

        MacroStorage macroStorage = ((MacroStorage) advancementCriterion);
        Macro macro = macroStorage.advancement_macros$getMacro();

        return macro != null && macro.getId().equals(conditionsContainer.getConditions().getId());

    }

}
