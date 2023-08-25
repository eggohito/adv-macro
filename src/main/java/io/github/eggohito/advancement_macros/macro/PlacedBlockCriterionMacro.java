package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class PlacedBlockCriterionMacro extends ItemCriterionMacro {

    public static final Codec<ItemCriterionMacro> CODEC = getCodec(PlacedBlockCriterionMacro::new);

    public PlacedBlockCriterionMacro(String locationKey, String itemKey) {
        super(Criteria.PLACED_BLOCK.getId(), locationKey, itemKey);
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    public static Pair<Identifier, Type> getFactory() {
        return new Pair<>(Criteria.PLACED_BLOCK.getId(), () -> CODEC);
    }

}
