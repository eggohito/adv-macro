package io.github.eggohito.advancement_macros.mixin.impl;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.access.AdvancementData;
import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.access.MacroStorage;
import io.github.eggohito.advancement_macros.access.PlayerMacroDataTracker;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.PassOrder;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
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

import java.util.LinkedHashMap;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Mixin(AbstractCriterion.class)
public abstract class AbstractCriterionMixin<T extends AbstractCriterionConditions> implements Criterion<T>, MacroContext {

    @Unique
    private TriggerContext advancement_macros$triggerContext;

    @Override
    public void advancement_macros$setContext(Criterion<?> criterion, Consumer<TriggerContext> contextConsumer) {

        //  Get the ID of the passed criterion
        Identifier criterionTriggerId = Criteria.getId(criterion);

        //  Don't set the trigger context if the passed criterion trigger is not registered,
        //  or if the criterion trigger doesn't have any registered macro handlers
        if (criterionTriggerId == null || !AdvancementMacros.REGISTRY.containsId(criterionTriggerId)) {
            return;
        }

        //  Instantiate a trigger context and initialize its values
        TriggerContext context = new TriggerContext(criterionTriggerId);
        contextConsumer.accept(context);

        advancement_macros$triggerContext = context;

    }

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void advancement_macros$writeNbtToRewards(ServerPlayerEntity player, Predicate<T> predicate, CallbackInfo ci, @Local Criterion.ConditionsContainer<T> conditionsContainer, @Local PlayerAdvancementTracker playerAdvancementTracker) {

        //  Skip this method handler if the trigger context of this criterion trigger is either null or empty
        if (advancement_macros$triggerContext == null || advancement_macros$triggerContext.isEmpty()) {
            return;
        }

        //  Get the name of the triggered criterion
        String criterionName = conditionsContainer.id();

        //  Get the advancement and advancement entry of the criterion
        AdvancementEntry advancementEntry = conditionsContainer.advancement();
        Advancement advancement = advancementEntry.value();

        //  Stop early if the criterion that has the criterion name does not somehow exist in the advancement
        if (!advancement.criteria().containsKey(criterionName) || ((AdvancementData) (Object) advancement).advancement_macros$getPassOrder() == PassOrder.NONE) {
            advancement_macros$triggerContext = null;
            return;
        }

        //  Get the actual criterion and the macro from the criterion
        AdvancementCriterion<?> advancementCriterion = advancement.criteria().get(criterionName);
        Macro macro = ((MacroStorage) (Object) advancementCriterion).advancement_macros$getMacro();

        //  Skip this method handler by this point if the macro from the criterion does not exist (e.g: wasn't serialized properly)
        if (macro == null) {
            advancement_macros$triggerContext = null;
            return;
        }

        //  Serialize the data of the criterion trigger to NBT
        NbtCompound criterionNbtData = new NbtCompound();
        macro.writeToNbt(criterionNbtData, advancement_macros$triggerContext);

        //  Directly put the NBT data of the criterion to the rewards of the advancement
        //  if the advancement only has one criterion
        ((PlayerMacroDataTracker) playerAdvancementTracker)
            .advancement_macros$getAll()
            .computeIfAbsent(advancementEntry.id(), id -> new LinkedHashMap<>())
            .put(criterionName, criterionNbtData);

        //  Reset the trigger context to null
        advancement_macros$triggerContext = null;

    }

}
