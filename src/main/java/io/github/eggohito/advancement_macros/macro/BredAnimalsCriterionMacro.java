package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class BredAnimalsCriterionMacro extends Macro {

    public static final Codec<BredAnimalsCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf("parent_key", "parent").forGetter(BredAnimalsCriterionMacro::getParentKey),
        Codec.STRING.optionalFieldOf("partner_key", "partner").forGetter(BredAnimalsCriterionMacro::getPartnerKey),
        Codec.STRING.optionalFieldOf("child_key", "child").forGetter(BredAnimalsCriterionMacro::getChildKey)
    ).apply(instance, BredAnimalsCriterionMacro::new));

    private final String parentKey;
    private final String partnerKey;
    private final String childKey;

    public BredAnimalsCriterionMacro(String parentKey, String partnerKey, String childKey) {
        super(Criteria.BRED_ANIMALS.getId());
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
    public void writeToNbt(NbtCompound rootNbt, String name, Object object) {

        if (name.equals("parent") && object instanceof AnimalEntity parent) {
            rootNbt.putString(parentKey, parent.getUuidAsString());
        }

        if (name.equals("partner") && object instanceof AnimalEntity partner) {
            rootNbt.putString(partnerKey, partner.getUuidAsString());
        }

        if (name.equals("child") && object instanceof PassiveEntity child) {
            rootNbt.putString(childKey, child.getUuidAsString());
        }

    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.BRED_ANIMALS.getId(), () -> CODEC);
    }

}
