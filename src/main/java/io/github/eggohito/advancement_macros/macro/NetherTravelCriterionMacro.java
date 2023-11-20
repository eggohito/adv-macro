package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Pair;

public class NetherTravelCriterionMacro extends TravelCriterionMacro {

    public static final Codec<NetherTravelCriterionMacro> CODEC = createCodec(NetherTravelCriterionMacro::new);

    public NetherTravelCriterionMacro(String startLocationKey) {
        super(Criteria.NETHER_TRAVEL, startLocationKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.NETHER_TRAVEL, () -> CODEC);
    }

}
