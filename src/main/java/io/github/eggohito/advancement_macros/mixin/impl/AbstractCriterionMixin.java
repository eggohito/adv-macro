package io.github.eggohito.advancement_macros.mixin.impl;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.access.MacroData;
import io.github.eggohito.advancement_macros.access.MacroStorage;
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

import java.util.*;
import java.util.function.Predicate;

@Mixin(AbstractCriterion.class)
public abstract class AbstractCriterionMixin<T extends AbstractCriterionConditions> implements MacroContext {

    @Unique
    private final Map<ServerPlayerEntity, List<Object>> advancement_macros$context = new HashMap<>();

    @Override
    public void advancement_macros$add(ServerPlayerEntity player, Object... objects) {
        advancement_macros$context.put(player, new LinkedList<>(Arrays.asList(objects)));
    }

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void advancement_macros$writeNbtToRewards(ServerPlayerEntity player, Predicate<T> predicate, CallbackInfo ci, @Local Criterion.ConditionsContainer<T> conditionsContainer) {

        List<Object> objects;
        if (!advancement_macros$context.containsKey(player) || (objects = advancement_macros$context.get(player)).isEmpty()) {
            return;
        }

        String criterionName = ((ConditionsContainerAccessor) conditionsContainer).getId();
        Advancement advancement = ((ConditionsContainerAccessor) conditionsContainer).getAdvancement();

        advancement.getCriteria()
            .entrySet()
            .stream()
            .filter(entry -> ((MacroStorage) entry.getValue()).advancement_macros$getMacro() != null)
            .map(entry -> Map.entry(entry.getKey(), ((MacroStorage) entry.getValue()).advancement_macros$getMacro()))
            .forEach(entry -> {

                NbtCompound nbt = new NbtCompound();
                String processedName = AdvancementMacros.CRITERION_NAME_REGEX
                    .matcher(entry.getKey())
                    .replaceAll("_");

                if (entry.getKey().equals(criterionName)) {
                    objects.removeIf(obj -> {
                        entry.getValue().writeToNbt(nbt, obj);
                        return true;
                    });
                }

                ((MacroData) advancement.getRewards()).advancement_macros$getData().put(processedName, nbt);

            });

    }

}
