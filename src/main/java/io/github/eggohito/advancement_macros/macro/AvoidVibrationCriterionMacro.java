package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Pair;

public class AvoidVibrationCriterionMacro extends TickCriterionMacro {

    public static final Codec<AvoidVibrationCriterionMacro> CODEC = createEmptyCodec(AvoidVibrationCriterionMacro::new);

    public AvoidVibrationCriterionMacro() {
        super(Criteria.AVOID_VIBRATION);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.AVOID_VIBRATION, () -> CODEC);
    }

}
