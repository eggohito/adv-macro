package io.github.eggohito.advancement_macros.access;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public interface AdvancementRewardsData {

    Identifier advancement_macros$getId();
    void advancement_macros$setId(Identifier id);

    NbtCompound advancement_macros$getNbt();
    void advancement_macros$setNbt(NbtCompound nbt);

}
