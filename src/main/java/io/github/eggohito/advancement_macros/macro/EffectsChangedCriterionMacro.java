package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class EffectsChangedCriterionMacro extends Macro {

    public static final String STATUS_EFFECTS_SOURCE_KEY_FIELD = "status_effects_source_key";
    public static final Codec<EffectsChangedCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(STATUS_EFFECTS_SOURCE_KEY_FIELD, "status_effects_source").forGetter(EffectsChangedCriterionMacro::getStatusEffectsSourceKey)
    ).apply(instance, EffectsChangedCriterionMacro::new));

    private final String statusEffectsSourceKey;
    public EffectsChangedCriterionMacro(String statusEffectsSourceKey) {
        super(Criteria.EFFECTS_CHANGED.getId());
        this.statusEffectsSourceKey = statusEffectsSourceKey;
    }

    public String getStatusEffectsSourceKey() {
        return statusEffectsSourceKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<Entity>ifPresent(STATUS_EFFECTS_SOURCE_KEY_FIELD, statusEffectSourceEntity ->
            rootNbt.putString(statusEffectsSourceKey, statusEffectSourceEntity.getUuidAsString())
        );
    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.EFFECTS_CHANGED.getId(), () -> CODEC);
    }

}
