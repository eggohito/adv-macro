package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;

import java.util.function.BiFunction;

public abstract class ItemCriterionMacro extends Macro {

    public static final String LOCATION_KEY = "location";
    public static final String ITEM_KEY = "item";

    private final String locationKey;
    private final String itemKey;

    public ItemCriterionMacro(Criterion<?> baseCriterion, String locationKey, String itemKey) {
        super(baseCriterion);
        this.locationKey = locationKey;
        this.itemKey = itemKey;
    }

    protected static <T extends ItemCriterionMacro> Codec<T> getCodec(BiFunction<String, String, T> macroFunction) {
        return RecordCodecBuilder.create(instance -> instance.group(
            strictOptionalField(LOCATION_KEY, LOCATION_KEY).forGetter(ItemCriterionMacro::getLocationKey),
            strictOptionalField(ITEM_KEY, ITEM_KEY).forGetter(ItemCriterionMacro::getItemKey)
        ).apply(instance, macroFunction));
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {

        context.<BlockPos>ifPresent(LOCATION_KEY, locationPos ->
            NbtUtil.writeBlockPosToNbt(rootNbt, locationKey, locationPos)
        );

        context.<ItemStack>ifPresent(ITEM_KEY, itemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, itemKey, itemStack)
        );

    }

    public String getLocationKey() {
        return locationKey;
    }

    public String getItemKey() {
        return itemKey;
    }

}
