package io.github.eggohito.advancement_macros.mixin.impl;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.access.MacroData;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.MacroException;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@SuppressWarnings("unused")
@Mixin(AdvancementRewards.class)
public abstract class AdvancementRewardsMixin implements MacroData {

    @Unique
    private static NbtCompound advancement_macros$data = new NbtCompound();

    @Override
    public NbtCompound advancement_macros$getData() {

        if (advancement_macros$data == null) {
            advancement_macros$data = new NbtCompound();
        }

        return advancement_macros$data;

    }

    @ModifyExpressionValue(method = "method_17978", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/function/CommandFunctionManager;execute(Lnet/minecraft/server/function/CommandFunction;Lnet/minecraft/server/command/ServerCommandSource;)I"))
    private static int advancement_macros$test(int original, MinecraftServer server, ServerPlayerEntity player, CommandFunction function) {

        if (advancement_macros$data == null) {
            return original;
        }

        int result = 0;
        try {
            result = server
                .getCommandFunctionManager()
                .execute(function, player.getCommandSource().withSilent().withLevel(2), null, advancement_macros$data);
        } catch (MacroException e) {
            AdvancementMacros.LOGGER.error("Error trying to execute reward function \"{}\": {}", function.getId(), e.getMessage().getString());
        }

        advancement_macros$data = null;
        return result;

    }

}
