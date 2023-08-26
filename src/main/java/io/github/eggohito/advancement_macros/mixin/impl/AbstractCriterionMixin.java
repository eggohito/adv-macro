package io.github.eggohito.advancement_macros.mixin.impl;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.access.MacroData;
import io.github.eggohito.advancement_macros.access.MacroStorage;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(AbstractCriterion.class)
public abstract class AbstractCriterionMixin<T extends AbstractCriterionConditions> implements MacroContext {

    @Unique
    private final Map<ServerPlayerEntity, TriggerContext> advancement_macros$context = new HashMap<>();

    @Unique
    private void advancement_macros$add(ServerPlayerEntity player, TriggerContext context) {
        advancement_macros$context.put(player, context);
    }

    @Override
    public void advancement_macros$add(ServerPlayerEntity player, Identifier id, Consumer<TriggerContext> contextConsumer) {

        TriggerContext context = TriggerContext.create(id);
        contextConsumer.accept(context);

        advancement_macros$add(player, context);

    }

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void advancement_macros$writeNbtToRewards(ServerPlayerEntity player, Predicate<T> predicate, CallbackInfo ci, @Local Criterion.ConditionsContainer<T> conditionsContainer) {

        //  Skip this mixin if there is no trigger context for the player or if the data of
        //  the trigger context is empty
        TriggerContext context;
        if (!advancement_macros$context.containsKey(player) || (context = advancement_macros$context.get(player)).isEmpty()) {
            return;
        }

        //  Skip this mixin if the criterion trigger ID of the target criterion (the criterion to be granted) is NOT
        //  the same as the criterion trigger ID from the trigger context
        Identifier criterionTriggerId = conditionsContainer.getConditions().getId();
        if (!context.getId().equals(criterionTriggerId)) {
            return;
        }

        //  Get the name of the target criterion and the advancement containing the target criterion
        String targetCriterionName = ((ConditionsContainerAccessor) conditionsContainer).getId();
        Advancement advancement = ((ConditionsContainerAccessor) conditionsContainer).getAdvancement();

        //  Iterate through all the advancement's criteria
        for (Map.Entry<String, AdvancementCriterion> entry : advancement.getCriteria().entrySet()) {

            //  Get the name of the criterion and itself
            String criterionName = entry.getKey();
            AdvancementCriterion criterion = entry.getValue();

            //  Continue looping if the criterion does not have a macro or if its criterion trigger ID does not match
            //  the criterion trigger ID of the target criterion
            Macro macro = ((MacroStorage) criterion).advancement_macros$getMacro();
            if (macro == null || !macro.getId().equals(criterionTriggerId)) {
                continue;
            }

            //  Transform the name of the criterion into a valid function macro name
            String processedName = AdvancementMacros.CRITERION_NAME_REGEX
                .matcher(criterionName)
                .replaceAll("_");

            //  Write the data from the trigger context to the NBT if the name of the criterion matches the name of
            //  the target criterion
            NbtCompound nbt = new NbtCompound();
            if (criterionName.equals(targetCriterionName)) {
                macro.writeToNbt(nbt, context);
                advancement_macros$context.remove(player);
            }

            //  Cache the NBT to the rewards of the advancement. This is regardless of whether the NBT is empty to
            //  work around the issue where if the NBT key does not exist in the NBT compound, the function won't be
            //  called
            ((MacroData) advancement.getRewards()).advancement_macros$getData().put(processedName, nbt);

        }

    }

}
