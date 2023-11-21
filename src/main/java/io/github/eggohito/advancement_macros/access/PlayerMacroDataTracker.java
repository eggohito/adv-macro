package io.github.eggohito.advancement_macros.access;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.nbt.NbtCompound;

import java.util.Map;

public interface PlayerMacroDataTracker {
    Map<AdvancementEntry, Map<String, NbtCompound>> advancement_macros$getAll();
}
