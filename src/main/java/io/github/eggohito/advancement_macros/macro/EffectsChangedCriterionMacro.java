package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;

public class EffectsChangedCriterionMacro extends Macro {

    public static final String EFFECTS_KEY = "effects";
    public static final String SOURCE_KEY = "source";

    public static final Codec<EffectsChangedCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(EFFECTS_KEY, EFFECTS_KEY).forGetter(EffectsChangedCriterionMacro::getEffectsKey),
        Codec.STRING.optionalFieldOf(SOURCE_KEY, SOURCE_KEY).forGetter(EffectsChangedCriterionMacro::getSourceKey)
    ).apply(instance, EffectsChangedCriterionMacro::new));

    private final String effectsKey;
    private final String sourceKey;

    public EffectsChangedCriterionMacro(String effectsKey, String sourceKey) {
        super(Criteria.EFFECTS_CHANGED);
        this.effectsKey = effectsKey;
        this.sourceKey = sourceKey;
    }

    public String getEffectsKey() {
        return effectsKey;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<Entity>ifPresent(SOURCE_KEY, statusEffectSourceEntity ->
            rootNbt.putString(sourceKey, statusEffectSourceEntity.getUuidAsString())
        );
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.EFFECTS_CHANGED, () -> CODEC);
    }

}
