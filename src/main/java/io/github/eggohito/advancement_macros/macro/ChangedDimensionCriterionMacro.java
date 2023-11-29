package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class ChangedDimensionCriterionMacro extends Macro {

    public static final String FROM_KEY = "from";
    public static final String TO_KEY = "to";

    public static final Codec<ChangedDimensionCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(FROM_KEY, FROM_KEY).forGetter(ChangedDimensionCriterionMacro::getFromKey),
        strictOptionalField(TO_KEY, TO_KEY).forGetter(ChangedDimensionCriterionMacro::getToKey)
    ).apply(instance, ChangedDimensionCriterionMacro::new));

    private final String fromKey;
    private final String toKey;

    public ChangedDimensionCriterionMacro(String fromKey, String toKey) {
        this.fromKey = fromKey;
        this.toKey = toKey;
    }

    public String getFromKey() {
        return fromKey;
    }

    public String getToKey() {
        return toKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<RegistryKey<World>>ifPresent(FROM_KEY, fromWorldRegistryKey ->
            rootNbt.putString(fromKey, fromWorldRegistryKey.getValue().toString())
        );

        context.<RegistryKey<World>>ifPresent(TO_KEY, toWorldRegistryKey ->
            rootNbt.putString(toKey, toWorldRegistryKey.getValue().toString())
        );

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.CHANGED_DIMENSION, () -> CODEC);
    }

}
