package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Pair;

public class KillMobNearSculkCatalystCriterionMacro extends OnKilledCriterionMacro {

    public static final Codec<KillMobNearSculkCatalystCriterionMacro> CODEC = createCodec(KillMobNearSculkCatalystCriterionMacro::new);

    public KillMobNearSculkCatalystCriterionMacro(String killerKey, String victimKey, String killingBlowKey) {
        super(killerKey, victimKey, killingBlowKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.KILL_MOB_NEAR_SCULK_CATALYST, () -> CODEC);
    }

}
