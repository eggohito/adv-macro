package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;

public class AvoidVibrationCriterionMacro extends TickCriterionMacro {

    public static final Codec<AvoidVibrationCriterionMacro> CODEC = createEmptyCodec(AvoidVibrationCriterionMacro::new);

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return new Factory(Criteria.AVOID_VIBRATION, () -> CODEC);
    }

}
