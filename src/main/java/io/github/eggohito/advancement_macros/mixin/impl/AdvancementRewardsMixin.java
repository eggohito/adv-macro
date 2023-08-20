package io.github.eggohito.advancement_macros.mixin.impl;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.eggohito.advancement_macros.access.RewardMacroData;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.MacroException;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AdvancementRewards.class)
public abstract class AdvancementRewardsMixin implements RewardMacroData {

    @Unique
    private static NbtCompound advancement_macros$data;

    @Override
    public void advancement_macros$setData(NbtCompound newData) {
        advancement_macros$data = newData;
    }

    @ModifyExpressionValue(method = "method_17978", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/function/CommandFunctionManager;execute(Lnet/minecraft/server/function/CommandFunction;Lnet/minecraft/server/command/ServerCommandSource;)I"))
    private static int advancement_macros$passDataToFunction(int original, MinecraftServer server, ServerPlayerEntity player, CommandFunction function) {
        try {
            return server.getCommandFunctionManager().execute(function, player.getCommandSource().withSilent().withLevel(2), null, advancement_macros$data);
        } catch (MacroException ignored) {
            return original;
        } finally {
            advancement_macros$data = new NbtCompound();
        }
    }

}
