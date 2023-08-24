package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class AllayDropItemOnBlockMacro extends ItemCriterionMacro {

    public static final Codec<AllayDropItemOnBlockMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf("location_key", "location").forGetter(AllayDropItemOnBlockMacro::getLocationKey),
        Codec.STRING.optionalFieldOf("item_key", "item").forGetter(AllayDropItemOnBlockMacro::getLocationKey)
    ).apply(instance, AllayDropItemOnBlockMacro::new));

    public AllayDropItemOnBlockMacro(String locationKey, String itemKey) {
        super(Criteria.ALLAY_DROP_ITEM_ON_BLOCK.getId(), locationKey, itemKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.ALLAY_DROP_ITEM_ON_BLOCK.getId(), () -> CODEC);
    }

}
