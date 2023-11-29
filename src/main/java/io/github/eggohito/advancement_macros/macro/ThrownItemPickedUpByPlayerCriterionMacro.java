package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Pair;

public class ThrownItemPickedUpByPlayerCriterionMacro extends ThrownItemPickedUpByEntityCriterionMacro {

    public static final Codec<ThrownItemPickedUpByPlayerCriterionMacro> CODEC = createCodec(ThrownItemPickedUpByPlayerCriterionMacro::new);

    public ThrownItemPickedUpByPlayerCriterionMacro(String thrownItemKey, String entityKey) {
        super(thrownItemKey, entityKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.THROWN_ITEM_PICKED_UP_BY_PLAYER, () -> CODEC);
    }

}
