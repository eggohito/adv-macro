package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;

public class AllayDropItemOnBlockCriterionMacro extends ItemCriterionMacro {

    public static final Codec<AllayDropItemOnBlockCriterionMacro> CODEC = createCodec(AllayDropItemOnBlockCriterionMacro::new);

    public AllayDropItemOnBlockCriterionMacro(String locationKey, String itemKey) {
        super(locationKey, itemKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return new Factory(Criteria.ALLAY_DROP_ITEM_ON_BLOCK, () -> CODEC);
    }

}
