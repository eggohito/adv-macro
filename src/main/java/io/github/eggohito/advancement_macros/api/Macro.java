package io.github.eggohito.advancement_macros.api;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

/**
 *      <p>A class used for associating a serializer for a
 *      {@linkplain net.minecraft.advancement.criterion.AbstractCriterion criterion trigger}. The serializer is used
 *      for serializing the data of said criterion trigger to NBT to be passed to the reward function of the
 *      advancement containing the criterion.</p>
 */
public abstract class Macro {

    private final Identifier id;
    public Macro(Identifier id) {
        this.id = id;
    }

    /**
     *      @return     The {@linkplain Identifier ID} of the criterion trigger this macro corresponds with.
     */
    public Identifier getId() {
        return id;
    }

    /**
     *      @return     The {@linkplain Type type} of the macro.
     */
    public abstract Type getType();

    /**
     *      <p>Serialize the passed {@linkplain Object object} to the passed {@linkplain NbtCompound NBT} using this
     *      macro. This is used for serializing data in the way specifically for this macro (or rather, for the
     *      criterion trigger this macro associates with).</p>
     *
     *      @param rootNbt  The {@linkplain NbtCompound NBT} to serialize the object to.
     *      @param object   The {@linkplain Object object} to serialize.
     */
    public void writeToNbt(NbtCompound rootNbt, Object object) {}

    /**
     *      <p>Same as {@link #writeToNbt(NbtCompound, Object)}, except the {@linkplain Object object} is mapped with
     *      a {@linkplain String string}. This is used for when the data that will be serialized is of the same type
     *      and need to be distinguished from one another.</p>
     *
     *      @param rootNbt  The {@linkplain NbtCompound NBT} to serialize the object to.
     *      @param name     The name of the mapping for the object.
     *      @param object   The {@linkplain Object object} to serialize.
     */
    public void writeToNbt(NbtCompound rootNbt, String name, Object object) {}

    /**
     *      <p>Represents the codec for the macro, used for taking the custom mappings that a user specifies within
     *      an advancement for serializing the data of the criterion trigger the macro associates with.</p>
     */
    public interface Type {
        Codec<? extends Macro> getCodec();
    }

}
