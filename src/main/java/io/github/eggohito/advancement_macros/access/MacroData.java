package io.github.eggohito.advancement_macros.access;

import net.minecraft.nbt.NbtCompound;

public interface MacroData {
    NbtCompound advancement_macros$getData();
    NbtCompound advancement_macros$getData(boolean strict);
}
