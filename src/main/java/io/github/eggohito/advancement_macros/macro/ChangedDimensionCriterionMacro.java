package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class ChangedDimensionCriterionMacro extends Macro {

    public static final Codec<ChangedDimensionCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf("from_dimension_key", "from_dimension").forGetter(ChangedDimensionCriterionMacro::getFromDimensionKey),
        Codec.STRING.optionalFieldOf("to_dimension_key", "to_dimension").forGetter(ChangedDimensionCriterionMacro::getToDimensionKey)
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
    public void writeToNbt(NbtCompound rootNbt, String name, Object object) {

        if (name.equals("to_dimension") && object instanceof RegistryKey<?> toRegKey) {
            rootNbt.putString(toDimensionKey, toRegKey.getValue().toString());
        }

        if (name.equals("from_dimension") && object instanceof RegistryKey<?> fromRegKey) {
            rootNbt.putString(fromDimensionKey, fromRegKey.getValue().toString());
        }

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.CHANGED_DIMENSION.getId(), () -> CODEC);
    }

}
