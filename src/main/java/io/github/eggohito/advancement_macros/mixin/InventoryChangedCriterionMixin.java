package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.InventoryChangedCriterionMacro;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryChangedCriterion.class)
public abstract class InventoryChangedCriterionMixin extends AbstractCriterion<InventoryChangedCriterion.Conditions> {

    @Inject(method = "trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/item/ItemStack;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/InventoryChangedCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, PlayerInventory inventory, ItemStack stack, int full, int empty, int occupied, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$add(player, this, triggerContext -> triggerContext
            .add(InventoryChangedCriterionMacro.ITEM_KEY, stack)
            .add(InventoryChangedCriterionMacro.FULL_SLOTS_KEY, full)
            .add(InventoryChangedCriterionMacro.EMPTY_SLOTS_KEY, empty)
            .add(InventoryChangedCriterionMacro.OCCUPIED_SLOTS_KEY, occupied));
    }

}
