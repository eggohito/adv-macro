package io.github.eggohito.advancement_macros.mixin.impl;

import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.access.MacroData;
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
        NbtCompound macroData = ((MacroData) advancement.rewards()).advancement_macros$getData(true);

        //  Skip this method handler by this point if the rewards of the advancement didn't have any data set
        if (macroData == null) {
            return;
        }

        //  Pass an empty NBT compound to other criteria that aren't triggered (e.g: specified as optional). This is to work
        //  around an issue with the reward function failing entirely if the placeholder key doesn't exist
        for (String otherCriterionName : advancement.criteria().keySet()) {

            //  Replace all the invalid criterion name characters from the criterion name with "_"
            String processedCriterionName = AdvancementMacros.INVALID_CRITERION_CHARACTERS
                .matcher(otherCriterionName)
                .replaceAll("_");

            //  Pass an empty NBT compound to the rewards of the advancement if the other criterion
            //  doesn't have any data set
            if (!macroData.contains(processedCriterionName)) {
                macroData.put(processedCriterionName, new NbtCompound());
            }

        }

    }

}
