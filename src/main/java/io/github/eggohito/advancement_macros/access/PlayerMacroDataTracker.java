package io.github.eggohito.advancement_macros.access;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import java.util.Map;

public interface PlayerMacroDataTracker {
    Map<Identifier, Map<String, NbtCompound>> advancement_macros$getAll();
}
