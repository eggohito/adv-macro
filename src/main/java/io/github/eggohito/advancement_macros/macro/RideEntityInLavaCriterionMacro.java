package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;

public class RideEntityInLavaCriterionMacro extends TravelCriterionMacro {

    public static final Codec<RideEntityInLavaCriterionMacro> CODEC = createCodec(RideEntityInLavaCriterionMacro::new);

    public RideEntityInLavaCriterionMacro(String startLocationKey) {
        super(startLocationKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return new Factory(Criteria.RIDE_ENTITY_IN_LAVA, () -> CODEC);
    }

}
