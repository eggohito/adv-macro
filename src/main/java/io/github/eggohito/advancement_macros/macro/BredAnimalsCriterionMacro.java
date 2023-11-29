package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;

public class BredAnimalsCriterionMacro extends Macro {

    public static final String PARENT_KEY = "parent";
    public static final String PARTNER_KEY = "partner";
    public static final String CHILD_KEY = "child";

    public static final Codec<BredAnimalsCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(PARENT_KEY, PARENT_KEY).forGetter(BredAnimalsCriterionMacro::getParentKey),
        strictOptionalField(PARTNER_KEY, PARTNER_KEY).forGetter(BredAnimalsCriterionMacro::getPartnerKey),
        strictOptionalField(CHILD_KEY, CHILD_KEY).forGetter(BredAnimalsCriterionMacro::getChildKey)
    ).apply(instance, BredAnimalsCriterionMacro::new));

    private final String parentKey;
    private final String partnerKey;
    private final String childKey;

    public BredAnimalsCriterionMacro(String parentKey, String partnerKey, String childKey) {
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

        context.<AnimalEntity>ifPresent(PARENT_KEY, parentAnimal ->
            rootNbt.putString(parentKey, parentAnimal.getUuidAsString())
        );

        context.<AnimalEntity>ifPresent(PARTNER_KEY, partnerAnimal ->
            rootNbt.putString(partnerKey, partnerAnimal.getUuidAsString())
        );

        context.<PassiveEntity>ifPresent(CHILD_KEY, childEntity ->
            rootNbt.putString(childKey, childEntity.getUuidAsString())
        );

    }

    public static Factory getFactory() {
        return new Factory(Criteria.BRED_ANIMALS, () -> CODEC);
    }

}
