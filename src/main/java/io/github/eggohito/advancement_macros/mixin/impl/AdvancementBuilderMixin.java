package io.github.eggohito.advancement_macros.mixin.impl;

import io.github.eggohito.advancement_macros.access.AdvancementRewardsAccess;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Advancement.Builder.class)
public abstract class AdvancementBuilderMixin {

    @Shadow
    private AdvancementRewards rewards;

    @Inject(method = "build(Lnet/minecraft/util/Identifier;)Lnet/minecraft/advancement/Advancement;", at = @At("RETURN"))
    private void advancement_macros$cacheIdToRewards(Identifier id, CallbackInfoReturnable<Advancement> cir) {
        ((AdvancementRewardsAccess) this.rewards).advancement_macros$setAdvancementId(id);
    }

}
