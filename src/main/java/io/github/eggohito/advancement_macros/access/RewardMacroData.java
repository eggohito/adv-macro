package io.github.eggohito.advancement_macros.access;

import io.github.eggohito.advancement_macros.util.TriggerContext;
import net.minecraft.nbt.NbtCompound;

public interface RewardMacroData {
    void advancement_macros$setContext(TriggerContext context);
    void advancement_macros$setData(NbtCompound newData);
    NbtCompound advancement_macros$getData();
}
