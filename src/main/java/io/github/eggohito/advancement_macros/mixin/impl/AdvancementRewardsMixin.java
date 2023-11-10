package io.github.eggohito.advancement_macros.mixin.impl;

import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.access.AdvancementRewardsAccess;
import io.github.eggohito.advancement_macros.access.MacroData;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.MacroException;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(AdvancementRewards.class)
public abstract class AdvancementRewardsMixin implements MacroData, AdvancementRewardsAccess {

    @Shadow
    @Final
    private CommandFunction.LazyContainer function;

    @Unique
    private Identifier advancement_macros$advancementId;

    @Unique
    private NbtCompound advancement_macros$data = new NbtCompound();

    @Override
    public void advancement_macros$setAdvancementId(Identifier id) {
        advancement_macros$advancementId = id;
    }

    @Override
    public NbtCompound advancement_macros$getData() {

        if (advancement_macros$data == null) {
            advancement_macros$data = new NbtCompound();
        }

        return advancement_macros$data;

    }

    @Inject(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/function/CommandFunction$LazyContainer;get(Lnet/minecraft/server/function/CommandFunctionManager;)Ljava/util/Optional;"), cancellable = true)
    private void advancement_macros$overrideFunctionCall(ServerPlayerEntity player, CallbackInfo ci) {

        MinecraftServer server = player.server;
        CommandFunction function = this.function.get(server.getCommandFunctionManager()).orElse(null);

        //  Skip this method handler if there is no cached NBT data or if the function is not specified
        if (advancement_macros$data == null || function == null) {

            //  If the function is null, cancel the target method by this point
            if (function == null) {
                ci.cancel();
            }

            return;

        }

        //  Try to execute the function with the passed NBT data. If it fails, inform the user with a log message
        //  containing the ID of the advancement, the ID of the reward function, and the reason it failed
        try {

            ServerCommandSource source = player.getCommandSource()
                .withSilent()
                .withLevel(server.getFunctionPermissionLevel());

            server
                .getCommandFunctionManager()
                .execute(function, source, null, advancement_macros$data);

        } catch (MacroException e) {
            AdvancementMacros.LOGGER.error("Error trying to execute reward function \"{}\" for advancement \"{}\": {}", function.getId(), advancement_macros$advancementId, e.getMessage().getString());
        }

        //  Reset the cached NBT data
        advancement_macros$data = null;
        ci.cancel();

    }

}
