package io.github.eggohito.advancement_macros.mixin.impl;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.access.MacroData;
import io.github.eggohito.advancement_macros.access.MacroStorage;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.advancement.criterion.Criteria;
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
    public void advancement_macros$add(ServerPlayerEntity player, Criterion<?> criterion, Consumer<TriggerContext> contextConsumer) {

        TriggerContext context = TriggerContext.create(criterion);
        contextConsumer.accept(context);

        advancement_macros$add(player, context);

    }

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void advancement_macros$writeNbtToRewards(ServerPlayerEntity player, Predicate<T> predicate, CallbackInfo ci, @Local Criterion.ConditionsContainer<T> conditionsContainer) {

        //  Skip this method handler if there is no trigger context for the player or if the data of the trigger context is empty
        TriggerContext context;
        if (!advancement_macros$context.containsKey(player) || (context = advancement_macros$context.get(player)).isEmpty()) {
            return;
        }

        //  Get the name of the target criterion and the advancement containing the target criterion
        String targetCriterionName = conditionsContainer.id();
        AdvancementEntry advancementEntry = conditionsContainer.advancement();

        //  Iterate each criterion of the advancement. Each criterion is processed to work around
        //  the issue where if the NBT key does not exist in the NBT compound, the function won't be called
        advancementEntry.value().criteria().forEach((criterionName, advancementCriterion) -> {

            //  Get the trigger ID and macro of the current criterion
            Identifier criterionTriggerId = Criteria.getId(advancementCriterion.trigger());
            Macro macro = ((MacroStorage) (Object) advancementCriterion).advancement_macros$getMacro();

            //  Skip the current iteration if the criterion trigger ID doesn't match the criterion trigger ID
            //  from the trigger context
            if (macro == null || !macro.getId().equals(criterionTriggerId)) {
                return;
            }

            //  Transform the name of the criterion into a valid function macro
            String processedCriterionName = AdvancementMacros.VALID_CRITERION_NAME_PATTERN
                .matcher(criterionName)
                .replaceAll("_");

            //  Write the data of the trigger context to NBT if the name of the criterion matches the name of the
            //  target criterion
            NbtCompound criterionNbtData = new NbtCompound();
            if (criterionName.equals(targetCriterionName)) {
                macro.writeToNbt(criterionNbtData, context);
                advancement_macros$context.remove(player);
            }

            //  Cache the NBT to the rewards of the advancement
            ((MacroData) advancementEntry.value().rewards()).advancement_macros$getData().put(processedCriterionName, criterionNbtData);

        });

    }

}
