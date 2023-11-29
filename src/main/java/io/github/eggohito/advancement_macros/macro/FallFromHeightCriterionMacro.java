package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;

public class FallFromHeightCriterionMacro extends TravelCriterionMacro {

    public static final Codec<FallFromHeightCriterionMacro> CODEC = createCodec(FallFromHeightCriterionMacro::new);

    public FallFromHeightCriterionMacro(String startLocationKey) {
        super(startLocationKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return new Factory(Criteria.FALL_FROM_HEIGHT, () -> CODEC);
    }

}
