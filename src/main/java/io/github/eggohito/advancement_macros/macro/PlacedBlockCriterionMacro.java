package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;

public class PlacedBlockCriterionMacro extends ItemCriterionMacro {

    public static final Codec<PlacedBlockCriterionMacro> CODEC = createCodec(PlacedBlockCriterionMacro::new);

    public PlacedBlockCriterionMacro(String locationKey, String itemKey) {
        super(locationKey, itemKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Factory getFactory() {
        return new Factory(Criteria.PLACED_BLOCK, () -> CODEC);
    }

}
