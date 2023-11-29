package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;

public class HeroOfTheVillageCriterionMacro extends TickCriterionMacro {

    public static final Codec<HeroOfTheVillageCriterionMacro> CODEC = createEmptyCodec(HeroOfTheVillageCriterionMacro::new);

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return new Factory(Criteria.HERO_OF_THE_VILLAGE, () -> CODEC);
    }

}
