package io.github.eggohito.advancement_macros.access;

import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public interface MacroContext {
    void advancement_macros$add(ServerPlayerEntity player, Identifier id, Consumer<TriggerContext> contextConsumer);
}
