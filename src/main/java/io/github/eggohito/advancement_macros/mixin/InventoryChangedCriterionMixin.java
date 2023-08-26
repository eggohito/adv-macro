package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.MacroContext;
import io.github.eggohito.advancement_macros.macro.InventoryChangedCriterionMacro;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryChangedCriterion.class)
public abstract class InventoryChangedCriterionMixin {

    @Shadow
    @Final
    static Identifier ID;

    @Inject(method = "trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/item/ItemStack;III)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/InventoryChangedCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passContext(ServerPlayerEntity player, PlayerInventory inventory, ItemStack stack, int full, int empty, int occupied, CallbackInfo ci) {
        ((MacroContext) this).advancement_macros$add(player, ID, triggerContext -> triggerContext
            .add(InventoryChangedCriterionMacro.MOVED_ITEM_KEY_FIELD, stack)
            .add(InventoryChangedCriterionMacro.FULL_ITEMS_KEY_FIELD, full)
            .add(InventoryChangedCriterionMacro.EMPTY_SLOTS_KEY_FIELD, empty)
            .add(InventoryChangedCriterionMacro.OCCUPIED_SLOTS_KEY_FIELD, occupied));
    }

}
