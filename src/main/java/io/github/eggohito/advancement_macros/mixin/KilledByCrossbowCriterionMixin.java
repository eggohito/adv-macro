package io.github.eggohito.advancement_macros.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.KilledByCrossbowCriterionMacro;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.KilledByCrossbowCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.Set;

@Mixin(KilledByCrossbowCriterion.class)
public abstract class KilledByCrossbowCriterionMixin extends AbstractCriterion<KilledByCrossbowCriterion.Conditions> {

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/KilledByCrossbowCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, Collection<Entity> piercingKilledEntities, CallbackInfo ci, @Local Set<EntityType<?>> entityTypeSet) {
        ((MacroContext) this).advancement_macros$setContext(this, triggerContext -> triggerContext
            .add(KilledByCrossbowCriterionMacro.UNIQUE_ENTITY_TYPES_KEY, entityTypeSet.size())
            .add(KilledByCrossbowCriterionMacro.VICTIMS_KEY, piercingKilledEntities));
    }

}
