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

        //  If there is no stored trigger context or if the trigger context is empty, skip
        TriggerContext context;
        if (!advancement_macros$context.containsKey(player) || (context = advancement_macros$context.get(player)).isEmpty()) {
            return;
        }

        //  If the criterion trigger ID of the target criterion (e.g: the criterion to be granted) is NOT the same
        //  as the criterion trigger ID from the stored context, skip
        Identifier criterionTriggerId = conditionsContainer.getConditions().getId();
        if (!context.getId().equals(criterionTriggerId)) {
            return;
        }

        //  Get the name of the target criterion (e.g: the criterion to be granted) and its corresponding advancement
        String targetCriterionName = ((ConditionsContainerAccessor) conditionsContainer).getId();
        Advancement advancement = ((ConditionsContainerAccessor) conditionsContainer).getAdvancement();

        //  Iterate through all the advancement's criteria
        for (Map.Entry<String, AdvancementCriterion> entry : advancement.getCriteria().entrySet()) {

            //  Get the name of the criterion and itself
            String criterionName = entry.getKey();
            AdvancementCriterion criterion = entry.getValue();

            //  If the criterion does not have a macro or if its criterion trigger ID is not the same as the criterion
            //  trigger of the target criterion, continue the loop
            Macro macro = ((MacroStorage) criterion).advancement_macros$getMacro();
            if (macro == null || !macro.getId().equals(criterionTriggerId)) {
                continue;
            }

            //  Transform the name of the criterion into a valid function macro
            String processedName = AdvancementMacros.CRITERION_NAME_REGEX
                .matcher(criterionName)
                .replaceAll("_");

            //  If the name of the criterion matches the name of the target criterion,
            //  write the data of its macro to NBT
            NbtCompound nbt = new NbtCompound();
            if (criterionName.equals(targetCriterionName)) {
                context.process(nbt, macro);
                advancement_macros$context.remove(player);
            }

            //  Pass the written NBT to the rewards of the advancement
            ((MacroData) advancement.getRewards()).advancement_macros$getData().put(processedName, nbt);

        }

    }

}
