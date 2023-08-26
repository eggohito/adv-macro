package io.github.eggohito.advancement_macros.api;

import com.mojang.serialization.Codec;
import io.github.eggohito.advancement_macros.data.TriggerContext;
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
     *      <p>Serialize the passed {@linkplain TriggerContext trigger context} into the
     *      passed {@linkplain NbtCompound NBT} using this macro.</p>
     *
     *      @param rootNbt  The {@linkplain NbtCompound NBT} to serialize the map to.
     *      @param context  The {@linkplain TriggerContext trigger context} to serialize.
     */
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {}

    /**
     *      <p>Represents the codec for the macro, used for taking the custom mappings that a user specifies within
     *      an advancement for serializing the data of the criterion trigger the macro associates with.</p>
     */
    public interface Type {
        Codec<? extends Macro> getCodec();
    }

}
