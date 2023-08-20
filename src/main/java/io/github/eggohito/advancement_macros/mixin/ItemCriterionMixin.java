package io.github.eggohito.advancement_macros.mixin;

import io.github.eggohito.advancement_macros.access.RewardMacroData;
import io.github.eggohito.advancement_macros.event.CriteriaTriggerCallback;
import io.github.eggohito.advancement_macros.util.TriggerContext;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.ItemCriterion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemCriterion.class)
public abstract class ItemCriterionMixin extends AbstractCriterion<ItemCriterion.Conditions> {

    @Inject(method = "trigger", at = @At(value = "INVOKE", target = "Lnet/minecraft/advancement/criterion/ItemCriterion;trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Predicate;)V"))
    private void advancement_macros$passDataToAbstract(ServerPlayerEntity player, BlockPos pos, ItemStack stack, CallbackInfo ci) {

        NbtCompound nbt = new NbtCompound();
        TriggerContext context = new TriggerContext(this)
            .addData(stack)
            .addData(pos);

        CriteriaTriggerCallback.EVENT.invoker().writeToNbt(context, nbt);
        ((RewardMacroData) this).advancement_macros$setData(nbt);

    }

}
