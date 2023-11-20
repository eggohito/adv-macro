package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Pair;

public class FallFromHeightCriterionMacro extends TravelCriterionMacro {

    public static final Codec<FallFromHeightCriterionMacro> CODEC = createCodec(FallFromHeightCriterionMacro::new);

    public FallFromHeightCriterionMacro(String startLocationKey) {
        super(Criteria.FALL_FROM_HEIGHT, startLocationKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.FALL_FROM_HEIGHT, () -> CODEC);
    }

}
