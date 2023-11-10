package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Pair;

public class RideEntityInLavaCriterionMacro extends TravelCriterionMacro {

    public static final Codec<RideEntityInLavaCriterionMacro> CODEC = getCodec(RideEntityInLavaCriterionMacro::new);

    public RideEntityInLavaCriterionMacro(String startLocationKey) {
        super(Criteria.RIDE_ENTITY_IN_LAVA, startLocationKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.RIDE_ENTITY_IN_LAVA, () -> CODEC);
    }

}
