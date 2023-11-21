package io.github.eggohito.advancement_macros.mixin.impl;

import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.access.AdvancementData;
import io.github.eggohito.advancement_macros.access.AdvancementRewardsData;
import io.github.eggohito.advancement_macros.access.PlayerMacroDataTracker;
import io.github.eggohito.advancement_macros.util.PassOrder;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.function.Predicate;

@SuppressWarnings("DataFlowIssue")
@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin implements PlayerMacroDataTracker {

    @Unique
    private final Map<AdvancementEntry, Map<String, NbtCompound>> advancement_macros$trackedMacroData = new HashMap<>();

    @Override
    public Map<AdvancementEntry, Map<String, NbtCompound>> advancement_macros$getAll() {
        return advancement_macros$trackedMacroData;
    }

    @Inject(method = "grantCriterion", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/AdvancementRewards;apply(Lnet/minecraft/server/network/ServerPlayerEntity;)V"))
    private void advancement_macros$writeEmptyNbtToRewards(AdvancementEntry advancementEntry, String criterionName, CallbackInfoReturnable<Boolean> cir) {

        Map<String, NbtCompound> macroData = this.advancement_macros$trackedMacroData
            .computeIfAbsent(advancementEntry, id -> new LinkedHashMap<>());

        //  Skip this method handler by this point if there isn't any tracked macro data
        if (macroData.isEmpty()) {
            return;
        }

        //  Get the advancement and its criteria from the advancement entry
        Advancement advancement = advancementEntry.value();
        Map<String, AdvancementCriterion<?>> criteria = advancement.criteria();

        //  Get the specified pass order from the advancement
        PassOrder passOrder = ((AdvancementData) (Object) advancement).advancement_macros$getPassOrder();

        //  If the advancement has more than 1 criterion:
        if (criteria.size() > 1 && passOrder == PassOrder.AUTO) {

            //  Pass an empty NBT compound to other criteria that aren't triggered (e.g: specified as optional). This is to work
            //  around an issue with the reward function failing entirely if the placeholder key doesn't exist
            criteria.keySet()
                .stream()
                .filter(Predicate.not(macroData::containsKey))
                .forEach(name -> macroData.put(name, new NbtCompound()));

        }

        //  Create an NBT compound to pass to the rewards of the advancement
        NbtCompound nbtDataToPass = new NbtCompound();
        List<Map.Entry<String, NbtCompound>> macroDataList = new LinkedList<>(macroData.entrySet());

        //  Reverse the macro data list if the specified pass order is "last"
        //  (meaning the data of the last criterion will be passed)
        if ((criteria.size() > 1 && macroDataList.size() > 1) && passOrder == PassOrder.LAST) {
            Collections.reverse(macroDataList);
        }

        for (Map.Entry<String, NbtCompound> entry : macroDataList) {

            //  Use the data of the criterion directly if the advancement only has one criterion
            //  or if the specified pass order is NOT "auto"
            if (criteria.size() == 1 || passOrder != PassOrder.AUTO) {
                nbtDataToPass = entry.getValue();
                break;
            }

            //  Replace certain characters (e.g: `:`, `.`, `/`, `-`, and ` `) in the name of the
            //  criterion with an underscore
            String processedName = AdvancementMacros.REPLACEABLE_CHARACTERS
                .matcher(entry.getKey())
                .replaceAll("_");

            //  Remove characters that aren't `a` to `z`, `A` to `Z`, `0` to `9`, and `_` from
            //  the name of the criterion
            processedName = AdvancementMacros.INVALID_MACRO_CHARACTERS
                .matcher(processedName)
                .replaceAll("");

            //  Put the data of the criterion to the NBT compound that'll be passed to the function
            nbtDataToPass.put(processedName, entry.getValue());

        }

        //  Pass the NBT compound to the rewards of the advancement and clear the tracked macro data for the advancement
        ((AdvancementRewardsData) advancement.rewards()).advancement_macros$setNbt(nbtDataToPass);
        this.advancement_macros$trackedMacroData.remove(advancementEntry);

    }

}
