package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;

public class TameAnimalCriterionMacro extends Macro {

    public static final String ENTITY_KEY = "entity_key";

    public static final Codec<TameAnimalCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(ENTITY_KEY, ENTITY_KEY).forGetter(TameAnimalCriterionMacro::getEntityKey)
    ).apply(instance, TameAnimalCriterionMacro::new));

    private final String entityKey;

    public TameAnimalCriterionMacro(String entityKey) {
        super(Criteria.TAME_ANIMAL);
        this.entityKey = entityKey;
    }

    public String getEntityKey() {
        return entityKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<AnimalEntity>ifPresent(ENTITY_KEY, tamedAnimalEntity ->
            rootNbt.putString(entityKey, tamedAnimalEntity.getUuidAsString())
        );
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.TAME_ANIMAL, () -> CODEC);
    }

}
