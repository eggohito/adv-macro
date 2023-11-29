package io.github.eggohito.advancement_macros.macro;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.advancement_macros.api.Macro;
import io.github.eggohito.advancement_macros.data.TriggerContext;
import io.github.eggohito.advancement_macros.util.NbtUtil;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

public class ShotCrossbowCriterionMacro extends Macro {

    public static final String ITEM_KEY = "item";

    public static final Codec<ShotCrossbowCriterionMacro> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        strictOptionalField(ITEM_KEY, ITEM_KEY).forGetter(ShotCrossbowCriterionMacro::getItemKey)
    ).apply(instance, ShotCrossbowCriterionMacro::new));

    private final String itemKey;

    public ShotCrossbowCriterionMacro(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemKey() {
        return itemKey;
    }

    @Override
    public Type getType() {
        return () -> CODEC;
    }

    @Override
    public void writeToNbt(NbtCompound rootNbt, TriggerContext context) {
        context.<ItemStack>ifPresent(ITEM_KEY, usedCrossbowItemStack ->
            NbtUtil.writeItemStackToNbt(rootNbt, itemKey, usedCrossbowItemStack)
        );
    }

    public static Factory getFactory() {
        return new Factory(Criteria.SHOT_CROSSBOW, () -> CODEC);
    }

}
