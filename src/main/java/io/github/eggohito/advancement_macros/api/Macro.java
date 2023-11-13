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
 *      <p>A class used for serializing the data passed to an {@link net.minecraft.advancement.criterion.AbstractCriterion}
 *      when triggered to NBT. The NBT will then be passed to the reward function of the advancement that contains a criterion that uses
 *      the criterion trigger.</p>
 */
public abstract class Macro {

    private final Identifier id;

    /**
     *      <p>Instantiate a {@link Macro} instance for the specified {@link Criterion}.</p>
     *
     *      @param baseCriterion    The criterion trigger to base this macro handler from.
     */
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
     *      <p>Serialize the data from the passed {@linkplain TriggerContext trigger context} into the
     *      passed {@linkplain NbtCompound NBT} using this macro.</p>
     *
     *      @param rootNbt  The {@linkplain NbtCompound NBT} to serialize the data to.
     *      @param context  The {@linkplain TriggerContext trigger context} to serialize.
     */
    public abstract void writeToNbt(NbtCompound rootNbt, TriggerContext context);

    /**
     *      <p>Create an empty {@link Codec} for the specified {@link Macro}. This is mostly used for criterion triggers that do
     *      not provide any fields.</p>
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
