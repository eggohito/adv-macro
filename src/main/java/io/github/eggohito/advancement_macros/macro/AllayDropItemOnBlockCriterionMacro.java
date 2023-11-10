package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Pair;

public class AllayDropItemOnBlockCriterionMacro extends ItemCriterionMacro {

    public static final Codec<AllayDropItemOnBlockCriterionMacro> CODEC = getCodec(AllayDropItemOnBlockCriterionMacro::new);

    public AllayDropItemOnBlockCriterionMacro(String locationKey, String itemKey) {
        super(Criteria.ALLAY_DROP_ITEM_ON_BLOCK, locationKey, itemKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.ALLAY_DROP_ITEM_ON_BLOCK, () -> CODEC);
    }

}
