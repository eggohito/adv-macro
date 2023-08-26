package io.github.eggohito.advancement_macros.access;

import io.github.eggohito.advancement_macros.api.Macro;

public interface MacroStorage {
    Macro advancement_macros$getMacro();
    void advancement_macros$setMacro(Macro newMacro);
}
