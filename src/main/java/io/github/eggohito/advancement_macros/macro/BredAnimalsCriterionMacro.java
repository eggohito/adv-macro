package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Pair;

public class BredAnimalsCriterionMacro extends Macro {

    public static final String PARENT_KEY_FIELD = "parent_key";
    public static final String PARTNER_KEY_FIELD = "partner_key";
    public static final String CHILD_KEY_FIELD = "child_key";

    public static final Codec<BredAnimalsCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf(PARENT_KEY_FIELD, "parent").forGetter(BredAnimalsCriterionMacro::getParentKey),
        Codec.STRING.optionalFieldOf(PARTNER_KEY_FIELD, "partner").forGetter(BredAnimalsCriterionMacro::getPartnerKey),
        Codec.STRING.optionalFieldOf(CHILD_KEY_FIELD, "child").forGetter(BredAnimalsCriterionMacro::getChildKey)
    ).apply(instance, BredAnimalsCriterionMacro::new));

    private final String parentKey;
    private final String partnerKey;
    private final String childKey;

    public BredAnimalsCriterionMacro(String parentKey, String partnerKey, String childKey) {
        super(Criteria.BRED_ANIMALS);
        this.parentKey = parentKey;
        this.partnerKey = partnerKey;
        this.childKey = childKey;
    }

    public String getParentKey() {
        return parentKey;
    }

    public String getPartnerKey() {
        return partnerKey;
    }

    public String getChildKey() {
        return childKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<AnimalEntity>ifPresent(PARENT_KEY_FIELD, parentAnimal ->
            rootNbt.putString(parentKey, parentAnimal.getUuidAsString())
        );

        context.<AnimalEntity>ifPresent(PARTNER_KEY_FIELD, partnerAnimal ->
            rootNbt.putString(partnerKey, partnerAnimal.getUuidAsString())
        );

        context.<PassiveEntity>ifPresent(CHILD_KEY_FIELD, childEntity ->
            rootNbt.putString(childKey, childEntity.getUuidAsString())
        );

    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.BRED_ANIMALS, () -> CODEC);
    }

}
