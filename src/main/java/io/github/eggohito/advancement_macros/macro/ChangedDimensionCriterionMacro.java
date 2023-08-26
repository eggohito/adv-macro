package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class ChangedDimensionCriterionMacro extends Macro {

    public static final String FROM_DIMENSION_KEY_FIELD = "from_dimension_key";
    public static final String TO_DIMENSION_KEY_FIELD = "to_dimension_key";

    public static final Codec<ChangedDimensionCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(FROM_DIMENSION_KEY_FIELD, "from_dimension").forGetter(ChangedDimensionCriterionMacro::getFromDimensionKey),
        Codec.STRING.optionalFieldOf(TO_DIMENSION_KEY_FIELD, "to_dimension").forGetter(ChangedDimensionCriterionMacro::getToDimensionKey)
    ).apply(instance, ChangedDimensionCriterionMacro::new));

    private final String fromDimensionKey;
    private final String toDimensionKey;

    public ChangedDimensionCriterionMacro(String fromDimensionKey, String toDimensionKey) {
        super(Criteria.CHANGED_DIMENSION.getId());
        this.fromDimensionKey = fromDimensionKey;
        this.toDimensionKey = toDimensionKey;
    }

    public String getFromDimensionKey() {
        return fromDimensionKey;
    }

    public String getToDimensionKey() {
        return toDimensionKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<RegistryKey<World>>ifPresent(FROM_DIMENSION_KEY_FIELD, fromDimensionRegKey ->
            rootNbt.putString(fromDimensionKey, fromDimensionRegKey.getValue().toString())
        );

        context.<RegistryKey<World>>ifPresent(TO_DIMENSION_KEY_FIELD, toDimensionRegKey ->
            rootNbt.putString(toDimensionKey, toDimensionRegKey.getValue().toString())
        );

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.CHANGED_DIMENSION.getId(), () -> CODEC);
    }

}
