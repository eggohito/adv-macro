package io.github.eggohito.advancement_macros.util;

import java.util.Locale;

public enum PassOrder {

    NONE,
    AUTO,
    FIRST,
    LAST;

    public static PassOrder fromName(String name) {
        try {
            return PassOrder.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (Exception ignored) {
            throw new IllegalArgumentException("Expected value to be any of FIRST or LAST (case-insensitive.)");
        }
    }

}
