package io.github.eggohito.advancement_macros.access;

import io.github.eggohito.advancement_macros.api.TriggerContext;
import net.minecraft.server.network.ServerPlayerEntity;

public interface MacroContext {
    void advancement_macros$add(ServerPlayerEntity player, TriggerContext context);
}
