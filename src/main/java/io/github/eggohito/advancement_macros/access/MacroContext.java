package io.github.eggohito.advancement_macros.access;

import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.Consumer;

public interface MacroContext {
    void advancement_macros$add(ServerPlayerEntity player, Criterion<?> criterion, Consumer<TriggerContext> contextConsumer);
}
