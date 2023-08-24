package io.github.eggohito.advancement_macros.util;

import io.github.eggohito.advancement_macros.macro.Macro;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public interface MacroSupplier {
    Pair<Identifier, Macro.Type> getFactory();
}
