package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Pair;

public class LocationCriterionMacro extends TickCriterionMacro {

    public static final Codec<LocationCriterionMacro> CODEC = createEmptyCodec(LocationCriterionMacro::new);

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.LOCATION, () -> CODEC);
    }

}
