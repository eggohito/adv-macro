package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;

public class NetherTravelCriterionMacro extends TravelCriterionMacro {

    public static final Codec<NetherTravelCriterionMacro> CODEC = createCodec(NetherTravelCriterionMacro::new);

    public NetherTravelCriterionMacro(String startLocationKey) {
        super(startLocationKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return new Factory(Criteria.NETHER_TRAVEL, () -> CODEC);
    }

}
