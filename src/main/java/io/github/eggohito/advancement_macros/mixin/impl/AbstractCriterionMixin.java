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

import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(AbstractCriterion.class)
public abstract class AbstractCriterionMixin<T extends AbstractCriterionConditions> implements Criterion<T>, MacroContext {

    @Unique
    private TriggerContext advancement_macros$triggerContext;

    @Override
    public void advancement_macros$setContext(Criterion<?> criterion, Consumer<TriggerContext> contextConsumer) {

        TriggerContext context = TriggerContext.create(criterion);
        contextConsumer.accept(context);

        advancement_macros$triggerContext = context;

    }

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void advancement_macros$writeNbtToRewards(ServerPlayerEntity player, Predicate<T> predicate, CallbackInfo ci, @Local Criterion.ConditionsContainer<T> conditionsContainer) {

        //  Skip this method handler if the trigger context of this criterion trigger is either null or empty
        if (advancement_macros$triggerContext == null || advancement_macros$triggerContext.isEmpty()) {
            return;
        }

        //  Get the name of the triggered criterion
        String criterionName = conditionsContainer.id();

        //  Get the advancement and advancement entry of the criterion
        AdvancementEntry advancementEntry = conditionsContainer.advancement();
        Advancement advancement = advancementEntry.value();

        //  Get the actual criterion from the criterion
        AdvancementCriterion<?> advancementCriterion = advancement.criteria().get(criterionName);

        //  Skip this method handler if the actual criterion of the criterion is null (it shouldn't be)
        if (advancementCriterion == null) {
            return;
        }

        //  Get the macro from the actual criterion and the ID of its trigger
        Macro macro = ((MacroStorage) (Object) advancementCriterion).advancement_macros$getMacro();
        Identifier criterionTriggerId = Criteria.getId(advancementCriterion.trigger());

        //  Skip the method handler by this point if the macro handler from the criterion is somehow null
        if (macro == null) {

            AdvancementMacros.LOGGER.warn("Couldn't serialize trigger context of criterion \"{}\" from advancement \"{}\" to NBT due to missing macro handler! (criterion trigger ID: \"{}\")", criterionName, advancementEntry.id(), criterionTriggerId);
            advancement_macros$triggerContext = null;

            return;

        }

        //  Skip the method handler by this point if the ID of the macro handler from the criterion doesn't
        //  match the ID of the criterion trigger
        if (!macro.getId().equals(criterionTriggerId)) {

            AdvancementMacros.LOGGER.warn("Couldn't serialize trigger context of criterion \"{}\" from advancement \"{}\" to NBT due to ID mismatch! (criterion trigger ID: \"{}\", macro handler ID: \"{}\")", criterionName, advancementEntry.id(), criterionTriggerId, macro.getId());
            advancement_macros$triggerContext = null;

            return;

        }

        //  Replace all the invalid criterion name characters from the criterion name with "_"
        String processedCriterionName = AdvancementMacros.INVALID_CRITERION_CHARACTERS
            .matcher(criterionName)
            .replaceAll("_");

        //  Serialize the data of the criterion trigger to NBT
        NbtCompound criterionNbtData = new NbtCompound();
        macro.writeToNbt(criterionNbtData, advancement_macros$triggerContext);

        //  Pass the serialized NBT data to the rewards of the advancement
        //  that contains the criterion
        ((MacroData) advancement.rewards())
            .advancement_macros$getData()
            .put(processedCriterionName, criterionNbtData);

        //  Reset the trigger context to null
        advancement_macros$triggerContext = null;

    }

}
