package io.github.eggohito.advancement_macros.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.MapCodec;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

/**
 *      <p>A class used for associating a serializer for a
 *      {@linkplain net.minecraft.advancement.criterion.AbstractCriterion criterion trigger}. The serializer is used
 *      for serializing the data of said criterion trigger to NBT to be passed to the reward function of the
 *      advancement containing the criterion.</p>
 */
public abstract class Macro {

    private final Identifier id;

    public Macro(Criterion<?> baseCriterion) {
        this.id = Criteria.getId(baseCriterion);
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
    public abstract void writeToNbt(NbtCompound rootNbt, TriggerContext context);

    /**
     *      <p>Create an empty {@link Codec} for the specified {@link Macro}.</p>
     *
     *      @param supplier The {@link Supplier supplier} of the {@link Macro}.
     *      @return         The {@link Codec} for the supplied {@link Macro}.
     */
    public static <T extends Macro> Codec<T> createEmptyCodec(java.util.function.Supplier<T> supplier) {
        return MapCodec.of(Encoder.empty(), Decoder.unit(supplier)).codec();
    }

    /**
     *      <p>Represents the codec for the macro, used for taking the custom mappings that a user specifies within
     *      an advancement for serializing the data of the criterion trigger the macro associates with.</p>
     */
    @FunctionalInterface
    public interface Type {
        Codec<? extends Macro> getCodec();
    }

    @FunctionalInterface
    public interface Supplier {
        Factory getFactory();
    }

    @FunctionalInterface
    public interface Factory {
        Pair<Criterion<?>, Type> getData();
    }

}
