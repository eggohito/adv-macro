package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class TameAnimalCriterionMacro extends Macro {

    public static final String TAMED_ANIMAL_KEY_FIELD = "tamed_animal_key";
    public static final Codec<TameAnimalCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(TAMED_ANIMAL_KEY_FIELD, "tamed_animal").forGetter(TameAnimalCriterionMacro::getTamedAnimalKey)
    ).apply(instance, TameAnimalCriterionMacro::new));

    private final String tamedAnimalKey;
    public TameAnimalCriterionMacro(String tamedAnimalKey) {
        super(Criteria.TAME_ANIMAL.getId());
        this.tamedAnimalKey = tamedAnimalKey;
    }

    public String getTamedAnimalKey() {
        return tamedAnimalKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<AnimalEntity>ifPresent(TAMED_ANIMAL_KEY_FIELD, tamedAnimalEntity ->
            rootNbt.putString(tamedAnimalKey, tamedAnimalEntity.getUuidAsString())
        );
    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.TAME_ANIMAL.getId(), () -> CODEC);
    }

}
