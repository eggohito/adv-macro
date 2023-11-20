package io.github.eggohito.advancement_macros.mixin.impl;

import io.github.eggohito.advancement_macros.access.IdentifiableAdvancementRewards;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AdvancementEntry.class)
public abstract class AdvancementEntryMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private static void advancement_macros$cacheIdToRewards(Identifier id, Advancement advancement, CallbackInfo ci) {
        ((IdentifiableAdvancementRewards) advancement.rewards()).advancement_macros$setId(id);
    }

}
