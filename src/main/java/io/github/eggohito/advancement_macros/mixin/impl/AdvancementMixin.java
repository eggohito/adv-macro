package io.github.eggohito.advancement_macros.mixin.impl;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.eggohito.advancement_macros.access.RewardMacroData;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Advancement.class)
public abstract class AdvancementMixin implements RewardMacroData {

    @Unique
    private NbtCompound advancement_macros$data = new NbtCompound();

    @Override
    public void advancement_macros$setData(NbtCompound newData) {
        this.advancement_macros$data = newData;
    }

    @Override
    public NbtCompound advancement_macros$getData() {
        return this.advancement_macros$data;
    }

    @ModifyReturnValue(method = "getRewards", at = @At("RETURN"))
    private AdvancementRewards advancement_macros$passDataToRewards(AdvancementRewards original) {
        ((RewardMacroData) original).advancement_macros$setData(advancement_macros$data);
        return original;
    }

}
