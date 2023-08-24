package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class PlacedBlockMacro extends ItemCriterionMacro {

    public static final Codec<PlacedBlockMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.STRING.optionalFieldOf("location_key", "location").forGetter(PlacedBlockMacro::getLocationKey),
        Codec.STRING.optionalFieldOf("item_key", "item").forGetter(PlacedBlockMacro::getItemKey)
    ).apply(instance, PlacedBlockMacro::new));

    public PlacedBlockMacro(String locationKey, String itemKey) {
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
