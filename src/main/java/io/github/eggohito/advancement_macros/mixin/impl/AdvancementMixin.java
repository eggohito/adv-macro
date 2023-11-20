package io.github.eggohito.advancement_macros.mixin.impl;

import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import io.github.eggohito.advancement_macros.access.AdvancementRewardsData;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Advancement.class)
public abstract class AdvancementMixin {

    @Inject(method = "fromJson", at = @At("RETURN"))
    private static void advancement_macros$cacheIdToRewards(JsonObject json, AdvancementEntityPredicateDeserializer predicateDeserializer, CallbackInfoReturnable<Advancement> cir, @Local AdvancementRewards advancementRewards) {
        ((AdvancementRewardsData) advancementRewards).advancement_macros$setId(predicateDeserializer.getAdvancementId());
    }

}
