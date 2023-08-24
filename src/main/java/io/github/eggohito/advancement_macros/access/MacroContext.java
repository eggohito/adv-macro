package io.github.eggohito.advancement_macros.access;

import net.minecraft.server.network.ServerPlayerEntity;

public interface MacroContext {
    void advancement_macros$add(ServerPlayerEntity player, Object... objects);
}
