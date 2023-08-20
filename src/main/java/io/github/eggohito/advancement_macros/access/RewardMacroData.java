package io.github.eggohito.advancement_macros.access;

import net.minecraft.nbt.NbtCompound;

public interface RewardMacroData {
    void advancement_macros$setData(NbtCompound newData);
    NbtCompound advancement_macros$getData();
}
