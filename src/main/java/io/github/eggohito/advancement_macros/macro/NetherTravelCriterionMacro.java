package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class NetherTravelCriterionMacro extends TravelCriterionMacro {

    public static final Codec<TravelCriterionMacro> CODEC = getCodec(NetherTravelCriterionMacro::new);

    public NetherTravelCriterionMacro(String startLocationKey) {
        super(Criteria.NETHER_TRAVEL.getId(), startLocationKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.NETHER_TRAVEL.getId(), () -> CODEC);
    }

}
