package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Pair;

public class ItemUsedOnBlockCriterionMacro extends ItemCriterionMacro {

    public static final Codec<ItemUsedOnBlockCriterionMacro> CODEC = createCodec(ItemUsedOnBlockCriterionMacro::new);

    public ItemUsedOnBlockCriterionMacro(String locationKey, String itemKey) {
        super(locationKey, itemKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return () -> new Pair<>(Criteria.ITEM_USED_ON_BLOCK, () -> CODEC);
    }

}
