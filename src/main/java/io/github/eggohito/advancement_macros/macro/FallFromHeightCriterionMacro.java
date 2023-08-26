package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class FallFromHeightCriterionMacro extends TravelCriterionMacro {

    public static final Codec<TravelCriterionMacro> CODEC = getCodec(FallFromHeightCriterionMacro::new);

    public FallFromHeightCriterionMacro(String startLocationKey) {
        super(Criteria.FALL_FROM_HEIGHT.getId(), startLocationKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.FALL_FROM_HEIGHT.getId(), () -> CODEC);
    }

}
