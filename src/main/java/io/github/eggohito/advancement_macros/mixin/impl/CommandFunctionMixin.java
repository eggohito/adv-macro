package io.github.eggohito.advancement_macros.mixin.impl;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.server.function.CommandFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CommandFunction.class)
public abstract class CommandFunctionMixin {

    @ModifyReturnValue(method = "isValidMacroVariableName", at = @At("RETURN"))
    private static boolean advancement_macros$allowOtherChars(boolean original, String name) {

        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            if (!Character.isLetterOrDigit(ch) && !(ch == '_' || ch == '.')) {
                return false;
            }
        }

        return true;

    }

}
