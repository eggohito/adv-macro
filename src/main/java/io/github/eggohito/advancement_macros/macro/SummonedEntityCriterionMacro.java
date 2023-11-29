package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;

public class SummonedEntityCriterionMacro extends Macro {

    public static final String ENTITY_KEY = "entity";

    public static final Codec<SummonedEntityCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(ENTITY_KEY, ENTITY_KEY).forGetter(SummonedEntityCriterionMacro::getEntityKey)
    ).apply(instance, SummonedEntityCriterionMacro::new));

    private final String entityKey;

    public SummonedEntityCriterionMacro(String entityKey) {
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
        context.<Entity>ifPresent(ENTITY_KEY, summonedEntity ->
            rootNbt.putString(entityKey, summonedEntity.getUuidAsString())
        );
    }

    public static Factory getFactory() {
        return new Factory(Criteria.SUMMONED_ENTITY, () -> CODEC);
    }

}
