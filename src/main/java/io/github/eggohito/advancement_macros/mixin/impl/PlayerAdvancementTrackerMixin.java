package io.github.eggohito.advancement_macros.mixin.impl;

import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.access.AdvancementRewardsData;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public abstract class PlayerAdvancementTrackerMixin {

    @Inject(method = "grantCriterion", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/AdvancementRewards;apply(Lnet/minecraft/server/network/ServerPlayerEntity;)V"))
    private void advancement_macros$writeEmptyNbtToRewards(AdvancementEntry advancementEntry, String criterionName, CallbackInfoReturnable<Boolean> cir) {

        Advancement advancement = advancementEntry.value();
        NbtCompound macroData = ((AdvancementRewardsData) advancement.rewards()).advancement_macros$getNbt();

        //  Skip this method handler by this point if the rewards of the advancement didn't have any data set
        if (macroData.isEmpty()) {
            return;
        }

        //  Pass an empty NBT compound to other criteria that aren't triggered (e.g: specified as optional). This is to work
        //  around an issue with the reward function failing entirely if the placeholder key doesn't exist
        for (String otherCriterionName : advancement.criteria().keySet()) {

            //  Replace certain characters (e.g: `:`, `.`, `/`, `-`) with an underscore
            String processedCriterionName = AdvancementMacros.REPLACEABLE_CHARACTERS
                .matcher(otherCriterionName)
                .replaceAll("_");

            //  Remove characters that aren't `a` to `z`, `A` to `Z`, `0` to `9`, and `_`
            processedCriterionName = AdvancementMacros.INVALID_MACRO_CHARACTERS
                .matcher(processedCriterionName)
                .replaceAll("");

            //  Pass an empty NBT compound to the rewards of the advancement if the other criterion
            //  doesn't have any data set
            if (!macroData.contains(processedCriterionName)) {
                macroData.put(processedCriterionName, new NbtCompound());
            }

        }

    }

}
