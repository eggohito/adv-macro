package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class AllayDropItemOnBlockCriterionMacro extends ItemCriterionMacro {

    public static final Codec<ItemCriterionMacro> CODEC = getCodec(AllayDropItemOnBlockCriterionMacro::new);

    public AllayDropItemOnBlockCriterionMacro(String locationKey, String itemKey) {
        super(Criteria.ALLAY_DROP_ITEM_ON_BLOCK.getId(), locationKey, itemKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.ALLAY_DROP_ITEM_ON_BLOCK.getId(), () -> CODEC);
    }

}
