package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public abstract class Macro {

    private final Identifier id;
    public Macro(Identifier id) {
        this.id = id;
    }

    public Identifier getId() {
        return id;
    }

    public abstract Type getType();

    public abstract void writeToNbt(NbtCompound rootNbt, Object object);

    public interface Type {
        Codec<? extends Macro> getCodec();
    }

}
