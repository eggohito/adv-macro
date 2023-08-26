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

public class SummonedEntityCriterionMacro extends Macro {

    public static final String SUMMONED_ENTITY_KEY_FIELD = "summoned_entity_key";
    public static final Codec<SummonedEntityCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(SUMMONED_ENTITY_KEY_FIELD, "summoned_entity").forGetter(SummonedEntityCriterionMacro::getSummonedEntityKey)
    ).apply(instance, SummonedEntityCriterionMacro::new));

    private final String summonedEntityKey;
    public SummonedEntityCriterionMacro(String summonedEntityKey) {
        super(Criteria.SUMMONED_ENTITY.getId());
        this.summonedEntityKey = summonedEntityKey;
    }

    public String getSummonedEntityKey() {
        return summonedEntityKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<Entity>ifPresent(SUMMONED_ENTITY_KEY_FIELD, summonedEntity ->
            rootNbt.putString(summonedEntityKey, summonedEntity.getUuidAsString())
        );
    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.SUMMONED_ENTITY.getId(), () -> CODEC);
    }

}
