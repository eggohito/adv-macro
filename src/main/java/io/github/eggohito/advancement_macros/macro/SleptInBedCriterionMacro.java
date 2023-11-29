package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;

public class SleptInBedCriterionMacro extends TickCriterionMacro {

    public static final Codec<SleptInBedCriterionMacro> CODEC = createEmptyCodec(SleptInBedCriterionMacro::new);

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return new Factory(Criteria.SLEPT_IN_BED, () -> CODEC);
    }

}
