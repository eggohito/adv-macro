package io.github.eggohito.advancement_macros.mixin.impl;

import io.github.eggohito.advancement_macros.AdvancementMacros;
import io.github.eggohito.advancement_macros.access.IdentifiableAdvancementRewards;
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
public abstract class AdvancementRewardsMixin implements MacroData, IdentifiableAdvancementRewards {

    @Unique
    private Identifier advancement_macros$id;

    @Unique
    private NbtCompound advancement_macros$data = new NbtCompound();

    @Override
    public void advancement_macros$setId(Identifier id) {
        this.advancement_macros$id = id;
    }

    @Override
    public NbtCompound advancement_macros$getData() {
        return advancement_macros$data;
    }

    @Override
    public void advancement_macros$setData(NbtCompound data) {
        this.advancement_macros$data = data == null ? new NbtCompound() : data;
    }

    @Shadow
    @Final
    private CommandFunction.LazyContainer function;

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
                .withLevel(2);

            server
                .getCommandFunctionManager()
                .execute(function, source, null, advancement_macros$data);

        } catch (MacroException e) {
            AdvancementMacros.LOGGER.error("Error trying to execute reward function \"{}\" for advancement \"{}\": {}", function.getId(), advancement_macros$id, e.getMessage().getString());
        }

        //  Reset the cached NBT data
        advancement_macros$data = new NbtCompound();
        ci.cancel();

    }

}
